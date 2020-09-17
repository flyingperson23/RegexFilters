package flyingperson.regexfilters;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;

public class RegexTileEntity extends TileEntity implements ITickable {
    public static final int SIZE = 9;

    String exp = RegexFilters.DUMMY_STRING;
    public final RegexItemStackHandler itemStackHandler = new RegexItemStackHandler(SIZE, this);
    private boolean shouldUpdate = false;
    private NBTTagCompound updateTag;
    int counter;

    private void doUpdateFromNBT() {
        if (updateTag != null && world != null) {
            if (shouldUpdate && !world.isRemote) {
                if (updateTag.hasKey("items")) {
                    itemStackHandler.deserializeNBT((NBTTagCompound) updateTag.getTag("items"));
                }
                if (updateTag.hasKey("expression")) {
                    exp = updateTag.getString("expression");
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                    markDirty();
                }
                shouldUpdate = false;
                updateTag = null;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        shouldUpdate = true;
        updateTag = compound;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", itemStackHandler.serializeNBT());
        if (exp != null) compound.setString("expression", exp);
        return compound;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        }
        return super.getCapability(capability, facing);
    }


    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        NBTTagCompound nbtTag = new NBTTagCompound();
        if (exp != null) nbtTag.setString("expression", exp);
        nbtTag.merge(super.getUpdateTag());
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.getNbtCompound();
        handleUpdateTag(tag);
        if (tag.hasKey("expression")) this.exp = tag.getString("expression");
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void update() {
        if (exp == null || exp.equals("")) exp = RegexFilters.DUMMY_STRING;
        doUpdateFromNBT();
        counter++;
    }

    public static ArrayList<String> getNames(ItemStack stack) {
        ArrayList<String> names = new ArrayList<>();
        ResourceLocation registryName = stack.getItem().getRegistryName();
        if (registryName != null) {
            names.add(registryName.getResourceDomain()+":"+registryName.getResourcePath());
        }
        names.add(stack.getDisplayName());
        if (!stack.isEmpty()) Arrays.stream(OreDictionary.getOreIDs(stack)).forEach((i) -> names.add("ore:"+OreDictionary.getOreName(i)));
        return names;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (!this.world.isRemote) this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
    }

}

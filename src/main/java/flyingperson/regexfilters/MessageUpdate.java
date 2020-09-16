package flyingperson.regexfilters;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public class MessageUpdate implements IMessage {
    public MessageUpdate(){}

    private NBTTagCompound tag;
    private BlockPos pos;
    public MessageUpdate(RegexTileEntity te) {
        this.tag = te.writeToNBT(new NBTTagCompound());
        this.pos = te.getPos();
    }

    @Override public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeCompoundTag(tag);
        buffer.writeLong(pos.toLong());
    }

    @Override public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        try {
            this.tag = buffer.readCompoundTag();
            this.pos = BlockPos.fromLong(buffer.readLong());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class MessageHandler implements IMessageHandler<MessageUpdate, IMessage> {
        @Override public IMessage onMessage(MessageUpdate message, MessageContext ctx) {
            if (message.tag != null) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
                    if (te != null) {
                        te.readFromNBT(message.tag);
                        te.getWorld().notifyBlockUpdate(te.getPos(), te.getWorld().getBlockState(te.getPos()), te.getWorld().getBlockState(te.getPos()), 3);
                        te.markDirty();
                    }
                });
            }
            return null;
        }
    }
}
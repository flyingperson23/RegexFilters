package flyingperson.regexfilters;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class RegexItemStackHandler extends ItemStackHandler {
    private final RegexTileEntity te;
    public RegexItemStackHandler(int size, RegexTileEntity te) {
        super(size);
        this.te = te;
    }
    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        for (String s : RegexTileEntity.getNames(stack)) {
            if (Pattern.compile(te.exp).matcher(s).find()) return true;
        }
        return false;
    }

    @Override
    protected void onContentsChanged(int slot) {
        te.markDirty();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (isItemValid(slot, stack)) super.setStackInSlot(slot, stack);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (isItemValid(slot, stack)) return super.insertItem(slot, stack, simulate);
        return stack;
    }
}

package flyingperson.regexfilters;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageRequestUpdate implements IMessage {
    public MessageRequestUpdate(){}

    private BlockPos pos;
    public MessageRequestUpdate(BlockPos pos) {
        this.pos = pos;
    }

    @Override public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
    }

    @Override public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
    }

    public static class MessageHandler implements IMessageHandler<MessageRequestUpdate, IMessage> {
        @Override public IMessage onMessage(MessageRequestUpdate message, MessageContext ctx) {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            World world = serverPlayer.world;
            serverPlayer.getServerWorld().addScheduledTask(() -> {
                TileEntity te = world.getTileEntity(message.pos);
                if (te instanceof RegexTileEntity) {
                    RegexFilters.INSTANCE.sendTo(new MessageUpdate((RegexTileEntity) te), serverPlayer);
                    IBlockState state = world.getBlockState(message.pos);
                    world.notifyBlockUpdate(message.pos, state, state, 3);
                    te.markDirty();
                }
            });
            return null;
        }
    }
}
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

import java.nio.charset.Charset;

public class MessageSetExpression implements IMessage {
    public MessageSetExpression(){}

    private BlockPos pos;
    private String str;
    public MessageSetExpression(String str, BlockPos pos) {
        this.str = str;
        this.pos = pos;
    }

    @Override public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(str.toCharArray().length);
        buf.writeCharSequence(str, Charset.defaultCharset());
    }

    @Override public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        int len = buf.readInt();
        str = String.valueOf(buf.readCharSequence(len, Charset.defaultCharset()));
    }

    public static class MessageHandler implements IMessageHandler<MessageSetExpression, IMessage> {
        @Override public IMessage onMessage(MessageSetExpression message, MessageContext ctx) {
            if (message.str != null) {
                EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
                World world = serverPlayer.world;
                serverPlayer.getServerWorld().addScheduledTask(() -> {
                    TileEntity te = world.getTileEntity(message.pos);
                    if (te instanceof RegexTileEntity) {
                        ((RegexTileEntity) te).exp = message.str;
                        IBlockState state = world.getBlockState(message.pos);
                        world.notifyBlockUpdate(message.pos, state, state, 3);
                        te.markDirty();
                    }
                });
            }
            return null;
        }
    }
}
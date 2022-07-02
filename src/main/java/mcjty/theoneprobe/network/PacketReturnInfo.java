package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PacketReturnInfo implements IMessage {

    private int dim;
    private BlockPos pos;
    private ProbeInfo probeInfo;

    public PacketReturnInfo() {/**/}

    public PacketReturnInfo(int dim, @Nonnull BlockPos pos, @Nonnull ProbeInfo probeInfo) {
        this.dim = dim;
        this.pos = pos;
        this.probeInfo = probeInfo;
    }

    public static class Handler implements IMessageHandler<PacketReturnInfo, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(@Nonnull PacketReturnInfo message, @Nonnull MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> OverlayRenderer.registerProbeInfo(message.dim, message.pos, message.probeInfo));
            return null;
        }
    }

    @Override
    public void fromBytes(@Nonnull ByteBuf buf) {
        this.dim = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        if (buf.readBoolean()) {
            this.probeInfo = new ProbeInfo();
            this.probeInfo.fromBytes(buf);
        } else {
            this.probeInfo = null;
        }
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        if (this.probeInfo != null) {
            buf.writeBoolean(true);
            this.probeInfo.toBytes(buf);
        } else {
            buf.writeBoolean(false);
        }
    }
}

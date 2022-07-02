package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class PacketReturnEntityInfo implements IMessage {

    private UUID uuid;
    private ProbeInfo probeInfo;

    public PacketReturnEntityInfo() {/**/}

    public PacketReturnEntityInfo(@Nonnull UUID uuid, @Nonnull ProbeInfo probeInfo) {
        this.uuid = uuid;
        this.probeInfo = probeInfo;
    }

    public static class Handler implements IMessageHandler<PacketReturnEntityInfo, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(@Nonnull PacketReturnEntityInfo message, @Nonnull MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> OverlayRenderer.registerProbeInfo(message.uuid, message.probeInfo));
            return null;
        }
    }

    @Override
    public void fromBytes(@Nonnull ByteBuf buf) {
        this.uuid = new UUID(buf.readLong(), buf.readLong());
        if (buf.readBoolean()) {
            this.probeInfo = new ProbeInfo();
            this.probeInfo.fromBytes(buf);
        } else {
            this.probeInfo = null;
        }
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        buf.writeLong(this.uuid.getMostSignificantBits());
        buf.writeLong(this.uuid.getLeastSignificantBits());
        if (this.probeInfo != null) {
            buf.writeBoolean(true);
            this.probeInfo.toBytes(buf);
        } else {
            buf.writeBoolean(false);
        }
    }
}

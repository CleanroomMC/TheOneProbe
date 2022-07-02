package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitEntityData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;
import static mcjty.theoneprobe.config.ConfigSetup.PROBE_NEEDEDFOREXTENDED;
import static mcjty.theoneprobe.config.ConfigSetup.PROBE_NEEDEDHARD;

public class PacketGetEntityInfo implements IMessage {

    private int dim;
    private UUID uuid;
    private ProbeMode mode;
    private Vec3d hitVec;

    public PacketGetEntityInfo() {/**/}

    public PacketGetEntityInfo(int dim, @Nonnull ProbeMode mode, @Nonnull RayTraceResult mouseOver, @Nonnull Entity entity) {
        this.dim = dim;
        this.uuid = entity.getPersistentID();
        this.mode = mode;
        this.hitVec = mouseOver.hitVec;
    }

    public static class Handler implements IMessageHandler<PacketGetEntityInfo, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(PacketGetEntityInfo message, @Nonnull MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(@Nonnull PacketGetEntityInfo message, @Nonnull MessageContext ctx) {
            WorldServer world = DimensionManager.getWorld(message.dim);
            if (world != null) {
                Entity entity = world.getEntityFromUuid(message.uuid);
                if (entity != null) {
                    ProbeInfo probeInfo = getProbeInfo(ctx.getServerHandler().player, message.mode, world, entity, message.hitVec);
                    PacketHandler.INSTANCE.sendTo(new PacketReturnEntityInfo(message.uuid, probeInfo), ctx.getServerHandler().player);
                }
            }
        }
    }

    @Nullable
    private static ProbeInfo getProbeInfo(@Nonnull EntityPlayer player, @Nonnull ProbeMode mode, @Nonnull World world, @Nonnull Entity entity, @Nonnull Vec3d hitVec) {
        if (ConfigSetup.needsProbe == PROBE_NEEDEDFOREXTENDED) {
            // We need a probe only for extended information
            if (!ModItems.hasAProbeSomewhere(player)) {
                // No probe anywhere, switch EXTENDED to NORMAL
                if (mode == ProbeMode.EXTENDED) {
                    mode = ProbeMode.NORMAL;
                }
            }
        } else if (ConfigSetup.needsProbe == PROBE_NEEDEDHARD && !ModItems.hasAProbeSomewhere(player)) {
            // The server says we need a probe but we don't have one in our hands or on our head
            return null;
        }

        final ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();
        final IProbeHitEntityData data = new ProbeHitEntityData(hitVec);

        IProbeConfig probeConfig = TheOneProbe.theOneProbeImp.createProbeConfig();
        for (IProbeConfigProvider configProvider : TheOneProbe.theOneProbeImp.getConfigProviders()) {
            configProvider.getProbeConfig(probeConfig, player, world, entity, data);
        }
        ConfigSetup.setRealConfig(probeConfig);

        List<IProbeInfoEntityProvider> entityProviders = TheOneProbe.theOneProbeImp.getEntityProviders();
        for (IProbeInfoEntityProvider provider : entityProviders) {
            try {
                provider.addProbeEntityInfo(mode, probeInfo, player, world, entity, data);
            } catch (Throwable e) {
                ThrowableIdentity.registerThrowable(e);
                probeInfo.text(LABEL + "{*theoneprobe.provider.error*} " + ERROR + provider.getID());
            }
        }
        return probeInfo;
    }

    @Override
    public void fromBytes(@Nonnull ByteBuf buf) {
        this.dim = buf.readInt();
        this.uuid = new UUID(buf.readLong(), buf.readLong());
        this.mode = ProbeMode.VALUES[buf.readByte()];
        if (buf.readBoolean()) {
            this.hitVec = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        buf.writeInt(this.dim);
        buf.writeLong(this.uuid.getMostSignificantBits());
        buf.writeLong(this.uuid.getLeastSignificantBits());
        buf.writeByte(this.mode.ordinal());
        if (this.hitVec == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeDouble(this.hitVec.x);
            buf.writeDouble(this.hitVec.y);
            buf.writeDouble(this.hitVec.z);
        }
    }
}

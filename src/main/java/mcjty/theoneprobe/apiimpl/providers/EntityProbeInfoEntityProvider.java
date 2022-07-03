package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Nonnull
    @Override
    public String getID() {
        return String.format("%s:entity.entity", TheOneProbe.MODID);
    }

    @Override
    public void addProbeEntityInfo(@Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull Entity entity, @Nonnull IProbeHitEntityData data) {
        if (entity instanceof IProbeInfoEntityAccessor) {
            ((IProbeInfoEntityAccessor) entity).addProbeInfo(mode, probeInfo, player, world, entity, data);
        }
    }
}

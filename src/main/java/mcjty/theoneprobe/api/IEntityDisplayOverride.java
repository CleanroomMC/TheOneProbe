package mcjty.theoneprobe.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Implement this interface if you want a custom display for your own entities instead of the standard
 * display.
 */
public interface IEntityDisplayOverride {

    /**
     * This function returns true if you handled the probe info yourselves and TOP doesn't have to add its
     * own info.
     */
    boolean overrideStandardInfo(@Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull Entity entity, @Nonnull IProbeHitEntityData data);

}

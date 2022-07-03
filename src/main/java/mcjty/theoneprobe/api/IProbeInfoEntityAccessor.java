package mcjty.theoneprobe.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * You can implement this in your entity implementation if you want to support
 * the probe. An alternative to this is to make an IProveInfoProvider.
 * Note that if you implement this then it will be called last (after all the providers)
 */
public interface IProbeInfoEntityAccessor {

    /**
     * Add information for the probe info for the given entity. This is always
     * called server side.
     * The given probeInfo object represents a vertical layout. So adding elements to that
     * will cause them to be grouped vertically.
     */
    void addProbeInfo(@Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull Entity entity, @Nonnull IProbeHitEntityData data);
}

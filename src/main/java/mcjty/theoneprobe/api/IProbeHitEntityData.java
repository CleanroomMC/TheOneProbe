package mcjty.theoneprobe.api;

import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

/**
 * Access information about where the probe hit the entity
 */
public interface IProbeHitEntityData {

    @Nullable
    Vec3d getHitVec();
}

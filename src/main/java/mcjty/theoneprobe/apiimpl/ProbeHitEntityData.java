package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class ProbeHitEntityData implements IProbeHitEntityData {

    private final Vec3d hitVec;

    public ProbeHitEntityData(@Nullable Vec3d hitVec) {
        this.hitVec = hitVec;
    }

    @Nullable
    @Override
    public Vec3d getHitVec() {
        return null;
    }
}

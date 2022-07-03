package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeHitData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

public class ProbeHitData implements IProbeHitData {

    private final BlockPos pos;
    private final Vec3d hitVec;
    private final EnumFacing side;
    private final ItemStack pickBlock;

    public ProbeHitData(@Nonnull BlockPos pos, @Nonnull Vec3d hitVec, @Nonnull EnumFacing side, @Nonnull ItemStack pickBlock) {
        this.pos = pos;
        this.hitVec = hitVec;
        this.side = side;
        this.pickBlock = pickBlock;
    }

    @Nonnull
    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Nonnull
    @Override
    public Vec3d getHitVec() {
        return hitVec;
    }

    @Nonnull
    @Override
    public EnumFacing getSideHit() {
        return side;
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock() {
        return pickBlock;
    }
}

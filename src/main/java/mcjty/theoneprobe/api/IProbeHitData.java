package mcjty.theoneprobe.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

/**
 * Access information about where the probe hit the block
 */
public interface IProbeHitData {

    @Nonnull
    BlockPos getPos();

    @Nonnull
    Vec3d getHitVec();

    @Nonnull
    EnumFacing getSideHit();

    /**
     * Access the client-side result of getPickBlock() for the given block. That way
     * you don't have to call this server side because that can sometimes be
     * problematic
     * @return the picked block or {@link ItemStack#EMPTY}
     */
    @Nonnull
    ItemStack getPickBlock();
}

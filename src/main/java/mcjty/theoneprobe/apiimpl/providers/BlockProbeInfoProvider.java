package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockProbeInfoProvider implements IProbeInfoProvider {

    @Nonnull
    @Override
    public String getID() {
        return String.format("%s:block", TheOneProbe.MODID);
    }

    @Override
    public void addProbeInfo(@Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull IBlockState blockState, @Nonnull IProbeHitData data) {
        final Block block = blockState.getBlock();
        if (block instanceof IProbeInfoAccessor) {
            ((IProbeInfoAccessor) block).addProbeInfo(mode, probeInfo, player, world, blockState, data);
        }
    }
}

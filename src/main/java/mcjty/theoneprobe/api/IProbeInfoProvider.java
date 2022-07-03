package mcjty.theoneprobe.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * You can implement IProbeInfoAccessor in your blocks or else you can use
 * this and register that to the ITheOneProbe. Note that TheOneProbe already
 * adds default providers which gives basic block information.
 */
public interface IProbeInfoProvider {

    /**
     * @return Return a unique ID (usually combined with the modid) to identify this provider.
     */
    @Nonnull
    String getID();

    /**
     * Add information for the probe info for the given block. This is always called
     * server side.
     * The given probeInfo object represents a vertical layout. So adding elements to that
     * will cause them to be grouped vertically.
     */
    void addProbeInfo(@Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull IBlockState blockState, @Nonnull IProbeHitData data);
}

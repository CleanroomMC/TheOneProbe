package mcjty.theoneprobe.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Implement this interface if you want to override the default
 * probe config for some of your blocks or entities.
 */
public interface IProbeConfigProvider {

    /**
     * Possibly override the config for this entity. You can make modifications to the given 'config' which starts
     * from default.
     */
    void getProbeConfig(@Nonnull IProbeConfig config, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull Entity entity, @Nonnull IProbeHitEntityData data);

    /**
     * Possibly override the config for this block. You can make modifications to the given 'config' which starts
     * from default.
     */
    void getProbeConfig(@Nonnull IProbeConfig config, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull IBlockState blockState, @Nonnull IProbeHitData data);

}

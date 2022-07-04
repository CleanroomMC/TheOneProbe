package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.ConfigSetup;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;

public class DebugProbeInfoProvider implements IProbeInfoProvider {

    @Nonnull
    @Override
    public String getID() {
        return String.format("%s:debug", TheOneProbe.MODID);
    }

    @Override
    public void addProbeInfo(@Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull IBlockState blockState, @Nonnull IProbeHitData data) {
        if (mode == ProbeMode.DEBUG) {
            showDebugInfo(probeInfo, world, blockState, data.getPos(), blockState.getBlock(), data.getSideHit());
        }
    }

    @SuppressWarnings("deprecation")
    private void showDebugInfo(@Nonnull IProbeInfo probeInfo, @Nonnull World world, @Nonnull IBlockState blockState, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull EnumFacing side) {
        final String simpleName = block.getClass().getSimpleName();
        IProbeInfo vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xFFFF4444).spacing(2));
        if (block.getRegistryName() != null) vertical.text(LABEL + "Reg Name: " + INFO + block.getRegistryName().toString());
        vertical.text(LABEL + "Unlocalized Name: " + INFO + block.getTranslationKey())
                .text(LABEL + "Meta: " + INFO + blockState.getBlock().getMetaFromState(blockState))
                .text(LABEL + "Class: " + INFO + simpleName)
                .text(LABEL + "Hardness: " + INFO + block.getBlockHardness(blockState, world, pos))
                .text(LABEL + "Power W: " + INFO + block.getWeakPower(blockState, world, pos, side.getOpposite())
                        + LABEL + ", S: " + INFO + block.getStrongPower(blockState, world, pos, side.getOpposite()))
                .text(LABEL + "Light: " + INFO + block.getLightValue(blockState, world, pos));
        final TileEntity te = world.getTileEntity(pos);
        if (te != null) vertical.text(LABEL + "TE: " + INFO + te.getClass().getSimpleName());
    }
}

package mcjty.theoneprobe.apiimpl.providers;

import mcjty.lib.api.power.IBigPower;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.compat.RedstoneFluxTools;
import mcjty.theoneprobe.compat.TeslaTools;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.setup.ModSetup;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;

import static mcjty.theoneprobe.api.TextStyleClass.*;

public class DefaultProbeInfoProvider implements IProbeInfoProvider {

    @Nonnull
    @Override
    public String getID() {
        return String.format("%s:default", TheOneProbe.MODID);
    }

    @Override
    public void addProbeInfo(@Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull IBlockState blockState, @Nonnull IProbeHitData data) {

        boolean handled = false;
        for (IBlockDisplayOverride override : TheOneProbe.theOneProbeImp.getBlockOverrides()) {
            if (override.overrideStandardInfo(mode, probeInfo, player, world, blockState, data)) {
                handled = true;
                break;
            }
        }

        final IProbeConfig config = ConfigSetup.getRealConfig();

        final Block block = blockState.getBlock();
        final BlockPos pos = data.getPos();

        if (!handled) {
            showStandardBlockInfo(config, mode, probeInfo, blockState, block, world, pos, player, data);
        }

        if (Tools.show(mode, config.getShowCropPercentage())) showGrowthLevel(probeInfo, blockState);

        boolean showHarvestLevel = Tools.show(mode, config.getShowHarvestLevel());
        boolean showHarvested = Tools.show(mode, config.getShowCanBeHarvested());
        if (showHarvested && showHarvestLevel)
            HarvestInfoTools.showHarvestInfo(probeInfo, world, pos, block, blockState, player);
        else if (showHarvested) HarvestInfoTools.showCanBeHarvested(probeInfo, world, pos, block, player);
        else if (showHarvestLevel) HarvestInfoTools.showHarvestLevel(probeInfo, blockState, block);

        if (Tools.show(mode, config.getShowRedstone()))
            showRedstonePower(probeInfo, world, blockState, data, block, Tools.show(mode, config.getShowLeverSetting()));
        if (Tools.show(mode, config.getShowLeverSetting())) showLeverSetting(probeInfo, world, blockState, data, block);

        ChestInfoTools.showChestInfo(mode, probeInfo, world, pos, config);

        if (config.getRFMode() > 0) showRF(probeInfo, world, pos);
        if (Tools.show(mode, config.getShowTankSetting())) {
            if (config.getTankMode() > 0) showTankInfo(probeInfo, world, pos);
        }

        if (Tools.show(mode, config.getShowBrewStandSetting())) showBrewingStandInfo(probeInfo, world, data, block);
        if (Tools.show(mode, config.getShowMobSpawnerSetting())) showMobSpawnerInfo(probeInfo, world, data, block);
    }

    private void showBrewingStandInfo(@Nonnull IProbeInfo probeInfo, @Nonnull World world, @Nonnull IProbeHitData data, @Nonnull Block block) {
        if (block instanceof BlockBrewingStand) {
            final TileEntity te = world.getTileEntity(data.getPos());
            if (te instanceof TileEntityBrewingStand) {
                int brewTime = ((TileEntityBrewingStand) te).getField(0);
                int fuel = ((TileEntityBrewingStand) te).getField(1);
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(new ItemStack(Items.BLAZE_POWDER), probeInfo.defaultItemStyle().width(16).height(16))
                        .text(LABEL + "{*theoneprobe.provider.block.fuel*} " + INFO + fuel);
                if (brewTime > 0) {
                    probeInfo.text(LABEL + "{*theoneprobe.provider.block.time*} " + INFO + StringUtils.ticksToElapsedTime(brewTime));
                }
            }
        }
    }

    private void showMobSpawnerInfo(@Nonnull IProbeInfo probeInfo, @Nonnull World world, @Nonnull IProbeHitData data, @Nonnull Block block) {
        if (block instanceof BlockMobSpawner) {
            TileEntity te = world.getTileEntity(data.getPos());
            if (te instanceof TileEntityMobSpawner) {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle()
                        .alignment(ElementAlignment.ALIGN_CENTER))
                        .text(LABEL + "{*theoneprobe.provider.block.mob*} " +
                                INFO + ((TileEntityMobSpawner) te).getSpawnerBaseLogic().getCachedEntity().getName());
            }
        }
    }

    private void showRedstonePower(@Nonnull IProbeInfo probeInfo, @Nonnull World world, @Nonnull IBlockState blockState, @Nonnull IProbeHitData data, @Nonnull Block block, boolean showLever) {
        // We are showing the lever setting so we don't show redstone in that case
        if (showLever && block instanceof BlockLever) return;

        int redstonePower;
        if (block instanceof BlockRedstoneWire) redstonePower = blockState.getValue(BlockRedstoneWire.POWER);
        else redstonePower = world.getRedstonePower(data.getPos(), data.getSideHit().getOpposite());

        if (redstonePower > 0) {
            probeInfo.horizontal()
                    .item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14))
                    .text(LABEL + "{*theoneprobe.provider.block.power*} " + INFO + redstonePower);
        }
    }

    private void showLeverSetting(@Nonnull IProbeInfo probeInfo, @Nonnull World world, @Nonnull IBlockState blockState, @Nonnull IProbeHitData data, @Nonnull Block block) {
        if (block instanceof BlockLever) {
            boolean powered = blockState.getValue(BlockLever.POWERED);
            probeInfo.horizontal().item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14))
                    .text(LABEL + "{*theoneprobe.provider.block.state*} " + INFO + (powered ? "{*theoneprobe.provider.block.on*}" : "{*theoneprobe.provider.block.off*}"));
        } else if (block instanceof BlockRedstoneComparator) {
            BlockRedstoneComparator.Mode mode = blockState.getValue(BlockRedstoneComparator.MODE);
            probeInfo.text(LABEL + "{*theoneprobe.provider.block.mode*} " + INFO + mode.getName());
        } else if (block instanceof BlockRedstoneRepeater) {
            boolean locked = blockState.getValue(BlockRedstoneRepeater.LOCKED);
            int delay = blockState.getValue(BlockRedstoneRepeater.DELAY);
            probeInfo.text(LABEL + "{*theoneprobe.provider.block.delay*} " + INFO + delay + " {*theoneprobe.provider.ticks*}");
            if (locked) probeInfo.text(INFO + "{*theoneprobe.provider.block.locked*}");
        }
    }

    private void showTankInfo(@Nonnull IProbeInfo probeInfo, @Nonnull World world, @Nonnull BlockPos pos) {
        final TileEntity te = world.getTileEntity(pos);
        if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            if (handler != null) {
                IFluidTankProperties[] properties = handler.getTankProperties();
                if (properties != null) {
                    for (IFluidTankProperties property : properties) {
                        if (property != null) {
                            FluidStack fluidStack = property.getContents();
                            addFluidInfo(probeInfo, ConfigSetup.getDefaultConfig(), fluidStack, property.getCapacity());
                        }
                    }
                }
            }
        }
    }

    private void addFluidInfo(@Nonnull IProbeInfo probeInfo, @Nonnull ProbeConfig config, @Nullable FluidStack fluidStack, int maxContents) {
        if (fluidStack != null)
            probeInfo.text(NAME + "{*theoneprobe.provider.block.fluid*} " + fluidStack.getLocalizedName());
        int contents = fluidStack == null ? 0 : fluidStack.amount;

        if (config.getTankMode() == 1) {
            probeInfo.progress(contents, maxContents,
                    probeInfo.defaultProgressStyle()
                            .suffix("mB")
                            .filledColor(ConfigSetup.tankbarFilledColor)
                            .alternateFilledColor(ConfigSetup.tankbarAlternateFilledColor)
                            .borderColorTop(ConfigSetup.tankbarBorderColorTop)
                            .borderColorBottom(ConfigSetup.tankbarBorderColorBottom)
                            .numberFormat(ConfigSetup.tankFormat));
        } else {
            probeInfo.text(PROGRESS + ElementProgress.format(contents, ConfigSetup.tankFormat, "{*theoneprobe.provider.mb*}"));
        }
    }

    private void showRF(@Nonnull IProbeInfo probeInfo, @Nonnull World world, @Nonnull BlockPos pos) {
        ProbeConfig config = ConfigSetup.getDefaultConfig();
        final TileEntity te = world.getTileEntity(pos);
        if (ModSetup.tesla && TeslaTools.isEnergyHandler(te)) {
            long energy = TeslaTools.getEnergy(te);
            long maxEnergy = TeslaTools.getMaxEnergy(te);
            addRFInfo(probeInfo, config, energy, maxEnergy);
        } else if (te instanceof IBigPower) {
            long energy = ((IBigPower) te).getStoredPower();
            long maxEnergy = ((IBigPower) te).getCapacity();
            addRFInfo(probeInfo, config, energy, maxEnergy);
        } else if (ModSetup.redstoneflux && RedstoneFluxTools.isEnergyHandler(te)) {
            int energy = RedstoneFluxTools.getEnergy(te);
            int maxEnergy = RedstoneFluxTools.getMaxEnergy(te);
            addRFInfo(probeInfo, config, energy, maxEnergy);
        } else if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage handler = te.getCapability(CapabilityEnergy.ENERGY, null);
            if (handler != null) addRFInfo(probeInfo, config, handler.getEnergyStored(), handler.getMaxEnergyStored());
        }
    }

    private void addRFInfo(@Nonnull IProbeInfo probeInfo, @Nonnull ProbeConfig config, long energy, long maxEnergy) {
        if (config.getRFMode() == 1) {
            probeInfo.progress(energy, maxEnergy,
                    probeInfo.defaultProgressStyle()
                            .suffix("RF")
                            .filledColor(ConfigSetup.rfbarFilledColor)
                            .alternateFilledColor(ConfigSetup.rfbarAlternateFilledColor)
                            .borderColorTop(ConfigSetup.rfbarBorderColorTop)
                            .borderColorBottom(ConfigSetup.rfbarBorderColorBottom)
                            .numberFormat(ConfigSetup.rfFormat));
        } else {
            probeInfo.text(PROGRESS + "{*theoneprobe.provider.block.fe*} " + ElementProgress.format(energy, ConfigSetup.rfFormat, "{*theoneprobe.provider.fe*}"));
        }
    }

    private void showGrowthLevel(@Nonnull IProbeInfo probeInfo, @Nonnull IBlockState blockState) {
        for (IProperty<?> property : blockState.getProperties().keySet()) {
            if (!"age".equals(property.getName())) continue;
            if (property.getValueClass() == Integer.class) {
                IProperty<Integer> integerProperty = (IProperty<Integer>) property;
                int age = blockState.getValue(integerProperty);
                int maxAge = Collections.max(integerProperty.getAllowedValues());
                if (age == maxAge) {
                    probeInfo.text(OK + "{*theoneprobe.provider.block.mature*}");
                } else {
                    probeInfo.text(LABEL + "{*theoneprobe.provider.block.growth*} " + WARNING + (age * 100) / maxAge + "%");
                }
            }
            return;
        }
    }

    public static void showStandardBlockInfo(@Nonnull IProbeConfig config, @Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull IBlockState blockState, @Nonnull Block block, @Nonnull World world,
                                             @Nonnull BlockPos pos, @Nonnull EntityPlayer player, @Nonnull IProbeHitData data) {
        ItemStack pickBlock = data.getPickBlock();

        if (block instanceof BlockSilverfish && mode != ProbeMode.DEBUG && !Tools.show(mode, config.getShowSilverfish())) {
            BlockSilverfish.EnumType type = blockState.getValue(BlockSilverfish.VARIANT);
            blockState = type.getModelBlock();
            block = blockState.getBlock();
            pickBlock = new ItemStack(block, 1, block.getMetaFromState(blockState));
        }

        final String modid = Tools.getModName(block);
        if (block instanceof BlockFluidBase || block instanceof BlockLiquid) {
            final Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
            if (fluid != null) {
                FluidStack fluidStack = new FluidStack(fluid, Fluid.BUCKET_VOLUME);
                ItemStack bucketStack = FluidUtil.getFilledBucket(fluidStack);

                IProbeInfo horizontal = probeInfo.horizontal();
                if (fluidStack.isFluidEqual(FluidUtil.getFluidContained(bucketStack))) {
                    horizontal.item(bucketStack);
                } else {
                    horizontal.icon(fluid.getStill(), -1, -1, 16, 16, probeInfo.defaultIconStyle().width(20));
                }

                horizontal.vertical().text(NAME + fluidStack.getLocalizedName()).text(MODNAME + modid);
                return;
            }
        }

        if (!pickBlock.isEmpty()) {
            if (Tools.show(mode, config.getShowModName())) {
                probeInfo.horizontal()
                        .item(pickBlock)
                        .vertical()
                        .itemLabel(pickBlock)
                        .text(MODNAME + modid);
            } else {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(pickBlock)
                        .itemLabel(pickBlock);
            }
        } else {
            if (Tools.show(mode, config.getShowModName())) {
                probeInfo.vertical()
                        .text(NAME + getBlockUnlocalizedName(block))
                        .text(MODNAME + modid);
            } else {
                probeInfo.vertical()
                        .text(NAME + getBlockUnlocalizedName(block));
            }
        }
    }

    @Nonnull
    private static String getBlockUnlocalizedName(@Nonnull Block block) {
        return String.format("{*%s.name*}", block.getTranslationKey());
    }
}

package mcjty.theoneprobe.apiimpl.providers;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.Map;

import static mcjty.theoneprobe.api.TextStyleClass.*;

public class HarvestInfoTools {

    private static final ResourceLocation ICONS = new ResourceLocation(TheOneProbe.MODID, "textures/gui/icons.png");

    //TODO read Harvest Levels from config to allow for better mod expansion
    private static final String[] harvestLevels = new String[]{
            "stone",
            "iron",
            "diamond",
            "obsidian",
            "cobalt"
    };

    //TODO display the tool corresponding to the harvest level
    private static final Map<String, ItemStack> testTools = new Object2ObjectOpenHashMap<>();
    static {
        testTools.put("shovel", new ItemStack(Items.WOODEN_SHOVEL));
        testTools.put("axe", new ItemStack(Items.WOODEN_AXE));
        testTools.put("pickaxe", new ItemStack(Items.WOODEN_PICKAXE));
    }

    public static void showHarvestLevel(@Nonnull IProbeInfo probeInfo, @Nonnull IBlockState blockState, @Nonnull Block block) {
        final String harvestTool = block.getHarvestTool(blockState);
        if (harvestTool != null) {
            int harvestLevel = block.getHarvestLevel(blockState);
            String harvestName;
            if (harvestLevel >= harvestLevels.length) {
                harvestName = Integer.toString(harvestLevel);
            } else if (harvestLevel < 0) {
                harvestName = Integer.toString(harvestLevel);
            } else {
                harvestName = harvestLevels[harvestLevel];
            }
            probeInfo.text(LABEL + "{*theoneprobe.provider.harvest.tool*} " + INFO + harvestTool + " (level " + harvestName + ")");
        }
    }

    public static void showCanBeHarvested(@Nonnull IProbeInfo probeInfo, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull EntityPlayer player) {
        // If the player holds the probe there is no need to show harvestability information as the
        // probe cannot harvest anything. This is only supposed to work in off hand.
        if (ModItems.isProbeInHand(player.getHeldItemMainhand())) return;
        if (block.canHarvestBlock(world, pos, player) && world.getBlockState(pos).getBlockHardness(world, pos) >= 0) {
            probeInfo.text(OK + "{*theoneprobe.provider.harvest.harvestable*}");
        } else {
            probeInfo.text(WARNING + "{*theoneprobe.provider.harvest.not_harvestable*}");
        }
    }

    public static void showHarvestInfo(@Nonnull IProbeInfo probeInfo, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull IBlockState blockState, @Nonnull EntityPlayer player) {
        String harvestTool = block.getHarvestTool(blockState);

        if (harvestTool == null) {
            // The block doesn't have an explicitly-set harvest tool, so we're going to test our wooden tools against the block.
            float blockHardness = blockState.getBlockHardness(world, pos);
            if (blockHardness > 0f) {
                for (Map.Entry<String, ItemStack> testToolEntry : testTools.entrySet()) {
                    // loop through our test tools until we find a winner.
                    ItemStack testTool = testToolEntry.getValue();

                    if (testTool != null && testTool.getItem() instanceof ItemTool) {
                        ItemTool toolItem = (ItemTool) testTool.getItem();
                        // @todo
                        if (testTool.getDestroySpeed(blockState) >= toolItem.toolMaterial.getEfficiency()) {
                            // BINGO!
                            harvestTool = testToolEntry.getKey();
                            break;
                        }
                    }
                }
            }
        }

        String harvestName = null;
        if (harvestTool != null) {
            int harvestLevel = block.getHarvestLevel(blockState);
            if (harvestLevel < 0) {
                // NOTE: When a block doesn't have an explicitly-set harvest tool, getHarvestLevel will return -1 for ANY tool. (Expected behavior)
            } else if (harvestLevel < harvestLevels.length) {
                harvestName = harvestLevels[harvestLevel];
            }
            harvestTool = StringUtils.capitalize(harvestTool);
        }

        boolean v = ConfigSetup.harvestStyleVanilla;
        final int offs = v ? 16 : 0;
        final int dim = v ? 13 : 16;

        final IProbeInfo horizontal = probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
        final IIconStyle iconStyle = probeInfo.defaultIconStyle().width(v ? 18 : 20).height(v ? 14 : 16).textureWidth(32).textureHeight(32);

        if (block.canHarvestBlock(world, pos, player) && world.getBlockState(pos).getBlockHardness(world, pos) >= 0) {
            horizontal.icon(ICONS, 0, offs, dim, dim, iconStyle)
                    .text(OK + ((harvestTool != null) ? harvestTool : "{*theoneprobe.provider.harvest.no_tool*}"));
        } else {
            //noinspection ConstantConditions
            if (harvestName == null || harvestName.isEmpty()) {
                horizontal.icon(ICONS, 16, offs, dim, dim, iconStyle)
                        .text(WARNING + ((harvestTool != null) ? harvestTool : "{*theoneprobe.provider.harvest.no_tool*}"));
            } else {
                //noinspection ConstantConditions
                horizontal.icon(ICONS, 16, offs, dim, dim, iconStyle)
                        .text(WARNING + ((harvestTool != null) ? harvestTool : "{*theoneprobe.provider.harvest.no_tool*}") +
                                " ({*theoneprobe.provider.harvest.level*} " + harvestName + ")");
            }
        }
    }
}

package mcjty.theoneprobe.apiimpl.providers;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.ConfigSetup;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;

public class ChestInfoTools {

    public static void showChestInfo(@Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull IProbeConfig config) {
        List<ItemStack> stacks = new ObjectArrayList<>();
        IProbeConfig.ConfigMode chestMode = config.getShowChestContents();
        if (chestMode == IProbeConfig.ConfigMode.EXTENDED && (ConfigSetup.showSmallChestContentsWithoutSneaking > 0 || !ConfigSetup.getInventoriesToShow().isEmpty())) {
            if (ConfigSetup.getInventoriesToShow().contains(world.getBlockState(pos).getBlock().getRegistryName())) {
                chestMode = IProbeConfig.ConfigMode.NORMAL;
            } else if (ConfigSetup.showSmallChestContentsWithoutSneaking > 0) {
                int slots = getChestContents(world, pos, stacks);
                if (slots <= ConfigSetup.showSmallChestContentsWithoutSneaking) {
                    chestMode = IProbeConfig.ConfigMode.NORMAL;
                }
            }
        } else if (chestMode == IProbeConfig.ConfigMode.NORMAL && !ConfigSetup.getInventoriesToNotShow().isEmpty()) {
            if (ConfigSetup.getInventoriesToNotShow().contains(world.getBlockState(pos).getBlock().getRegistryName())) {
                chestMode = IProbeConfig.ConfigMode.EXTENDED;
            }
        }

        if (Tools.show(mode, chestMode)) {
            if (stacks.isEmpty()) getChestContents(world, pos, stacks);

            if (world.getTileEntity(pos) instanceof TileEntityFurnace) return;

            if (!stacks.isEmpty()) {
                boolean showDetailed = Tools.show(mode, config.getShowChestContentsDetailed()) && stacks.size() <= ConfigSetup.showItemDetailThresshold;
                if (mode == ProbeMode.NORMAL) {
                    if (stacks.size() >= 5) stacks = stacks.subList(0, 5);
                }
                showChestContents(probeInfo, world, pos, stacks, mode != ProbeMode.NORMAL && showDetailed);
            }
        }
    }

    private static void addItemStack(@Nonnull List<ItemStack> stacks, @Nonnull Set<Item> foundItems, @Nonnull ItemStack stack) {
        if (stack.isEmpty()) return;
        if (foundItems.contains(stack.getItem())) {
            for (ItemStack s : stacks) {
                if (ItemHandlerHelper.canItemStacksStack(s, stack)) {
                    s.grow(stack.getCount());
                    return;
                }
            }
        }
        // If we come here we need to append a new stack
        stacks.add(stack.copy());
        foundItems.add(stack.getItem());
    }

    private static void showChestContents(@Nonnull IProbeInfo probeInfo, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull List<ItemStack> stacks, boolean detailed) {
        IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(ConfigSetup.chestContentsBorderColor).spacing(0));
        IProbeInfo horizontal = null;

        int rows = 0;
        int idx = 0;

        if (detailed) {
            for (ItemStack stackInSlot : stacks) {
                vertical.horizontal(new LayoutStyle().spacing(5).alignment(ElementAlignment.ALIGN_CENTER))
                        .item(stackInSlot, new ItemStyle().width(20).height(20))
                        .itemLabel(stackInSlot)
                        .text(INFO + " ");
            }
        } else {
            for (ItemStack stackInSlot : stacks) {
                if (idx % 9 == 0) {
                    horizontal = vertical.horizontal(new LayoutStyle().spacing(0));
                    rows++;
                    if (rows > 4) break;
                }
                if (horizontal == null) continue;
                horizontal.item(stackInSlot);
                idx++;
            }
        }
    }

    private static int getChestContents(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull List<ItemStack> stacks) {
        TileEntity te = world.getTileEntity(pos);

        Set<Item> foundItems = ConfigSetup.compactEqualStacks ? new ObjectOpenHashSet<>() : null;
        int maxSlots = 0;
        if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler capability = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (capability != null && foundItems != null) {
                for (int i = 0; i < capability.getSlots(); i++) {
                    addItemStack(stacks, foundItems, capability.getStackInSlot(i));
                }
            }
        } else if (te instanceof IInventory) {
            IInventory inventory = (IInventory) te;
            maxSlots = inventory.getSizeInventory();
            if (foundItems != null) {
                for (int i = 0; i < maxSlots; i++) {
                    addItemStack(stacks, foundItems, inventory.getStackInSlot(i));
                }
            }
        }
        return maxSlots;
    }
}

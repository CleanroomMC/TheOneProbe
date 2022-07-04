package mcjty.theoneprobe;

import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.gui.GuiConfig;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.keys.KeyBindings;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

public class ClientForgeEventHandlers {

    public static boolean ignoreNextGuiClose = false;

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (ignoreNextGuiClose) {
            if (event.getGui() == null && (Minecraft.getMinecraft().currentScreen instanceof GuiConfig)) {
                ignoreNextGuiClose = false;
                // We don't want our gui to be closed for a new 'null' gui
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void renderGameOverlayEvent(@Nonnull RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) return;

        if (ConfigSetup.holdKeyToMakeVisible) {
            if (!KeyBindings.toggleVisible.isKeyDown()) return;
        } else if (!ConfigSetup.isVisible) return;

        OverlayRenderer.renderHUD(hasItemInEitherHand(ModItems.creativeProbe) ? ProbeMode.DEBUG : getModeForPlayer(), event.getPartialTicks());
    }

    @Nonnull
    private ProbeMode getModeForPlayer() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        return player.isSneaking() ? ProbeMode.EXTENDED : ProbeMode.NORMAL;
    }

    private boolean hasItemInEitherHand(@Nonnull Item item) {
        ItemStack mainHand = Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack offHand = Minecraft.getMinecraft().player.getHeldItem(EnumHand.OFF_HAND);
        //noinspection ConstantConditions
        return (mainHand != null && mainHand.getItem() == item) ||
                (offHand != null && offHand.getItem() == item);
    }

    private boolean hasItemInMainHand(@Nonnull Item item) {
        ItemStack mainHand = Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND);
        //noinspection ConstantConditions
        return mainHand != null && mainHand.getItem() == item;
    }
}

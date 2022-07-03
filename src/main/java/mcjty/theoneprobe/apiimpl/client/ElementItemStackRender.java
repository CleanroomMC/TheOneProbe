package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class ElementItemStackRender {

    public static void render(@Nonnull ItemStack itemStack, @Nonnull IItemStyle style, int x, int y) {
        if (!itemStack.isEmpty()) {
            final int size = itemStack.getCount();
            String amount;
            if (size <= 1) {
                amount = "";
            } else if (size < 100000) {
                amount = String.valueOf(size);
            } else if (size < 1000000) {
                amount = size / 1000 + "k";
            } else if (size < 1000000000) {
                amount = size / 1000000 + "m";
            } else {
                amount = size / 1000000000 + "g";
            }

            if (!RenderHelper.renderItemStack(Minecraft.getMinecraft(), Minecraft.getMinecraft().getRenderItem(), itemStack, x + (style.getWidth() - 18) / 2, y + (style.getHeight() - 18) / 2, amount)) {
                // There was a crash rendering this item
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, TextFormatting.RED + "{*theoneprobe.provider.error*} " + itemStack.getDisplayName());
            }
        }
    }

}

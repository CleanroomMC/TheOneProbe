package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class ElementProgressRender {

    private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");
    private static final ResourceLocation SPRITES = new ResourceLocation(TheOneProbe.MODID, "textures/gui/sprites.png");


    public static void render(IProgressStyle style, long current, long max, int x, int y, int w, int h) {
        if (style.isLifeBar()) {
            renderLifeBar(current, x, y, w, h);
        } else if (style.isArmorBar()) {
            renderArmorBar(current, x, y, w, h);
        } else if (style.isArrowBar()) {
            renderArrowBar(current, max, x, y, w, h);
        } else {
            RenderHelper.drawThickBeveledBoxGradient(x, y, x + w, y + h, 1, style.getBorderColorTop(), style.getBorderColorBottom(), style.getBackgroundColor());

            Minecraft.getMinecraft().getTextureManager().bindTexture(SPRITES);
            RenderHelper.renderColor(RenderHelper.renderBarColor(style.getFilledColor()));
            if (style.isRenderBG()) {
                RenderHelper.drawTexturedModalRect(x + 1, y + 1, 4, 47, w - 2, 12);
            }

            if (current > 0 && max > 0) {
                // Determine the progress bar width, but limit it to the size of the element (minus 2).
                int dx = (int) Math.min((current * (w - 2) / max), w - 2);

                if (dx > 0) {
                    RenderHelper.renderColor(RenderHelper.renderBarColor(style.getFilledColor()));
                    RenderHelper.drawTexturedModalRect(x + 1, y + 1, 4, 35, dx, 12);
                }
            }
        }



        if (style.isShowText()) {
            RenderHelper.renderText(Minecraft.getMinecraft(), x + 3, y + 3, style.getPrefix() + ElementProgress.format(current, current > 9999 ? NumberFormat.COMPACT : NumberFormat.FULL, "") + "/" + ElementProgress.format(max, max > 9999 ? NumberFormat.COMPACT : NumberFormat.FULL, " " + style.getSuffix()), RenderHelper.renderColorToHSB(style.getFilledColor(), + 0.3f, 1.0f));
        }
    }

    private static void renderLifeBar(long current, int x, int y, int w, int h) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ICONS);
        if (current * 4 >= w) {
            // Shortened view
            RenderHelper.drawTexturedModalRect(x, y, 52, 0, 9, 9);
            RenderHelper.renderText(Minecraft.getMinecraft(), x + 12, y, TextFormatting.WHITE + String.valueOf((current / 2)));
        } else {
            for (int i = 0; i < current / 2; i++) {
                RenderHelper.drawTexturedModalRect(x, y, 52, 0, 9, 9);
                x += 8;
            }
            if (current % 2 != 0) {
                RenderHelper.drawTexturedModalRect(x, y, 61, 0, 9, 9);
            }
        }
    }

    private static void renderArmorBar(long current, int x, int y, int w, int h) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ICONS);
        if (current * 4 >= w) {
            // Shortened view
            RenderHelper.drawTexturedModalRect(x, y, 43, 9, 9, 9);
            RenderHelper.renderText(Minecraft.getMinecraft(), x + 12, y, TextFormatting.WHITE + String.valueOf((current / 2)));
        } else {
            for (int i = 0; i < current / 2; i++) {
                RenderHelper.drawTexturedModalRect(x, y, 43, 9, 9, 9);
                x += 8;
            }
            if (current % 2 != 0) {
                RenderHelper.drawTexturedModalRect(x, y, 25, 9, 9, 9);
            }
        }
    }

    //vanilla style arrow progress bar used in most guis to represent processing time
    private static void renderArrowBar(long current, long max, int x, int y, int w, int h) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(SPRITES);

        //bg
        RenderHelper.drawTexturedModalRect(x + 1, y + 1, 5, 16, 23, 15);


        if (current > 0 && max > 0) {
            // Determine the progress bar width, but limit it to the size of the element (minus 2).
            int dx = (int) Math.min((current * (w - 2) / max), w - 2);

            if (dx > 0) {
                RenderHelper.drawTexturedModalRect(x + 1, y + 1, 5, 0, dx, 15);
            }
        }
    }
}

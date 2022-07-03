package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ElementIconRender {

    public static void render(@Nonnull ResourceLocation icon, int x, int y, int w, int h, int u, int v, int txtw, int txth) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (u == -1) {
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(icon.toString());
            //noinspection ConstantConditions
            if (sprite == null) return;
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            RenderHelper.drawTexturedModalRect(x, y, sprite, w, h);
        } else {
            Minecraft.getMinecraft().getTextureManager().bindTexture(icon);
            RenderHelper.drawTexturedModalRect(x, y, u, v, w, h, txtw, txth);
        }
    }
}

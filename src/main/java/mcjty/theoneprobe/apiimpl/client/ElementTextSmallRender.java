package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Set;

import static mcjty.theoneprobe.api.IProbeInfo.ENDLOC;
import static mcjty.theoneprobe.api.IProbeInfo.STARTLOC;

public class ElementTextSmallRender {

    public static void render(@Nonnull String text, int x, int y) {
        RenderHelper.renderSmallText(Minecraft.getMinecraft(), x, y, stylizeString(text));
    }

    @Nonnull
    private static String stylizeString(@Nonnull String text) {
        while (text.contains(STARTLOC) && text.contains(ENDLOC)) {
            int start = text.indexOf(STARTLOC);
            int end = text.indexOf(ENDLOC);
            if (start < end) {
                // Translation is needed
                final String left = text.substring(0, start);
                String middle = text.substring(start + 2, end);
                middle = I18n.format(middle).trim();
                final String right = text.substring(end+2);
                text = left + middle + right;
            } else {
                break;
            }
        }
        if (text.contains("{=")) {
            Set<TextStyleClass> stylesNeedingContext = EnumSet.noneOf(TextStyleClass.class);
            TextStyleClass context = null;
            for (TextStyleClass styleClass : ConfigSetup.textStyleClasses.keySet()) {
                if (text.contains(styleClass.toString())) {
                    final String replacement = ConfigSetup.getTextStyle(styleClass);
                    if ("context".equals(replacement)) {
                        stylesNeedingContext.add(styleClass);
                    } else if (context == null) {
                        context = styleClass;
                        text = StringUtils.replace(text, styleClass.toString(), replacement);
                    } else {
                        text = StringUtils.replace(text, styleClass.toString(), replacement);
                    }
                }
            }
            if (context != null) {
                for (TextStyleClass styleClass : stylesNeedingContext) {
                    final String replacement = ConfigSetup.getTextStyle(context);
                    text = StringUtils.replace(text, styleClass.toString(), replacement);
                }
            }
        }
        return text;
    }

    public static int getWidth(String text) {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(stylizeString(text)) / 2;
    }
}

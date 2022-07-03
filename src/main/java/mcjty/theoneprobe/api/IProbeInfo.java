package mcjty.theoneprobe.api;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Information to return to the probe. Most methods here return the same probe info
 * object so that you can chain:
 * `probeInfo.item(diamond).text("My diamond").text("Next line");`
 * <p>
 * `horizontal()` and `vertical()` are exceptions. They return a new probe info object
 * representing the new horizontal or vertical layout that was created. Keep that in mind!
 * <p>
 * Note that you can safely use TextFormatting color codes in the text.
 */
public interface IProbeInfo {

    /**
     * Use IProbeInfo#STARTLOC and IProbeInfo#ENDLOC in your strings for localization on the client
     */
    String STARTLOC = "{*";

    /**
     * Use IProbeInfo#STARTLOC and IProbeInfo#ENDLOC in your strings for localization on the client
     */
    String ENDLOC = "*}";

    /**
     * Create a default layout style for the horizontal or vertical elements
     */
    ILayoutStyle defaultLayoutStyle();

    /**
     * Create a default style for the progress bar
     */
    IProgressStyle defaultProgressStyle();

    /**
     * Create a default style for the text element
     */
    ITextStyle defaultTextStyle();

    /**
     * Create a default style for the item element
     */
    IItemStyle defaultItemStyle();

    /**
     * Create a default style for the entity element
     */
    IEntityStyle defaultEntityStyle();

    /**
     * Create a default style for the icon element
     */
    IIconStyle defaultIconStyle();

    /**
     * Create an icon. If u and v are -1 then the default texture atlas is used
     * and TheOneProbe will try to find the resource location on that atlas
     */
    IProbeInfo icon(@Nonnull ResourceLocation icon, int u, int v, int w, int h, @Nonnull IIconStyle style);

    IProbeInfo icon(@Nonnull ResourceLocation icon, int u, int v, int w, int h);

    /**
     * entityName can be an old-style entity name (like 'Zombie') or the string
     * representation of a resourcelocation (like 'minecraft:zombie')
     */
    IProbeInfo entity(@Nonnull String entityName, @Nonnull IEntityStyle style);

    IProbeInfo entity(@Nonnull String entityName);

    IProbeInfo entity(@Nonnull Entity entity, @Nonnull IEntityStyle style);

    IProbeInfo entity(@Nonnull Entity entity);

    /**
     * Note that you can include TextStyleClass info in the given text which
     * will be translated to the right style client-side. You can also
     * include STARTLOC/ENDLOC tags to force translation to localized
     * data on the client
     */
    IProbeInfo text(@Nonnull String text, @Nonnull ITextStyle style);

    IProbeInfo text(@Nonnull String text);

    IProbeInfo textSmall(@Nonnull String text, @Nonnull ITextStyle style);

    IProbeInfo textSmall(@Nonnull String text);

    IProbeInfo item(@Nonnull ItemStack stack, @Nonnull IItemStyle style);

    IProbeInfo item(@Nonnull ItemStack stack);

    /**
     * A localized name of the stack
     */
    IProbeInfo itemLabel(@Nonnull ItemStack stack, @Nonnull ITextStyle style);

    IProbeInfo itemLabel(@Nonnull ItemStack stack);

    /**
     * This creates a progress bar of 100 width
     */
    IProbeInfo progress(int current, int max, @Nonnull IProgressStyle style);

    IProbeInfo progress(int current, int max);

    IProbeInfo progress(long current, long max, @Nonnull IProgressStyle style);

    IProbeInfo progress(long current, long max);

    /**
     * Create a new horizontal probe info as a child of this one. Note that the returned
     * probe info is the new horizontal layout and not this one!
     */
    IProbeInfo horizontal(@Nonnull ILayoutStyle style);

    IProbeInfo horizontal();

    /**
     * Create a new vertical probe info as a child of this one. Note that the returned
     * probe info is the new horizontal layout and not this one!
     */
    IProbeInfo vertical(@Nonnull ILayoutStyle style);

    IProbeInfo vertical();

    /**
     * Add a custom element. Make sure the factory for this element is properly registered.
     */
    IProbeInfo element(@Nonnull IElement element);
}

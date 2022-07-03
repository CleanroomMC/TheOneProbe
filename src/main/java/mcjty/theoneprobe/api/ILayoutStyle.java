package mcjty.theoneprobe.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Style for a horizontal or vertical layout.
 */
public interface ILayoutStyle {
    /**
     * The color that is used for the border of the progress bar
     */
    ILayoutStyle borderColor(Integer c);

    /**
     * The spacing to use between elements in this panel. -1 means to use default depending
     * on vertical vs horizontal.
     */
    ILayoutStyle spacing(int f);

    /**
     * Set the alignment of the elements inside this element. Default is {@link ElementAlignment#ALIGN_TOPLEFT}
     */
    ILayoutStyle alignment(ElementAlignment alignment);

    @Nullable
    Integer getBorderColor();

    int getSpacing();

    @Nonnull
    ElementAlignment getAlignment();
}

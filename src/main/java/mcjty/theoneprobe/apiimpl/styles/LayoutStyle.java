package mcjty.theoneprobe.apiimpl.styles;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.ILayoutStyle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Style for a horizontal or vertical layout.
 */
public class LayoutStyle implements ILayoutStyle {

    private Integer borderColor = null;
    private ElementAlignment alignment = ElementAlignment.ALIGN_TOPLEFT;
    private int spacing = -1;

    @Override
    public ILayoutStyle alignment(@Nonnull ElementAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @Nonnull
    @Override
    public ElementAlignment getAlignment() {
        return this.alignment;
    }

    /**
     * The color that is used for the border of the progress bar
     */
    @Override
    public LayoutStyle borderColor(@Nullable Integer c) {
        this.borderColor = c;
        return this;
    }

    /**
     * The spacing to use between elements in this panel. -1 means to use default depending
     * on vertical vs horizontal.
     */
    @Override
    public LayoutStyle spacing(int f) {
        this.spacing = f;
        return this;
    }

    @Nullable
    @Override
    public Integer getBorderColor() {
        return this.borderColor;
    }

    @Override
    public int getSpacing() {
        return this.spacing;
    }
}

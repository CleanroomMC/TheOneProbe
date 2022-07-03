package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementHorizontal extends AbstractElementPanel {

    public static final int SPACING = 5;

    public ElementHorizontal(@Nullable Integer borderColor, int spacing, @Nonnull ElementAlignment alignment) {
        super(borderColor, spacing, alignment);
    }

    public ElementHorizontal(@Nonnull ByteBuf buf) {
        super(buf);
    }

    @Override
    public void render(int x, int y) {
        super.render(x, y);
        if (this.borderColor != null) {
            x += 3;
            y += 3;
        }
        int totHeight = getHeight();
        for (IElement element : this.children) {
            final int h = element.getHeight();
            int cy = y;
            switch (alignment) {
                case ALIGN_TOPLEFT:
                    break;
                case ALIGN_CENTER:
                    cy = y + (totHeight - h) / 2;
                    break;
                case ALIGN_BOTTOMRIGHT:
                    cy = y + totHeight - h;
                    break;
            }
            element.render(x, cy);
            x += element.getWidth() + spacing;
        }
    }

    private int getBorderSpacing() {
        return borderColor == null ? 0 : 6;
    }

    @Override
    public int getWidth() {
        int w = 0;
        for (IElement element : this.children) {
            w += element.getWidth();
        }
        return w + this.spacing * (this.children.size() - 1) + getBorderSpacing();
    }

    @Override
    public int getHeight() {
        int h = 0;
        for (IElement element : this.children) {
            int height = element.getHeight();
            if (height > h) {
                h = height;
            }
        }
        return h + getBorderSpacing();
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_HORIZONTAL;
    }
}

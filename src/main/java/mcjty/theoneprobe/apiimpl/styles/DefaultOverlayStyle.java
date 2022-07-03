package mcjty.theoneprobe.apiimpl.styles;

import mcjty.theoneprobe.api.IOverlayStyle;

import javax.annotation.Nonnull;

public class DefaultOverlayStyle implements IOverlayStyle {

    private int borderThickness;
    private int borderColorTop;
    private int borderColorBottom;
    private int boxColor;
    private int borderOffset;
    private int leftX;
    private int rightX;
    private int topY;
    private int bottomY;

    @Nonnull
    public IOverlayStyle copy() {
        return new DefaultOverlayStyle()
                .borderThickness(borderThickness)
                .borderColorTop(borderColorTop)
                .borderColorBottom(borderColorBottom)
                .boxColor(boxColor)
                .borderOffset(borderOffset)
                .location(leftX, rightX, topY, bottomY);
    }

    @Override
    public IOverlayStyle borderOffset(int offset) {
        this.borderOffset = offset;
        return this;
    }

    @Override
    public int getBorderOffset() {
        return borderOffset;
    }

    @Override
    public IOverlayStyle borderThickness(int thick) {
        this.borderThickness = thick;
        return this;
    }

    @Override
    public int getBorderThickness() {
        return borderThickness;
    }

    @Override
    public IOverlayStyle borderColorTop(int color) {
        this.borderColorTop = color;
        return this;
    }

    @Override
    public IOverlayStyle borderColorBottom(int color) {
        this.borderColorBottom = color;
        return this;
    }

    @Override
    public int getBorderColorTop() {
        return borderColorTop;
    }

    @Override
    public int getBorderColorBottom() {
        return borderColorBottom;
    }

    @Override
    public IOverlayStyle boxColor(int color) {
        this.boxColor = color;
        return this;
    }

    @Override
    public int getBoxColor() {
        return boxColor;
    }

    @Override
    public IOverlayStyle location(int leftX, int rightX, int topY, int bottomY) {
        this.leftX = leftX;
        this.rightX = rightX;
        this.topY = topY;
        this.bottomY = bottomY;
        return this;
    }

    @Override
    public int getLeftX() {
        return leftX;
    }

    @Override
    public int getRightX() {
        return rightX;
    }

    @Override
    public int getTopY() {
        return topY;
    }

    @Override
    public int getBottomY() {
        return bottomY;
    }
}

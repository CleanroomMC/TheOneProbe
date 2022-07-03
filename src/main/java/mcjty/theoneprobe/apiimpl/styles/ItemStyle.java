package mcjty.theoneprobe.apiimpl.styles;

import mcjty.theoneprobe.api.IItemStyle;

public class ItemStyle implements IItemStyle {

    private int width = 20;
    private int height = 20;

    @Override
    public IItemStyle width(int w) {
        this.width = w;
        return this;
    }

    @Override
    public IItemStyle height(int h) {
        this.height = h;
        return this;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}

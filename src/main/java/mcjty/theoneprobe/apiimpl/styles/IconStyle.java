package mcjty.theoneprobe.apiimpl.styles;

import mcjty.theoneprobe.api.IIconStyle;

public class IconStyle implements IIconStyle {

    private int width = 16;
    private int height = 16;
    private int txtw = 256;
    private int txth = 256;

    @Override
    public IIconStyle width(int w) {
        this.width = w;
        return this;
    }

    @Override
    public IIconStyle height(int h) {
        this.height = h;
        return this;
    }

    @Override
    public IIconStyle textureWidth(int w) {
        this.txtw = w;
        return this;
    }

    @Override
    public IIconStyle textureHeight(int h) {
        this.txth = h;
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

    @Override
    public int getTextureWidth() {
        return this.txtw;
    }

    @Override
    public int getTextureHeight() {
        return this.txth;
    }
}

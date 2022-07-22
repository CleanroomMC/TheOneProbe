package mcjty.theoneprobe.apiimpl.styles;

import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;

import javax.annotation.Nonnull;

/**
 * Style for the progress bar.
 */
public class ProgressStyle implements IProgressStyle {

    private int borderColorTop = 0xFF5000FF;
    private int borderColorBottom = 0xFF28007F;
    private int backgroundColor = 0xFF000000;
    private int filledColor = 0xFFAAAAAA;
    private int alternateFilledColor = 0xFFAAAAAA;
    private boolean showText = true;
    private boolean renderBG = true;
    private String prefix = "";
    private String suffix = "";
    private int width = 100;
    private int height = 14;
    private boolean lifeBar = false;
    private boolean armorBar = false;
    private boolean arrowBar = false;

    private NumberFormat numberFormat = NumberFormat.FULL;


    /**
     * Backwards compatibility; sets top and bottom border colors to same color
     */
    @Override
    public ProgressStyle borderColor(int c) {
        this.borderColorBottom = c;
        this.borderColorTop = c;
        return this;
    }

    /**
     * The color that is used for the border of the progress bar
     */
    @Override
    public ProgressStyle borderColorTop(int c) {
        this.borderColorTop = c;
        return this;
    }

    @Override
    public ProgressStyle borderColorBottom(int c) {
        this.borderColorBottom = c;
        return this;
    }

    /**
     * The color that is used for the background of the progress bar
     */
    @Override
    public ProgressStyle backgroundColor(int c) {
        this.backgroundColor = c;
        return this;
    }

    /**
     * The color that is used for the filled part of the progress bar
     */
    @Override
    public ProgressStyle filledColor(int c) {
        this.filledColor = c;
        return this;
    }

    /**
     * If this is different from the filledColor then the fill color will alternate
     */
    @Override
    public ProgressStyle alternateFilledColor(int c) {
        this.alternateFilledColor = c;
        return this;
    }

    /**
     * If true then text is shown inside the progress bar
     */
    @Override
    public ProgressStyle showText(boolean b) {
        this.showText = b;
        return this;
    }

    /**
     * If true then background will be rendered
     */
    @Override
    public ProgressStyle renderBG(boolean b) {
        this.renderBG = b;
        return this;
    }

    /**
     * The number format to use for the text inside the progress bar
     */
    @Override
    public ProgressStyle numberFormat(@Nonnull NumberFormat f) {
        this.numberFormat = f;
        return this;
    }

    @Override
    public ProgressStyle prefix(@Nonnull String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public ProgressStyle suffix(@Nonnull String suffix) {
        this.suffix = suffix;
        return this;
    }

    @Override
    public ProgressStyle width(int w) {
        this.width = w;
        return this;
    }

    @Override
    public ProgressStyle height(int h) {
        this.height = h;
        return this;
    }

    @Override
    public IProgressStyle lifeBar(boolean b) {
        this.lifeBar = b;
        return this;
    }

    @Override
    public IProgressStyle armorBar(boolean b) {
        this.armorBar = b;
        return this;
    }

    @Override
    public IProgressStyle arrowBar(boolean b) {
        this.arrowBar = b;
        return this;
    }

    @Override
    public int getBorderColorTop() {
        return this.borderColorTop;
    }

    public int getBorderColorBottom() {
        return this.borderColorBottom;
    }

    @Override
    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    @Override
    public int getFilledColor() {
        return this.filledColor;
    }

    @Override
    public int getAlternateFilledColor() {
        return this.alternateFilledColor;
    }

    @Override
    public boolean isShowText() {
        return this.showText;
    }

    @Override
    public boolean isRenderBG() {
        return this.renderBG;
    }

    @Nonnull
    @Override
    public NumberFormat getNumberFormat() {
        return this.numberFormat;
    }

    @Nonnull
    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Nonnull
    @Override
    public String getSuffix() {
        return this.suffix;
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
    public boolean isLifeBar() {
        return this.lifeBar;
    }

    @Override
    public boolean isArmorBar() {
        return this.armorBar;
    }

    @Override
    public boolean isArrowBar() {
        return this.arrowBar;
    }
}

package mcjty.theoneprobe.api;

import javax.annotation.Nonnull;

/**
 * Style for the progress bar.
 */
public interface IProgressStyle {

    /**
     * The color that is used for the border of the progress bar
     */
    IProgressStyle borderColorTop(int c);

    /**
     * The color that is used for the border of the progress bar
     */
    IProgressStyle borderColorBottom(int c);

    /**
     * The color that is used for the background of the progress bar
     */
    IProgressStyle backgroundColor(int c);

    /**
     * The color that is used for the filled part of the progress bar
     */
    IProgressStyle filledColor(int c);

    /**
     * If this is different from the filledColor then the fill color will alternate
     */
    IProgressStyle alternateFilledColor(int c);

    /**
     * If true then text is shown inside the progress bar
     */
    IProgressStyle showText(boolean b);

    /**
     * If true then background is rendered
     */
    IProgressStyle renderBG(boolean b);

    /**
     * The number format to use for the text inside the progress bar
     */
    IProgressStyle numberFormat(@Nonnull NumberFormat f);

    /**
     * The prefix to the value displayed
     */
    IProgressStyle prefix(@Nonnull String prefix);

    /**
     * The suffix to the value displayed
     */
    IProgressStyle suffix(@Nonnull String suffix);

    /**
     * If the progressbar is a health bar, then this is the maximum width
     */
    IProgressStyle width(int w);

    IProgressStyle height(int h);

    IProgressStyle lifeBar(boolean b);

    IProgressStyle armorBar(boolean b);

    IProgressStyle arrowBar(boolean b);

    int getBorderColorTop();

    int getBorderColorBottom();

    int getBackgroundColor();

    int getFilledColor();

    /**
     * Use {@link IProgressStyle#getAlternateFilledColor()} instead
     */
    @Deprecated()
    default int getAlternatefilledColor() {
        return 0;
    }

    default int getAlternateFilledColor() {
        return getAlternatefilledColor();
    }

    boolean isShowText();

    boolean isRenderBG();

    @Nonnull
    NumberFormat getNumberFormat();

    @Nonnull
    String getPrefix();

    @Nonnull
    String getSuffix();

    int getWidth();

    int getHeight();

    boolean isLifeBar();

    boolean isArmorBar();

    boolean isArrowBar();
}

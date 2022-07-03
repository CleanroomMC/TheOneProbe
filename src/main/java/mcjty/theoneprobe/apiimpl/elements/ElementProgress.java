package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import mcjty.theoneprobe.network.NetworkTools;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;

public class ElementProgress implements IElement {

    private final long current;
    private final long max;
    private final IProgressStyle style;

    public ElementProgress(long current, long max, @Nonnull IProgressStyle style) {
        this.current = current;
        this.max = max;
        this.style = style;
    }

    public ElementProgress(@Nonnull ByteBuf buf) {
        this.current = buf.readLong();
        this.max = buf.readLong();
        //noinspection ConstantConditions
        this.style = new ProgressStyle()
                .width(buf.readInt())
                .height(buf.readInt())
                .prefix(NetworkTools.readStringUTF8(buf))
                .suffix(NetworkTools.readStringUTF8(buf))
                .borderColorTop(buf.readInt())
                .borderColorBottom(buf.readInt())
                .filledColor(buf.readInt())
                .alternateFilledColor(buf.readInt())
                .backgroundColor(buf.readInt())
                .showText(buf.readBoolean())
                .renderBG(buf.readBoolean())
                .numberFormat(NumberFormat.values()[buf.readByte()])
                .lifeBar(buf.readBoolean())
                .armorBar(buf.readBoolean())
                .arrowBar(buf.readBoolean());
    }

    private static final DecimalFormat FORMAT_COMMAS = new DecimalFormat("###,###");

    /**
     * If the suffix starts with 'm' we can possibly drop that
     */
    public static String format(long in, @Nonnull NumberFormat style, @Nonnull String suffix) {
        switch (style) {
            case FULL:
                return in + suffix;
            case COMPACT: {
                int unit = 1000;
                if (in < unit) return in + suffix;
                int exp = (int) (Math.log(in) / Math.log(unit));
                char pre;
                if (suffix.startsWith("m")) {
                    suffix = suffix.substring(1);
                    if (exp - 2 >= 0) {
                        pre = "kMBTPE".charAt(exp - 2);
                        return String.format("%.1f%s", in / Math.pow(unit, exp), pre) + suffix;
                    } else {
                        return String.format("%.1f%s", in / Math.pow(unit, exp), suffix);
                    }
                } else {
                    pre = "kMBTPE".charAt(exp - 1);
                    return String.format("%.1f%s", in / Math.pow(unit, exp), pre) + suffix;
                }
            }
            case COMMAS:
                return FORMAT_COMMAS.format(in) + suffix;
            case NONE:
                return suffix;
        }
        return Long.toString(in);
    }

    @Override
    public void render(int x, int y) {
        ElementProgressRender.render(this.style, this.current, this.max, x, y, getWidth(), getHeight());
    }

    @Override
    public int getWidth() {
        if (this.style.isLifeBar()) {
            if (current * 4 >= this.style.getWidth()) return 100;
            else return (int) (this.current * 4 + 2);
        }
        return style.getWidth();
    }

    @Override
    public int getHeight() {
        return this.style.getHeight();
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        buf.writeLong(this.current);
        buf.writeLong(this.max);
        buf.writeInt(this.style.getWidth());
        buf.writeInt(this.style.getHeight());
        NetworkTools.writeStringUTF8(buf, this.style.getPrefix());
        NetworkTools.writeStringUTF8(buf, this.style.getSuffix());
        buf.writeInt(this.style.getBorderColorTop());
        buf.writeInt(this.style.getBorderColorBottom());
        buf.writeInt(this.style.getFilledColor());
        buf.writeInt(this.style.getAlternateFilledColor());
        buf.writeInt(this.style.getBackgroundColor());
        buf.writeBoolean(this.style.isShowText());
        buf.writeBoolean(this.style.isRenderBG());
        buf.writeByte(this.style.getNumberFormat().ordinal());
        buf.writeBoolean(this.style.isLifeBar());
        buf.writeBoolean(this.style.isArmorBar());
        buf.writeBoolean(this.style.isArrowBar());
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_PROGRESS;
    }
}

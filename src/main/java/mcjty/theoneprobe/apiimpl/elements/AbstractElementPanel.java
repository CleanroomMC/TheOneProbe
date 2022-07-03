package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.apiimpl.styles.*;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractElementPanel implements IElement, IProbeInfo {

    protected List<IElement> children = new ObjectArrayList<>();
    protected Integer borderColor;
    protected int spacing;
    protected ElementAlignment alignment;
    protected IProbeConfig overriddenConfig;

    @Override
    public void render(int x, int y) {
        if (this.borderColor != null) {
            RenderHelper.drawThickBeveledBoxGradient(x, y, x + getWidth() -2, y + getHeight() -2, 1, RenderHelper.renderColorToHSB(borderColor, 0.8f, 1.0f), borderColor, 0x00ffffff);
        }
    }

    public AbstractElementPanel(@Nullable Integer borderColor, int spacing, @Nonnull ElementAlignment alignment) {
        this.borderColor = borderColor;
        this.spacing = spacing;
        this.alignment = alignment;
    }

    public AbstractElementPanel(@Nonnull ByteBuf buf) {
        this.children = ProbeInfo.createElements(buf);
        if (buf.readBoolean()) {
            borderColor = buf.readInt();
        }
        this.spacing = buf.readShort();
        this.alignment = ElementAlignment.values()[buf.readShort()];
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        ProbeInfo.writeElements(children, buf);
        if (this.borderColor != null) {
            buf.writeBoolean(true);
            buf.writeInt(this.borderColor);
        } else {
            buf.writeBoolean(false);
        }
        buf.writeShort(this.spacing);
        buf.writeShort(this.alignment.ordinal());
    }

    @Override
    public IProbeInfo icon(@Nonnull ResourceLocation icon, int u, int v, int w, int h) {
        return icon(icon, u, v, w, h, new IconStyle());
    }

    @Override
    public IProbeInfo icon(@Nonnull ResourceLocation icon, int u, int v, int w, int h, @Nonnull IIconStyle style) {
        children.add(new ElementIcon(icon, u, v, w, h, style));
        return this;
    }

    @Override
    public IProbeInfo text(@Nonnull String text) {
        children.add(new ElementText(text));
        return this;
    }

    @Override
    public IProbeInfo text(@Nonnull String text, @Nonnull ITextStyle style) {
        children.add(new ElementText(text));
        return this;
    }

    @Override
    public IProbeInfo textSmall(@Nonnull String text) {
        children.add(new ElementTextSmall(text));
        return this;
    }

    @Override
    public IProbeInfo textSmall(@Nonnull String text, @Nonnull ITextStyle style) {
        children.add(new ElementTextSmall(text));
        return this;
    }

    @Override
    public IProbeInfo itemLabel(@Nonnull ItemStack stack, @Nonnull ITextStyle style) {
        children.add(new ElementItemLabel(stack));
        return this;
    }

    @Override
    public IProbeInfo itemLabel(@Nonnull ItemStack stack) {
        children.add(new ElementItemLabel(stack));
        return this;
    }

    @Override
    public IProbeInfo entity(@Nonnull String entityName, @Nonnull IEntityStyle style) {
        children.add(new ElementEntity(entityName, style));
        return this;
    }

    @Override
    public IProbeInfo entity(@Nonnull String entityName) {
        return entity(entityName, new EntityStyle());
    }

    @Override
    public IProbeInfo entity(@Nonnull Entity entity, @Nonnull IEntityStyle style) {
        children.add(new ElementEntity(entity, style));
        return this;
    }

    @Override
    public IProbeInfo entity(@Nonnull Entity entity) {
        return entity(entity, new EntityStyle());
    }

    @Override
    public IProbeInfo item(@Nonnull ItemStack stack, @Nonnull IItemStyle style) {
        children.add(new ElementItemStack(stack, style));
        return this;
    }

    @Override
    public IProbeInfo item(@Nonnull ItemStack stack) {
        return item(stack, new ItemStyle());
    }

    @Override
    public IProbeInfo progress(int current, int max) {
        return progress(current, max, new ProgressStyle());
    }

    @Override
    public IProbeInfo progress(int current, int max, @Nonnull IProgressStyle style) {
        children.add(new ElementProgress(current, max, style));
        return this;
    }

    @Override
    public IProbeInfo progress(long current, long max) {
        return progress(current, max, new ProgressStyle());
    }

    @Override
    public IProbeInfo progress(long current, long max, @Nonnull IProgressStyle style) {
        children.add(new ElementProgress(current, max, style));
        return this;
    }

    @Override
    public IProbeInfo horizontal(@Nonnull ILayoutStyle style) {
        ElementHorizontal e = new ElementHorizontal(style.getBorderColor(), style.getSpacing(), style.getAlignment());
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo horizontal() {
        ElementHorizontal e = new ElementHorizontal(null, spacing, ElementAlignment.ALIGN_TOPLEFT);
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo vertical(@Nonnull ILayoutStyle style) {
        ElementVertical e = new ElementVertical(style.getBorderColor(), style.getSpacing(), style.getAlignment());
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo vertical() {
        ElementVertical e = new ElementVertical(null, ElementVertical.SPACING, ElementAlignment.ALIGN_TOPLEFT);
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo element(@Nonnull IElement element) {
        children.add(element);
        return this;
    }

    @Override
    public ILayoutStyle defaultLayoutStyle() {
        return new LayoutStyle();
    }

    @Override
    public IProgressStyle defaultProgressStyle() {
        return new ProgressStyle();
    }

    @Override
    public ITextStyle defaultTextStyle() {
        return new TextStyle();
    }

    @Override
    public IItemStyle defaultItemStyle() {
        return new ItemStyle();
    }

    @Override
    public IEntityStyle defaultEntityStyle() {
        return new EntityStyle();
    }

    @Override
    public IIconStyle defaultIconStyle() {
        return new IconStyle();
    }
}

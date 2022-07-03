package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementIconRender;
import mcjty.theoneprobe.apiimpl.styles.IconStyle;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ElementIcon implements IElement {

    private final ResourceLocation icon;
    private final int u;
    private final int v;
    private final int w;
    private final int h;
    private final IIconStyle style;

    public ElementIcon(@Nonnull ResourceLocation icon, int u, int v, int w, int h, IIconStyle style) {
        this.icon = icon;
        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
        this.style = style;
    }

    public ElementIcon(@Nonnull ByteBuf buf) {
        //noinspection ConstantConditions
        this.icon = new ResourceLocation(NetworkTools.readString(buf), NetworkTools.readString(buf));
        this.u = buf.readInt();
        this.v = buf.readInt();
        this.w = buf.readInt();
        this.h = buf.readInt();
        this.style = new IconStyle()
                .width(buf.readInt())
                .height(buf.readInt())
                .textureWidth(buf.readInt())
                .textureHeight(buf.readInt());
    }

    @Override
    public void render(int x, int y) {
        ElementIconRender.render(icon, x, y, w, h, u, v, style.getTextureWidth(), style.getTextureHeight());
    }

    @Override
    public int getWidth() {
        return style.getWidth();
    }

    @Override
    public int getHeight() {
        return style.getHeight();
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        NetworkTools.writeString(buf, this.icon.getNamespace());
        NetworkTools.writeString(buf, this.icon.getPath());
        buf.writeInt(this.u);
        buf.writeInt(this.v);
        buf.writeInt(this.w);
        buf.writeInt(this.h);
        buf.writeInt(this.style.getWidth());
        buf.writeInt(this.style.getHeight());
        buf.writeInt(this.style.getTextureWidth());
        buf.writeInt(this.style.getTextureHeight());
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_ICON;
    }
}

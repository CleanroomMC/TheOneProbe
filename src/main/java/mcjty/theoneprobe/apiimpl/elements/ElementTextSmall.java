package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementTextSmallRender;
import mcjty.theoneprobe.network.NetworkTools;

import javax.annotation.Nonnull;

public class ElementTextSmall implements IElement {

    private final String text;

    public ElementTextSmall(@Nonnull String text) {
        this.text = text;
    }

    public ElementTextSmall(@Nonnull ByteBuf buf) {
        this.text = NetworkTools.readStringUTF8(buf);
    }

    @Override
    public void render(int x, int y) {
        ElementTextSmallRender.render(this.text, x, y);
    }

    @Override
    public int getWidth() {
        return ElementTextSmallRender.getWidth(this.text);
    }

    @Override
    public int getHeight() {
        return 5;
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        NetworkTools.writeStringUTF8(buf, this.text);
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_TEXT_SMALL;
    }
}

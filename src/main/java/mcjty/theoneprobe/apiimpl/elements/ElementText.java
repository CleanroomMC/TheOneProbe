package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;

import javax.annotation.Nonnull;

public class ElementText implements IElement {

    private final String text;

    public ElementText(@Nonnull String text) {
        this.text = text;
    }

    public ElementText(@Nonnull ByteBuf buf) {
        text = NetworkTools.readStringUTF8(buf);
    }

    @Override
    public void render(int x, int y) {
        ElementTextRender.render(this.text, x, y);
    }

    @Override
    public int getWidth() {
        return ElementTextRender.getWidth(this.text);
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        NetworkTools.writeStringUTF8(buf, this.text);
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_TEXT;
    }
}

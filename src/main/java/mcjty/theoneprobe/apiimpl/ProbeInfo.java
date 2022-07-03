package mcjty.theoneprobe.apiimpl;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.apiimpl.elements.ElementVertical;

import javax.annotation.Nonnull;
import java.util.List;

public class ProbeInfo extends ElementVertical {

    @Nonnull
    public List<IElement> getElements() {
        return this.children;
    }

    public void fromBytes(@Nonnull ByteBuf buf) {
        this.children = createElements(buf);
    }

    public ProbeInfo() {
        super(null, 2, ElementAlignment.ALIGN_TOPLEFT);
    }

    @Nonnull
    public static List<IElement> createElements(@Nonnull ByteBuf buf) {
        int size = buf.readShort();
        List<IElement> elements = new ObjectArrayList<>(size);
        for (int i = 0; i < size; i++) {
            IElementFactory factory = TheOneProbe.theOneProbeImp.getElementFactory(buf.readInt());
            if (factory != null) elements.add(factory.createElement(buf));
        }
        return elements;
    }

    public static void writeElements(@Nonnull List<IElement> elements, @Nonnull ByteBuf buf) {
        buf.writeShort(elements.size());
        for (IElement element : elements) {
            buf.writeInt(element.getID());
            element.toBytes(buf);
        }
    }

    public void removeElement(@Nonnull IElement element) {
        this.getElements().remove(element);
    }
}

package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementItemStackRender;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ElementItemStack implements IElement {

    private final ItemStack itemStack;
    private final IItemStyle style;

    public ElementItemStack(@Nonnull ItemStack itemStack, @Nonnull IItemStyle style) {
        this.itemStack = itemStack;
        this.style = style;
    }

    public ElementItemStack(@Nonnull ByteBuf buf) {
        if (buf.readBoolean()) this.itemStack = NetworkTools.readItemStack(buf);
        else this.itemStack = ItemStack.EMPTY;
        this.style = new ItemStyle()
                .width(buf.readInt())
                .height(buf.readInt());
    }

    @Override
    public void render(int x, int y) {
        ElementItemStackRender.render(this.itemStack, this.style, x, y);
    }

    @Override
    public int getWidth() {
        return this.style.getWidth();
    }

    @Override
    public int getHeight() {
        return this.style.getHeight();
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        if (!this.itemStack.isEmpty()) {
            buf.writeBoolean(true);
            NetworkTools.writeItemStack(buf, this.itemStack);
        } else buf.writeBoolean(false);
        buf.writeInt(this.style.getWidth());
        buf.writeInt(this.style.getHeight());
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_ITEM;
    }
}

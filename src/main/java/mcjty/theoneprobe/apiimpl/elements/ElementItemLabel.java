package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ElementItemLabel implements IElement {

    private final ItemStack itemStack;

    public ElementItemLabel(@Nonnull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ElementItemLabel(@Nonnull ByteBuf buf) {
        if (buf.readBoolean()) this.itemStack = NetworkTools.readItemStack(buf);
        else this.itemStack = ItemStack.EMPTY;
    }

    @Override
    public void render(int x, int y) {
        if (!this.itemStack.isEmpty()) {
            ElementTextRender.render(this.itemStack.getDisplayName(), x, y);
        }
    }

    @Override
    public int getWidth() {
        if (!this.itemStack.isEmpty()) {
            return ElementTextRender.getWidth(this.itemStack.getDisplayName());
        } else {
            return 10;
        }
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        if (!this.itemStack.isEmpty()) {
            buf.writeBoolean(true);
            NetworkTools.writeItemStack(buf, this.itemStack);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_ITEMLABEL;
    }
}

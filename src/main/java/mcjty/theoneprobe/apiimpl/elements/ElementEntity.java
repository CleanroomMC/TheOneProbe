package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementEntityRender;
import mcjty.theoneprobe.apiimpl.styles.EntityStyle;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class ElementEntity implements IElement {

    private final String entityName;
    private final Integer playerID;
    private final NBTTagCompound entityNBT;
    private final IEntityStyle style;

    public ElementEntity(@Nonnull String entityName, @Nonnull IEntityStyle style) {
        this.entityName = entityName;
        this.entityNBT = null;
        this.style = style;
        this.playerID = null;
    }

    public ElementEntity(@Nonnull Entity entity, @Nonnull IEntityStyle style) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            this.entityNBT = null;
            this.playerID = player.getEntityId();
        } else {
            this.entityNBT = entity.serializeNBT();
            this.playerID = null;
        }
        this.entityName = EntityList.getEntityString(entity);
        this.style = style;
    }

    public ElementEntity(@Nonnull ByteBuf buf) {
        this.entityName = NetworkTools.readString(buf);
        this.style = new EntityStyle()
                .width(buf.readInt())
                .height(buf.readInt())
                .scale(buf.readFloat());
        if (buf.readBoolean()) {
            this.entityNBT = NetworkTools.readNBT(buf);
        } else {
            this.entityNBT = null;
        }
        if (buf.readBoolean()) {
            this.playerID = buf.readInt();
        } else {
            this.playerID = null;
        }
    }

    @Override
    public void render(int x, int y) {
        if (playerID != null) {
            ElementEntityRender.renderPlayer(entityName, playerID, style, x, y);
        } else {
            ElementEntityRender.render(entityName, entityNBT, style, x, y);
        }
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
        NetworkTools.writeString(buf, entityName);
        buf.writeInt(this.style.getWidth());
        buf.writeInt(this.style.getHeight());
        buf.writeFloat(this.style.getScale());
        if (this.entityNBT != null) {
            buf.writeBoolean(true);
            NetworkTools.writeNBT(buf, entityNBT);
        } else {
            buf.writeBoolean(false);
        }
        if (this.playerID != null) {
            buf.writeBoolean(true);
            buf.writeInt(this.playerID);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_ENTITY;
    }
}

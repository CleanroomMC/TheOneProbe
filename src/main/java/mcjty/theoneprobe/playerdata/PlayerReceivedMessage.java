package mcjty.theoneprobe.playerdata;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class PlayerReceivedMessage {

    private boolean receivedMessage = false;

    public void copyFrom(@Nonnull PlayerReceivedMessage source) {
        this.receivedMessage = source.receivedMessage;
    }

    public boolean wasMessageReceived() {
        return receivedMessage;
    }

    public void setPlayerReceivedMessage(boolean receivedMessage) {
        this.receivedMessage = receivedMessage;
    }

    public void saveNBTData(@Nonnull NBTTagCompound compound) {
        compound.setBoolean("receivedMessage", receivedMessage);
    }

    public void loadNBTData(@Nonnull NBTTagCompound compound) {
        receivedMessage = compound.getBoolean("receivedMessage");
    }
}

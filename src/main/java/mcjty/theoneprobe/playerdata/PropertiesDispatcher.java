package mcjty.theoneprobe.playerdata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public class PropertiesDispatcher implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

    private final PlayerReceivedMessage playerReceivedMessage;

    public PropertiesDispatcher() {
        this.playerReceivedMessage = new PlayerReceivedMessage();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == PlayerProperties.PLAYER_GOT_NOTE;
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == PlayerProperties.PLAYER_GOT_NOTE) {
            //noinspection unchecked
            return (T) playerReceivedMessage;
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        playerReceivedMessage.saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        playerReceivedMessage.loadNBTData(nbt);
    }
}

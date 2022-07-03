package mcjty.theoneprobe.playerdata;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerProperties {

    @CapabilityInject(PlayerReceivedMessage.class)
    public static Capability<PlayerReceivedMessage> PLAYER_GOT_NOTE;

    @Nullable
    public static PlayerReceivedMessage didPlayerReceiveMessage(@Nonnull EntityPlayer player) {
        return player.getCapability(PLAYER_GOT_NOTE, null);
    }
}

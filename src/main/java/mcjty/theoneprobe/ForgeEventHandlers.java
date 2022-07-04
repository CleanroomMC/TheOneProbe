package mcjty.theoneprobe;

import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.playerdata.PlayerProperties;
import mcjty.theoneprobe.playerdata.PlayerReceivedMessage;
import mcjty.theoneprobe.playerdata.PropertiesDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        ConfigSetup.setupStyleConfig(ConfigSetup.mainConfig);
        ConfigSetup.updateDefaultOverlayStyle();

        if (ConfigSetup.mainConfig.hasChanged()) {
            ConfigSetup.mainConfig.save();
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(@Nonnull AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            if (!event.getObject().hasCapability(PlayerProperties.PLAYER_GOT_NOTE, null)) {
                event.addCapability(new ResourceLocation(TheOneProbe.MODID, "Properties"), new PropertiesDispatcher());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(@Nonnull PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // We need to copyFrom the capabilities
            if (event.getOriginal().hasCapability(PlayerProperties.PLAYER_GOT_NOTE, null)) {
                PlayerReceivedMessage oldStore = event.getOriginal().getCapability(PlayerProperties.PLAYER_GOT_NOTE, null);
                if (oldStore == null) return;
                PlayerReceivedMessage newStore = PlayerProperties.didPlayerReceiveMessage(event.getEntityPlayer());
                if (newStore == null) return;
                newStore.copyFrom(oldStore);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        if (ConfigSetup.firstJoinMessage) {
            PlayerReceivedMessage received = PlayerProperties.didPlayerReceiveMessage(event.player);
            if (received != null && !received.wasMessageReceived()) {
                event.player.sendMessage(new TextComponentTranslation("theoneprobe.message.configure_top"));
                received.setPlayerReceivedMessage(true);
            }
        }
    }
}

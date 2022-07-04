package mcjty.theoneprobe.keys;

import mcjty.theoneprobe.config.ConfigSetup;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.toggleVisible.isPressed() && !ConfigSetup.holdKeyToMakeVisible) {
                ConfigSetup.setVisible(!ConfigSetup.isVisible);
        }
    }
}

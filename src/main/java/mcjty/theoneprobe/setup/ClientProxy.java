package mcjty.theoneprobe.setup;

import mcjty.theoneprobe.ClientForgeEventHandlers;
import mcjty.theoneprobe.commands.CommandTopCfg;
import mcjty.theoneprobe.keys.KeyBindings;
import mcjty.theoneprobe.keys.KeyInputHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class ClientProxy implements IProxy {

    @Override
    public void preInit(@Nonnull FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandlers());
    }

    @Override
    public void init(@Nonnull FMLInitializationEvent e) {
        ClientCommandHandler.instance.registerCommand(new CommandTopCfg());
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        KeyBindings.init();
    }

    @Override
    public void postInit(@Nonnull FMLPostInitializationEvent e) {
    }
}

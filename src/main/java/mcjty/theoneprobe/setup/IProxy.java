package mcjty.theoneprobe.setup;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

public interface IProxy {

    void preInit(@Nonnull FMLPreInitializationEvent e);

    void init(@Nonnull FMLInitializationEvent e);

    void postInit(@Nonnull FMLPostInitializationEvent e);
}

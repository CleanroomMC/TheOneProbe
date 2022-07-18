package mcjty.theoneprobe.setup;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mcjty.theoneprobe.ForgeEventHandlers;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.providers.*;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.playerdata.PlayerReceivedMessage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Collections;
import java.util.Set;

public class ModSetup {

    private Logger logger;
    public static File modConfigDir;

    public static boolean tesla = false;
    public static boolean redstoneflux = false;

    public void preInit(@Nonnull FMLPreInitializationEvent e) {
        logger = e.getModLog();

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

        registerCapabilities();
        TheOneProbeImp.registerElements();
        TheOneProbe.theOneProbeImp.registerProvider(new DefaultProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerProvider(new DebugProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerProvider(new BlockProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DefaultProbeInfoEntityProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DebugProbeInfoEntityProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new EntityProbeInfoEntityProvider());

        modConfigDir = e.getModConfigurationDirectory();
        ConfigSetup.init();

        setupModCompat();
    }

    @Nonnull
    public Logger getLogger() {
        return logger;
    }

    private void setupModCompat() {
        tesla = Loader.isModLoaded("tesla");
        if (tesla) {
            logger.log(Level.INFO, "The One Probe Detected TESLA: enabling support");
        }

        redstoneflux = Loader.isModLoaded("redstoneflux");
        if (redstoneflux) {
            logger.log(Level.INFO, "The One Probe Detected RedstoneFlux: enabling support");
        }
    }

    private static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(PlayerReceivedMessage.class, new Capability.IStorage<PlayerReceivedMessage>() {

            @Nullable
            @Override
            public NBTBase writeNBT(Capability<PlayerReceivedMessage> capability, PlayerReceivedMessage instance, EnumFacing side) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void readNBT(Capability<PlayerReceivedMessage> capability, PlayerReceivedMessage instance, EnumFacing side, NBTBase nbt) {
                throw new UnsupportedOperationException();
            }

        }, () -> {
            throw new UnsupportedOperationException();
        });
    }

    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(TheOneProbe.instance, new GuiProxy());
    }

    @SuppressWarnings("unused")
    public void postInit(FMLPostInitializationEvent e) {
        configureProviders();
        configureEntityProviders();

        if (ConfigSetup.mainConfig.hasChanged()) {
            ConfigSetup.mainConfig.save();
        }
    }

    private void configureProviders() {
        String[] defaultValues = TheOneProbe.theOneProbeImp.getProviders().stream()
                .map(IProbeInfoProvider::getID)
                .toArray(String[]::new);

        String[] sortedProviders = ConfigSetup.mainConfig.getStringList("sortedProviders", ConfigSetup.CATEGORY_PROVIDERS, defaultValues, "Order in which providers should be used");
        Set<String> excluded = new ObjectOpenHashSet<>();
        Collections.addAll(excluded, ConfigSetup.mainConfig.getStringList("excludedProviders", ConfigSetup.CATEGORY_PROVIDERS, new String[]{}, "Providers that should be excluded"));

        TheOneProbe.theOneProbeImp.configureProviders(sortedProviders, excluded);
    }

    private void configureEntityProviders() {
        String[] defaultValues = TheOneProbe.theOneProbeImp.getEntityProviders().stream()
                .map(IProbeInfoEntityProvider::getID)
                .toArray(String[]::new);

        String[] sortedProviders = ConfigSetup.mainConfig.getStringList("sortedEntityProviders", ConfigSetup.CATEGORY_PROVIDERS, defaultValues, "Order in which entity providers should be used");
        Set<String> excluded = new ObjectOpenHashSet<>();
        Collections.addAll(excluded, ConfigSetup.mainConfig.getStringList("excludedEntityProviders", ConfigSetup.CATEGORY_PROVIDERS, new String[]{}, "Entity providers that should be excluded"));

        TheOneProbe.theOneProbeImp.configureEntityProviders(sortedProviders, excluded);
    }
}

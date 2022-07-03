package mcjty.theoneprobe;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Map;

import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.EXTENDED;
import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.NORMAL;

public class Tools {

    private static final String MINECRAFT = "Minecraft";

    private final static Map<String, String> modNamesForIds = new Object2ObjectOpenHashMap<>();

    private static void init() {
        Map<String, ModContainer> modMap = Loader.instance().getIndexedModList();
        for (Map.Entry<String, ModContainer> modEntry : modMap.entrySet()) {
            final String lowercaseId = modEntry.getKey().toLowerCase(Locale.ENGLISH);
            final String modName = modEntry.getValue().getName();
            modNamesForIds.put(lowercaseId, modName);
        }
    }

    @Nonnull
    public static String getModName(@Nonnull Block block) {
        if (modNamesForIds.isEmpty()) init();
        ResourceLocation resourceLocation = block.getRegistryName();
        if (resourceLocation == null) return MINECRAFT;

        final String modId = resourceLocation.getNamespace();
        final String lowercaseModId = modId.toLowerCase(Locale.ENGLISH);

        String modName = modNamesForIds.get(lowercaseModId);
        if (modName == null) {
            modName = WordUtils.capitalize(modId);
            modNamesForIds.put(lowercaseModId, modName);
        }
        return modName;
    }

    public static String getModName(Entity entity) {
        if (modNamesForIds.isEmpty()) init();

        EntityRegistry.EntityRegistration modSpawn = EntityRegistry.instance().lookupModSpawn(entity.getClass(), true);
        if (modSpawn == null) return MINECRAFT;

        ModContainer container = modSpawn.getContainer();
        if (container == null) return MINECRAFT;

        final String modId = container.getModId();
        final String lowercaseModId = modId.toLowerCase(Locale.ENGLISH);

        String modName = modNamesForIds.get(lowercaseModId);
        if (modName == null) {
            modName = WordUtils.capitalize(modId);
            modNamesForIds.put(lowercaseModId, modName);
        }
        return modName;
    }

    public static boolean show(ProbeMode mode, IProbeConfig.ConfigMode cfg) {
        return cfg == NORMAL || (cfg == EXTENDED && mode == ProbeMode.EXTENDED);
    }
}

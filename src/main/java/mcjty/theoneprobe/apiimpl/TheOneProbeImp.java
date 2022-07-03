package mcjty.theoneprobe.apiimpl;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.elements.*;
import mcjty.theoneprobe.config.ConfigSetup;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TheOneProbeImp implements ITheOneProbe {

    public static int ELEMENT_TEXT;
    public static int ELEMENT_TEXT_SMALL;
    public static int ELEMENT_ITEM;
    public static int ELEMENT_PROGRESS;
    public static int ELEMENT_HORIZONTAL;
    public static int ELEMENT_VERTICAL;
    public static int ELEMENT_ENTITY;
    public static int ELEMENT_ICON;
    public static int ELEMENT_ITEMLABEL;

    private final List<IProbeConfigProvider> configProviders = new ObjectArrayList<>();

    private List<IProbeInfoProvider> providers = new ObjectArrayList<>();
    private List<IProbeInfoEntityProvider> entityProviders = new ObjectArrayList<>();
    private final List<IBlockDisplayOverride> blockOverrides = new ObjectArrayList<>();
    private final List<IEntityDisplayOverride> entityOverrides = new ObjectArrayList<>();
    private final Map<Integer, IElementFactory> factories = new Int2ObjectOpenHashMap<>();
    private int lastId = 0;

    public TheOneProbeImp() {/**/}

    public static void registerElements() {
        ELEMENT_TEXT = TheOneProbe.theOneProbeImp.registerElementFactory(ElementText::new);
        ELEMENT_TEXT_SMALL = TheOneProbe.theOneProbeImp.registerElementFactory(ElementTextSmall::new);
        ELEMENT_ITEM = TheOneProbe.theOneProbeImp.registerElementFactory(ElementItemStack::new);
        ELEMENT_PROGRESS = TheOneProbe.theOneProbeImp.registerElementFactory(ElementProgress::new);
        ELEMENT_HORIZONTAL = TheOneProbe.theOneProbeImp.registerElementFactory(ElementHorizontal::new);
        ELEMENT_VERTICAL = TheOneProbe.theOneProbeImp.registerElementFactory(ElementVertical::new);
        ELEMENT_ENTITY = TheOneProbe.theOneProbeImp.registerElementFactory(ElementEntity::new);
        ELEMENT_ICON = TheOneProbe.theOneProbeImp.registerElementFactory(ElementIcon::new);
        ELEMENT_ITEMLABEL = TheOneProbe.theOneProbeImp.registerElementFactory(ElementItemLabel::new);
    }

    private int findProvider(@Nonnull String id) {
        for (int i = 0; i < this.providers.size(); i++) {
            if (id.equals(this.providers.get(i).getID())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void registerProvider(@Nonnull IProbeInfoProvider provider) {
        int idx = findProvider(provider.getID());
        if (idx != -1) {
            this.providers.set(idx, provider);
        } else {
            this.providers.add(provider);
        }
    }

    private int findEntityProvider(@Nonnull String id) {
        for (int i = 0; i < this.entityProviders.size(); i++) {
            if (id.equals(this.entityProviders.get(i).getID())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void registerEntityProvider(@Nonnull IProbeInfoEntityProvider provider) {
        int idx = findEntityProvider(provider.getID());
        if (idx != -1) this.entityProviders.set(idx, provider);
        else this.entityProviders.add(provider);
    }

    @Nullable
    @Override
    public IElementFactory getElementFactory(int id) {
        return this.factories.get(id);
    }

    public ProbeInfo create() {
        return new ProbeInfo();
    }

    public List<IProbeInfoProvider> getProviders() {
        return this.providers;
    }

    public List<IProbeInfoEntityProvider> getEntityProviders() {
        return this.entityProviders;
    }

    @Nullable
    private IProbeInfoProvider getProviderByID(@Nonnull String id) {
        for (IProbeInfoProvider provider : providers) {
            if (id.equals(provider.getID())) {
                return provider;
            }
        }
        return null;
    }

    @Nullable
    private IProbeInfoEntityProvider getEntityProviderByID(@Nonnull String id) {
        for (IProbeInfoEntityProvider provider : entityProviders) {
            if (id.equals(provider.getID())) {
                return provider;
            }
        }
        return null;
    }

    public void configureProviders(@Nonnull String[] sortedProviders, @Nonnull Set<String> excludedProviders) {
        List<IProbeInfoProvider> newProviders = new ObjectArrayList<>();
        for (String id : sortedProviders) {
            if (!excludedProviders.contains(id)) {
                final IProbeInfoProvider provider = getProviderByID(id);
                if (provider != null) {
                    newProviders.add(provider);
                }
            }
        }

        // Add all providers that are not in the list of sortedProviders and are also not
        // excluded.
        for (IProbeInfoProvider provider : this.providers) {
            if ((!newProviders.contains(provider)) && !excludedProviders.contains(provider.getID())) {
                newProviders.add(provider);
            }
        }

        this.providers = newProviders;
    }

    public void configureEntityProviders(@Nonnull String[] sortedProviders, @Nonnull Set<String> excludedProviders) {
        List<IProbeInfoEntityProvider> newProviders = new ArrayList<>();
        for (String id : sortedProviders) {
            if (!excludedProviders.contains(id)) {
                IProbeInfoEntityProvider provider = getEntityProviderByID(id);
                if (provider != null) newProviders.add(provider);
            }
        }

        // Add all providers that are not in the list of sortedProviders and are also not
        // excluded.
        for (IProbeInfoEntityProvider provider : this.entityProviders) {
            if ((!newProviders.contains(provider)) && !excludedProviders.contains(provider.getID())) {
                newProviders.add(provider);
            }
        }

        this.entityProviders = newProviders;
    }

    @Override
    public int registerElementFactory(@Nonnull IElementFactory factory) {
        this.factories.put(this.lastId, factory);
        int id = this.lastId;
        this.lastId++;
        return id;
    }

    @Nonnull
    @Override
    public IOverlayRenderer getOverlayRenderer() {
        return new DefaultOverlayRenderer();
    }

    @Nonnull
    @Override
    public IProbeConfig createProbeConfig() {
        return ConfigSetup.getDefaultConfig().lazyCopy();
    }

    @Override
    public void registerProbeConfigProvider(@Nonnull IProbeConfigProvider provider) {
        this.configProviders.add(provider);
    }

    public List<IProbeConfigProvider> getConfigProviders() {
        return this.configProviders;
    }

    @Override
    public void registerBlockDisplayOverride(@Nonnull IBlockDisplayOverride override) {
        this.blockOverrides.add(override);
    }

    @Nonnull
    public List<IBlockDisplayOverride> getBlockOverrides() {
        return this.blockOverrides;
    }

    @Override
    public void registerEntityDisplayOverride(@Nonnull IEntityDisplayOverride override) {
        this.entityOverrides.add(override);
    }

    @Nonnull
    public List<IEntityDisplayOverride> getEntityOverrides() {
        return this.entityOverrides;
    }
}

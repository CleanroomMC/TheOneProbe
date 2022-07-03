package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeConfig;

import javax.annotation.Nonnull;

import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.*;

public class ProbeConfig implements IProbeConfig {

    private int showRF = 1;
    private int showTank = 1;

    private IProbeConfig.ConfigMode showHarvestLevel = NORMAL;
    private IProbeConfig.ConfigMode showCanBeHarvested = NORMAL;
    private IProbeConfig.ConfigMode showModName = NORMAL;
    private IProbeConfig.ConfigMode showCropPercentage = NORMAL;
    private IProbeConfig.ConfigMode showChestContents = EXTENDED;
    private IProbeConfig.ConfigMode showChestContentsDetailed = EXTENDED;
    private IProbeConfig.ConfigMode showRedstone = NORMAL;
    private IProbeConfig.ConfigMode showMobHealth = NORMAL;
    private IProbeConfig.ConfigMode showMobGrowth = NORMAL;
    private IProbeConfig.ConfigMode showMobPotionEffects = EXTENDED;
    private IProbeConfig.ConfigMode showLeverSetting = NORMAL;
    private IProbeConfig.ConfigMode showTankSetting = EXTENDED;
    private IProbeConfig.ConfigMode showBrewStand = NORMAL;
    private IProbeConfig.ConfigMode showMobSpawner = NORMAL;
    private IProbeConfig.ConfigMode showMobOwner = EXTENDED;
    private IProbeConfig.ConfigMode showHorseStats = EXTENDED;
    private IProbeConfig.ConfigMode showSilverfish = NOT;

    /**
     * Make a lazy copy of this probe config.
     */
    public IProbeConfig lazyCopy() {
        return new LazyProbeConfig(this);
    }

    @Override
    public IProbeConfig setTankMode(int mode) {
        this.showTank = mode;
        return this;
    }

    @Override
    public int getTankMode() {
        return showTank;
    }

    @Override
    public IProbeConfig setRFMode(int showRF) {
        this.showRF = showRF;
        return this;
    }

    @Override
    public int getRFMode() {
        return showRF;
    }

    @Override
    public IProbeConfig showHorseStatSetting(@Nonnull ConfigMode mode) {
        showHorseStats = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getHorseStatSetting() {
        return showHorseStats;
    }

    @Override
    public IProbeConfig showAnimalOwnerSetting(@Nonnull ConfigMode mode) {
        showMobOwner = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getAnimalOwnerSetting() {
        return showMobOwner;
    }

    @Override
    public IProbeConfig showBrewStandSetting(@Nonnull ConfigMode mode) {
        showBrewStand = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowBrewStandSetting() {
        return showBrewStand;
    }

    @Override
    public IProbeConfig showMobSpawnerSetting(@Nonnull ConfigMode mode) {
        showMobSpawner = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowMobSpawnerSetting() {
        return showMobSpawner;
    }

    @Override
    public IProbeConfig showModName(@Nonnull ConfigMode mode) {
        showModName = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowModName() {
        return showModName;
    }

    @Override
    public IProbeConfig showHarvestLevel(@Nonnull ConfigMode mode) {
        showHarvestLevel = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowHarvestLevel() {
        return showHarvestLevel;
    }

    @Override
    public IProbeConfig showCanBeHarvested(@Nonnull ConfigMode mode) {
        showCanBeHarvested = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowCanBeHarvested() {
        return showCanBeHarvested;
    }

    @Override
    public IProbeConfig showCropPercentage(@Nonnull ConfigMode mode) {
        showCropPercentage = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowCropPercentage() {
        return showCropPercentage;
    }

    @Override
    public IProbeConfig showChestContents(@Nonnull ConfigMode mode) {
        showChestContents = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowChestContents() {
        return showChestContents;
    }

    @Override
    public IProbeConfig showChestContentsDetailed(@Nonnull ConfigMode mode) {
        showChestContentsDetailed = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowChestContentsDetailed() {
        return showChestContentsDetailed;
    }

    @Override
    public IProbeConfig showRedstone(@Nonnull ConfigMode mode) {
        showRedstone = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowRedstone() {
        return showRedstone;
    }

    @Override
    public IProbeConfig showMobHealth(@Nonnull ConfigMode mode) {
        showMobHealth = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowMobHealth() {
        return showMobHealth;
    }

    @Override
    public IProbeConfig showMobGrowth(@Nonnull ConfigMode mode) {
        showMobGrowth = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowMobGrowth() {
        return showMobGrowth;
    }

    @Override
    public IProbeConfig showMobPotionEffects(@Nonnull ConfigMode mode) {
        showMobPotionEffects = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowMobPotionEffects() {
        return showMobPotionEffects;
    }

    @Override
    public IProbeConfig showLeverSetting(@Nonnull ConfigMode mode) {
        showLeverSetting = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowLeverSetting() {
        return showLeverSetting;
    }

    @Override
    public IProbeConfig showTankSetting(@Nonnull ConfigMode mode) {
        showTankSetting = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowTankSetting() {
        return showTankSetting;
    }

    @Override
    public IProbeConfig showSilverfish(@Nonnull ConfigMode mode) {
        showSilverfish = mode;
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowSilverfish() {
        return showSilverfish;
    }
}

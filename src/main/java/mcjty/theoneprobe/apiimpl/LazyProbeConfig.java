package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeConfig;

import javax.annotation.Nonnull;

public class LazyProbeConfig implements IProbeConfig {

    private IProbeConfig original;
    private boolean dirty = false;

    public LazyProbeConfig(@Nonnull IProbeConfig original) {
        this.original = original;
    }

    private IProbeConfig realCopy() {
        if (!this.dirty) {
            this.dirty = true;
            this.original = new ProbeConfig()
                    .setRFMode(original.getRFMode())
                    .setTankMode(original.getTankMode())
                    .showModName(original.getShowModName())
                    .showChestContents(original.getShowChestContents())
                    .showChestContentsDetailed(original.getShowChestContentsDetailed())
                    .showCropPercentage(original.getShowCropPercentage())
                    .showHarvestLevel(original.getShowHarvestLevel())
                    .showCanBeHarvested(original.getShowCanBeHarvested())
                    .showMobHealth(original.getShowMobHealth())
                    .showMobGrowth(original.getShowMobGrowth())
                    .showMobPotionEffects(original.getShowMobPotionEffects())
                    .showRedstone(original.getShowRedstone())
                    .showLeverSetting(original.getShowLeverSetting())
                    .showTankSetting(original.getShowTankSetting())
                    .showBrewStandSetting(original.getShowBrewStandSetting())
                    .showMobSpawnerSetting(original.getShowMobSpawnerSetting())
                    .showAnimalOwnerSetting(original.getAnimalOwnerSetting())
                    .showHorseStatSetting(original.getHorseStatSetting())
                    .showSilverfish(original.getShowSilverfish());
        }
        return this.original;
    }

    @Override
    public IProbeConfig setTankMode(int mode) {
        realCopy().setTankMode(mode);
        return this;
    }

    @Override
    public int getTankMode() {
        return original.getTankMode();
    }

    @Override
    public IProbeConfig showHorseStatSetting(@Nonnull ConfigMode mode) {
        realCopy().showHorseStatSetting(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getHorseStatSetting() {
        return original.getHorseStatSetting();
    }

    @Override
    public IProbeConfig showAnimalOwnerSetting(@Nonnull ConfigMode mode) {
        realCopy().showAnimalOwnerSetting(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getAnimalOwnerSetting() {
        return original.getAnimalOwnerSetting();
    }

    @Override
    public IProbeConfig showMobSpawnerSetting(@Nonnull ConfigMode mode) {
        realCopy().showMobSpawnerSetting(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowMobSpawnerSetting() {
        return original.getShowMobSpawnerSetting();
    }

    @Override
    public IProbeConfig showBrewStandSetting(@Nonnull ConfigMode mode) {
        realCopy().showBrewStandSetting(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowBrewStandSetting() {
        return original.getShowBrewStandSetting();
    }

    @Override
    public IProbeConfig showTankSetting(@Nonnull ConfigMode mode) {
        realCopy().showTankSetting(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowTankSetting() {
        return original.getShowTankSetting();
    }

    @Override
    public IProbeConfig setRFMode(int showRF) {
        realCopy().setRFMode(showRF);
        return this;
    }

    @Override
    public int getRFMode() {
        return original.getRFMode();
    }

    @Override
    public IProbeConfig showModName(@Nonnull ConfigMode mode) {
        realCopy().showModName(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowModName() {
        return original.getShowModName();
    }

    @Override
    public IProbeConfig showHarvestLevel(@Nonnull ConfigMode mode) {
        realCopy().showHarvestLevel(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowHarvestLevel() {
        return original.getShowHarvestLevel();
    }

    @Override
    public IProbeConfig showCanBeHarvested(@Nonnull ConfigMode mode) {
        realCopy().showCanBeHarvested(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowCanBeHarvested() {
        return original.getShowCanBeHarvested();
    }

    @Override
    public IProbeConfig showCropPercentage(@Nonnull ConfigMode mode) {
        realCopy().showCropPercentage(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowCropPercentage() {
        return original.getShowCropPercentage();
    }

    @Override
    public IProbeConfig showChestContents(@Nonnull ConfigMode mode) {
        realCopy().showChestContents(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowChestContents() {
        return original.getShowChestContents();
    }

    @Override
    public IProbeConfig showChestContentsDetailed(@Nonnull ConfigMode mode) {
        realCopy().showChestContentsDetailed(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowChestContentsDetailed() {
        return original.getShowChestContentsDetailed();
    }

    @Override
    public IProbeConfig showRedstone(@Nonnull ConfigMode mode) {
        realCopy().showRedstone(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowRedstone() {
        return original.getShowRedstone();
    }

    @Override
    public IProbeConfig showMobHealth(@Nonnull ConfigMode mode) {
        realCopy().showMobHealth(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowMobHealth() {
        return original.getShowMobHealth();
    }

    @Override
    public IProbeConfig showMobGrowth(@Nonnull ConfigMode mode) {
        realCopy().showMobGrowth(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowMobGrowth() {
        return original.getShowMobGrowth();
    }

    @Override
    public IProbeConfig showMobPotionEffects(@Nonnull ConfigMode mode) {
        realCopy().showMobPotionEffects(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowMobPotionEffects() {
        return original.getShowMobPotionEffects();
    }

    @Override
    public IProbeConfig showLeverSetting(@Nonnull ConfigMode mode) {
        realCopy().showLeverSetting(mode);
        return this;
    }

    @Nonnull
    @Override
    public ConfigMode getShowLeverSetting() {
        return original.getShowLeverSetting();
    }

    @Nonnull
    @Override
    public ConfigMode getShowSilverfish() {
        return original.getShowSilverfish();
    }

    @Override
    public IProbeConfig showSilverfish(@Nonnull ConfigMode mode) {
        realCopy().showSilverfish(mode);
        return this;
    }
}

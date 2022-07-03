package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.ConfigSetup;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.UUID;

import static mcjty.theoneprobe.api.IProbeInfo.ENDLOC;
import static mcjty.theoneprobe.api.IProbeInfo.STARTLOC;
import static mcjty.theoneprobe.api.TextStyleClass.*;

public class DefaultProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Nonnull
    @Override
    public String getID() {
        return String.format("%s:entity.default", TheOneProbe.MODID);
    }

    private static final DecimalFormat FORMAT_COMMAS = new DecimalFormat("##.#");

    @Override
    public void addProbeEntityInfo(@Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull Entity entity, @Nonnull IProbeHitEntityData data) {
        boolean handled = false;
        for (IEntityDisplayOverride override : TheOneProbe.theOneProbeImp.getEntityOverrides()) {
            if (override.overrideStandardInfo(mode, probeInfo, player, world, entity, data)) {
                handled = true;
                break;
            }
        }
        final IProbeConfig config = ConfigSetup.getRealConfig();

        if (!handled) showStandardInfo(mode, probeInfo, entity, config);

        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase livingBase = (EntityLivingBase) entity;

            if (Tools.show(mode, config.getShowMobHealth())) {
                int health = (int) livingBase.getHealth();
                int maxHealth = (int) livingBase.getMaxHealth();
                int armor = livingBase.getTotalArmorValue();

                probeInfo.progress(health, maxHealth, probeInfo.defaultProgressStyle().lifeBar(true).showText(false).renderBG(true).width(150).height(10));

                if (mode == ProbeMode.EXTENDED) {
                    probeInfo.text(LABEL + "{*theoneprobe.provider.entity.health*} " + INFOIMP + health + " / " + maxHealth);
                }

                if (armor > 0) {
                    probeInfo.progress(armor, armor, probeInfo.defaultProgressStyle().armorBar(true).showText(false).renderBG(true).width(80).height(10));
                }
            }

            if (Tools.show(mode, config.getShowMobGrowth()) && entity instanceof EntityAgeable) {
                int age = ((EntityAgeable) entity).getGrowingAge();
                if (age < 0)
                    probeInfo.text(LABEL + "{*theoneprobe.provider.entity.growing_time*} " + ((age * -1) / 20) + "s");
            }

            if (Tools.show(mode, config.getShowMobPotionEffects())) {
                Collection<PotionEffect> effects = livingBase.getActivePotionEffects();
                if (!effects.isEmpty()) {
                    IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xFFFFFFFF));
                    for (PotionEffect effect : effects) {
                        String s1 = STARTLOC + effect.getEffectName() + ENDLOC;
                        Potion potion = effect.getPotion();
                        if (effect.getAmplifier() > 0) {
                            s1 += " " + STARTLOC + "potion.potency." + effect.getAmplifier() + ENDLOC;
                        }

                        if (effect.getDuration() > 20) {
                            s1 += " (" + getPotionDurationString(effect, 1.0F) + ")";
                        }

                        if (potion.isBadEffect()) {
                            vertical.text(ERROR + s1);
                        } else {
                            vertical.text(OK + s1);
                        }
                    }
                }
            }
        } else if (entity instanceof EntityItemFrame) {
            final EntityItemFrame itemFrame = (EntityItemFrame) entity;
            final ItemStack stack = itemFrame.getDisplayedItem();
            if (!stack.isEmpty()) {
                probeInfo.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER))
                        .item(stack, new ItemStyle().width(16).height(16))
                        .text(INFO + stack.getDisplayName());
                if (mode == ProbeMode.EXTENDED) {
                    probeInfo.text(LABEL + "{*theoneprobe.provider.entity.rotation*} " + INFO + itemFrame.getRotation());
                }
            } else {
                probeInfo.text(LABEL + "{*theoneprobe.provider.entity.empty*}");
            }
        }

        if (Tools.show(mode, config.getAnimalOwnerSetting())) {
            UUID ownerId = null;
            if (entity instanceof IEntityOwnable) {
                ownerId = ((IEntityOwnable) entity).getOwnerId();
            } else if (entity instanceof EntityHorse) {
                ownerId = ((EntityHorse) entity).getOwnerUniqueId();
            }

            if (ownerId != null) {
                final String username = UsernameCache.getLastKnownUsername(ownerId);
                if (username == null) probeInfo.text(WARNING + "{*theoneprobe.provider.entity.unknown_owner*}");
                else probeInfo.text(LABEL + "{*theoneprobe.provider.entity.owner*} " + INFO + username);
            } else if (entity instanceof EntityTameable) {
                probeInfo.text(LABEL + "{*theoneprobe.provider.entity.tamable*}");
            }
        }

        if (Tools.show(mode, config.getHorseStatSetting())) {
            if (entity instanceof EntityHorse) {
                double jumpStrength = ((EntityHorse) entity).getHorseJumpStrength();
                double jumpHeight = -0.1817584952 * jumpStrength * jumpStrength * jumpStrength + 3.689713992 * jumpStrength * jumpStrength + 2.128599134 * jumpStrength - 0.343930367;
                probeInfo.text(LABEL + "{*theoneprobe.provider.entity.jump_height*} " + INFO + FORMAT_COMMAS.format(jumpHeight));
                IAttributeInstance iattributeinstance = ((EntityHorse) entity).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                probeInfo.text(LABEL + "{*theoneprobe.provider.entity.speed*} " + INFO + FORMAT_COMMAS.format(iattributeinstance.getAttributeValue()));
            }
        }

        if (entity instanceof EntityWolf && ConfigSetup.showCollarColor && ((EntityWolf) entity).isTamed()) {
            probeInfo.text(LABEL + "{*theoneprobe.provider.entity.collar*} " + INFO + ((EntityWolf) entity).getCollarColor().getName());
        }
    }

    @Nonnull
    public static String getPotionDurationString(@Nonnull PotionEffect effect, float factor) {
        if (effect.getDuration() == 32767) {
            return "**:**";
        } else {
            int i = MathHelper.floor(effect.getDuration() * factor);
            return StringUtils.ticksToElapsedTime(MathHelper.floor(effect.getDuration() * factor));
        }
    }

    public static void showStandardInfo(@Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull Entity entity, @Nonnull IProbeConfig config) {
        final String modid = Tools.getModName(entity);

        if (Tools.show(mode, config.getShowModName())) {
            probeInfo.horizontal()
                    .entity(entity)
                    .vertical()
                    .text(NAME + entity.getDisplayName().getFormattedText())
                    .text(MODNAME + modid);
        } else {
            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                    .entity(entity)
                    .text(NAME + entity.getDisplayName().getFormattedText());
        }
    }
}

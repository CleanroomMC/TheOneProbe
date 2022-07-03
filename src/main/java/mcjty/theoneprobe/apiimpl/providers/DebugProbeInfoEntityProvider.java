package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.ConfigSetup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;

public class DebugProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Nonnull
    @Override
    public String getID() {
        return String.format("%s:entity.debug", TheOneProbe.MODID);
    }

    @Override
    public void addProbeEntityInfo(@Nonnull ProbeMode mode, @Nonnull IProbeInfo probeInfo, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull Entity entity, @Nonnull IProbeHitEntityData data) {
        if (mode == ProbeMode.DEBUG && ConfigSetup.showDebugInfo && entity instanceof EntityLivingBase) {
            IProbeInfo vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xFFFF4444).spacing(2));

            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            final int totalArmorValue = entityLivingBase.getTotalArmorValue();
            final int age = entityLivingBase.getIdleTime();
            final float absorptionAmount = entityLivingBase.getAbsorptionAmount();
            final float aiMoveSpeed = entityLivingBase.getAIMoveSpeed();
            final int revengeTimer = entityLivingBase.getRevengeTimer();
            vertical.text(LABEL + "Tot armor: " + INFO + totalArmorValue)
                    .text(LABEL + "Age: " + INFO + age)
                    .text(LABEL + "Absorption: " + INFO + absorptionAmount)
                    .text(LABEL + "AI Move Speed: " + INFO + aiMoveSpeed)
                    .text(LABEL + "Revenge Timer: " + INFO + revengeTimer);
            if (entity instanceof EntityAgeable) {
                int growingAge = ((EntityAgeable) entity).getGrowingAge();
                vertical.text(LABEL + "Growing Age: " + INFO + growingAge);
            }
        }
    }
}

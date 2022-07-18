package mcjty.theoneprobe.rendering;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitData;
import mcjty.theoneprobe.apiimpl.ProbeHitEntityData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.apiimpl.elements.ElementText;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.network.ThrowableIdentity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;

public class OverlayRenderer {

    private static Map<Pair<Integer, BlockPos>, Pair<Long, ProbeInfo>> cachedInfo = new Object2ObjectOpenHashMap<>();
    private static Map<UUID, Pair<Long, ProbeInfo>> cachedEntityInfo = new Object2ObjectOpenHashMap<>();
    private static long lastCleanupTime = 0;

    // For a short while we keep displaying the last pair if we have no new information
    // to prevent flickering
    private static Pair<Long, ProbeInfo> lastPair;
    private static long lastPairTime = 0;

    // When the server delays too long we also show some preliminary information already
    private static long lastRenderedTime = -1;

    public static void registerProbeInfo(int dim, BlockPos pos, ProbeInfo probeInfo) {
        if (probeInfo == null) return;
        final long time = System.currentTimeMillis();
        cachedInfo.put(Pair.of(dim, pos), Pair.of(time, probeInfo));
    }

    public static void registerProbeInfo(UUID uuid, ProbeInfo probeInfo) {
        if (probeInfo == null) return;
        final long time = System.currentTimeMillis();
        cachedEntityInfo.put(uuid, Pair.of(time, probeInfo));
    }

    public static void renderHUD(ProbeMode mode, float partialTicks) {
        final float dist = ConfigSetup.probeDistance;

        RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver != null) {
            if (mouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                GlStateManager.pushMatrix();

                ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
                final double sw = scaledresolution.getScaledWidth_double();
                final double sh = scaledresolution.getScaledHeight_double();
                final double scale = ConfigSetup.tooltipScale;

                setupOverlayRendering(sw * scale, sh * scale);
                renderHUDEntity(mode, mouseOver, sw * scale, sh * scale);
                setupOverlayRendering(sw, sh);
                GlStateManager.popMatrix();

                checkCleanup();
                return;
            }
        }

        final EntityPlayerSP entity = Minecraft.getMinecraft().player;
        final Vec3d start = entity.getPositionEyes(partialTicks);
        final Vec3d vec31 = entity.getLook(partialTicks);
        final Vec3d end = start.add(vec31.x * dist, vec31.y * dist, vec31.z * dist);

        mouseOver = entity.getEntityWorld().rayTraceBlocks(start, end, entity.isSneaking());
        if (mouseOver == null) return;

        if (mouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            GlStateManager.pushMatrix();

            ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            final double sw = scaledresolution.getScaledWidth_double();
            final double sh = scaledresolution.getScaledHeight_double();
            final double scale = ConfigSetup.tooltipScale;

            setupOverlayRendering(sw * scale, sh * scale);
            renderHUDBlock(mode, mouseOver, sw * scale, sh * scale);
            setupOverlayRendering(sw, sh);

            GlStateManager.popMatrix();
        }

        checkCleanup();
    }

    public static void setupOverlayRendering(double sw, double sh) {
        GlStateManager.clear(256);
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, sw, sh, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
    }

    private static void checkCleanup() {
        long time = System.currentTimeMillis();
        if (time > lastCleanupTime + 5000) {
            cleanupCachedBlocks(time);
            cleanupCachedEntities(time);
            lastCleanupTime = time;
        }
    }

    private static void renderHUDEntity(ProbeMode mode, @Nonnull RayTraceResult mouseOver, double sw, double sh) {
        Entity entity = mouseOver.entityHit;
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (entity == null) return;
        if (entity instanceof MultiPartEntityPart) {
            MultiPartEntityPart part = (MultiPartEntityPart) entity;
            if (part.parent instanceof Entity) {
                entity = (Entity) part.parent;
            }
        }

        // We can't show info for this entity
        if (EntityList.getEntityString(entity) == null && !(entity instanceof EntityPlayer)) return;

        ProbeInfo info = TheOneProbe.theOneProbeImp.create();
        final IProbeHitEntityData data = new ProbeHitEntityData(mouseOver.hitVec);

        IProbeConfig config = TheOneProbe.theOneProbeImp.createProbeConfig();
        for (IProbeConfigProvider provider : TheOneProbe.theOneProbeImp.getConfigProviders()) provider.getProbeConfig(config, player, player.world, entity, data);
        ConfigSetup.setRealConfig(config);

        for (IProbeInfoEntityProvider provider : TheOneProbe.theOneProbeImp.getEntityProviders()) {
            try {
                provider.addProbeEntityInfo(mode, info, player, player.world, entity, data);
            } catch (Throwable e) {
                ThrowableIdentity.registerThrowable(e);
                info.text(LABEL + "{*theoneprobe.provider.error*} " + ERROR + provider.getID());
            }
        }

        renderElements(info, ConfigSetup.getDefaultOverlayStyle(), sw, sh, null);
    }

    private static void renderHUDBlock(ProbeMode mode, @Nonnull RayTraceResult mouseOver, double sw, double sh) {
        BlockPos blockPos = mouseOver.getBlockPos();
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        World world = player.getEntityWorld();
        if (world.isAirBlock(blockPos)) return;

        IElement damageElement = null;
        if (ConfigSetup.showBreakProgress > 0) {
            float damage = Minecraft.getMinecraft().playerController.curBlockDamageMP;
            if (damage > 0) {
                if (ConfigSetup.showBreakProgress == 2) {
                    damageElement = new ElementText(TextFormatting.RED + "{*theoneprobe.provider.progress*}" + (int) (damage * 100) + "%");
                } else {
                    damageElement = new ElementProgress((long) (damage * 100), 100, new ProgressStyle()
                            .prefix("Progress ") //todo localization in prefix() and suffix()
                            .suffix("%")
                            .width(85)
                            .borderColorTop(0)
                            .borderColorBottom(0)
                            .filledColor(0)
                            .filledColor(0xFF990000)
                            .alternateFilledColor(0xFF550000));
                }
            }
        }

        IBlockState state = world.getBlockState(blockPos);
        ProbeInfo info = TheOneProbe.theOneProbeImp.create();
        IProbeHitData data = new ProbeHitData(blockPos, mouseOver.hitVec, mouseOver.sideHit, state.getBlock().getPickBlock(state, mouseOver, world, blockPos, player));

        IProbeConfig probeConfig = TheOneProbe.theOneProbeImp.createProbeConfig();
        for (IProbeConfigProvider provider : TheOneProbe.theOneProbeImp.getConfigProviders()) provider.getProbeConfig(probeConfig, player, world, state, data);
        ConfigSetup.setRealConfig(probeConfig);

        for (IProbeInfoProvider provider : TheOneProbe.theOneProbeImp.getProviders()) {
            try {
                provider.addProbeInfo(mode, info, player, world, state, data);
            } catch (Throwable e) {
                ThrowableIdentity.registerThrowable(e);
                info.text(LABEL + "{*theoneprobe.provider.error*} " + ERROR + provider.getID());
            }
        }

        renderElements(info, ConfigSetup.getDefaultOverlayStyle(), sw, sh, damageElement);
    }

    public static void renderOverlay(IOverlayStyle style, IProbeInfo probeInfo) {
        GlStateManager.pushMatrix();

        Minecraft minecraft = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(minecraft);
        final double sw = scaledresolution.getScaledWidth_double();
        final double sh = scaledresolution.getScaledHeight_double();

        final double scale = ConfigSetup.tooltipScale;

        setupOverlayRendering(sw * scale, sh * scale);
        renderElements((ProbeInfo) probeInfo, style, sw * scale, sh * scale, null);
        setupOverlayRendering(sw, sh);
        GlStateManager.popMatrix();
    }

    private static void cleanupCachedBlocks(long time) {
        // It has been a while. Time to clean up unused cached pairs.
        Map<Pair<Integer, BlockPos>, Pair<Long, ProbeInfo>> newCachedInfo = new Object2ObjectOpenHashMap<>();
        for (Map.Entry<Pair<Integer, BlockPos>, Pair<Long, ProbeInfo>> entry : cachedInfo.entrySet()) {
            long t = entry.getValue().getLeft();
            if (time < t + ConfigSetup.timeout + 1000) {
                newCachedInfo.put(entry.getKey(), entry.getValue());
            }
        }
        cachedInfo = newCachedInfo;
    }

    private static void cleanupCachedEntities(long time) {
        // It has been a while. Time to clean up unused cached pairs.
        Map<UUID, Pair<Long, ProbeInfo>> newCachedInfo = new Object2ObjectOpenHashMap<>();
        for (Map.Entry<UUID, Pair<Long, ProbeInfo>> entry : cachedEntityInfo.entrySet()) {
            long t = entry.getValue().getLeft();
            if (time < t + ConfigSetup.timeout + 1000) {
                newCachedInfo.put(entry.getKey(), entry.getValue());
            }
        }
        cachedEntityInfo = newCachedInfo;
    }

    public static void renderElements(ProbeInfo probeInfo, IOverlayStyle style, double sw, double sh, @Nullable IElement extra) {
        if (extra != null) probeInfo.element(extra);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();

//        final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
//        final int scaledWidth = scaledresolution.getScaledWidth();
//        final int scaledHeight = scaledresolution.getScaledHeight();
        final int scaledWidth = (int) sw;
        final int scaledHeight = (int) sh;

        final int offset = style.getBorderOffset();
        final int thick = style.getBorderThickness();

        int w = probeInfo.getWidth();
        int h = probeInfo.getHeight();
        int margin = 0;
        if (thick > 0) {
            w += (offset + thick + 3) * 2;
            h += (offset + thick + 3) * 2;
            margin = offset + thick + 3;
        }

        int x;
        int y;
        if (style.getLeftX() != -1) {
            x = style.getLeftX();
        } else if (style.getRightX() != -1) {
            x = scaledWidth - w - style.getRightX();
        } else {
            x = (scaledWidth - w) / 2;
        }
        if (style.getTopY() != -1) {
            y = style.getTopY();
        } else if (style.getBottomY() != -1) {
            y = scaledHeight - h - style.getBottomY();
        } else {
            y = (scaledHeight - h) / 2;
        }

        if (thick > 0) {
            if (offset > 0) {
                RenderHelper.drawOutlineBox(x, y, x + w - 1, y + h - 1, thick, style.getBoxColor());
            }
            RenderHelper.drawThickBeveledBoxGradient(x + offset, y + offset, x + w - 1 - offset, y + h - 1 - offset, thick, style.getBorderColorTop(), style.getBorderColorBottom(), style.getBoxColor());
        }

        if (!Minecraft.getMinecraft().isGamePaused()) RenderHelper.rot += .5f;

        probeInfo.render(x + margin, y + margin);
        if (extra != null) probeInfo.removeElement(extra);
    }
}

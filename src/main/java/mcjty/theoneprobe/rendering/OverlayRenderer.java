package mcjty.theoneprobe.rendering;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitData;
import mcjty.theoneprobe.apiimpl.ProbeHitEntityData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.apiimpl.elements.ElementText;
import mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoEntityProvider;
import mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoProvider;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.network.PacketGetEntityInfo;
import mcjty.theoneprobe.network.PacketGetInfo;
import mcjty.theoneprobe.network.PacketHandler;
import mcjty.theoneprobe.network.ThrowableIdentity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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

        mouseOver = entity.getEntityWorld().rayTraceBlocks(start, end, ConfigSetup.showLiquids);
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
        if (entity == null) return;
        if (entity instanceof MultiPartEntityPart) {
            MultiPartEntityPart part = (MultiPartEntityPart) entity;
            if (part.parent instanceof Entity) {
                entity = (Entity) part.parent;
            }
        }

        // We can't show info for this entity
        if (EntityList.getEntityString(entity) == null && !(entity instanceof EntityPlayer)) return;

        final UUID uuid = entity.getPersistentID();

        final EntityPlayerSP player = Minecraft.getMinecraft().player;
        final long time = System.currentTimeMillis();

        Pair<Long, ProbeInfo> cacheEntry = cachedEntityInfo.get(uuid);
        if (cacheEntry == null || cacheEntry.getValue() == null) {

            // To make sure we don't ask it too many times before the server got a chance to send the answer
            // we insert a dummy entry here for a while
            if (cacheEntry == null || time >= cacheEntry.getLeft()) {
                cachedEntityInfo.put(uuid, Pair.of(time + 500, null));
                requestEntityInfo(mode, mouseOver, entity, player);
            }

            if (lastPair != null && time < lastPairTime + ConfigSetup.timeout) {
                renderElements(lastPair.getRight(), ConfigSetup.getDefaultOverlayStyle(), sw, sh, null);
                lastRenderedTime = time;
            } else if (ConfigSetup.waitingForServerTimeout > 0 && lastRenderedTime != -1 && time > lastRenderedTime + ConfigSetup.waitingForServerTimeout) {
                // It has been a while. Show some info on client that we are
                // waiting for server information
                ProbeInfo info = getWaitingEntityInfo(mode, mouseOver, entity, player);
                registerProbeInfo(uuid, info);
                lastPair = Pair.of(time, info);
                lastPairTime = time;
                renderElements(lastPair.getRight(), ConfigSetup.getDefaultOverlayStyle(), sw, sh, null);
                lastRenderedTime = time;
            }
        } else {
            if (time > cacheEntry.getLeft() + ConfigSetup.timeout) {
                // This info is slightly old. Update it

                // To make sure we don't ask it too many times before the server got a chance to send the answer
                // we increase the time a bit here
                cachedEntityInfo.put(uuid, Pair.of(time + 500, cacheEntry.getRight()));
                requestEntityInfo(mode, mouseOver, entity, player);
            }
            renderElements(cacheEntry.getRight(), ConfigSetup.getDefaultOverlayStyle(), sw, sh, null);
            lastRenderedTime = time;
            lastPair = cacheEntry;
            lastPairTime = time;
        }
    }

    private static void requestEntityInfo(ProbeMode mode, RayTraceResult mouseOver, Entity entity, @Nonnull EntityPlayerSP player) {
        PacketHandler.INSTANCE.sendToServer(new PacketGetEntityInfo(player.getEntityWorld().provider.getDimension(), mode, mouseOver, entity));
    }

    private static void renderHUDBlock(ProbeMode mode, @Nonnull RayTraceResult mouseOver, double sw, double sh) {
        BlockPos blockPos = mouseOver.getBlockPos();
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player.getEntityWorld().isAirBlock(blockPos)) return;

        final long time = System.currentTimeMillis();

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

        int dimension = player.getEntityWorld().provider.getDimension();
        Pair<Integer, BlockPos> key = Pair.of(dimension, blockPos);
        Pair<Long, ProbeInfo> cacheEntry = cachedInfo.get(key);
        if (cacheEntry == null || cacheEntry.getValue() == null) {

            // To make sure we don't ask it too many times before the server got a chance to send the answer
            // we insert a dummy entry here for a while
            if (cacheEntry == null || time >= cacheEntry.getLeft()) {
                cachedInfo.put(key, Pair.of(time + 500, null));
                requestBlockInfo(mode, mouseOver, blockPos, player);
            }

            if (lastPair != null && time < lastPairTime + ConfigSetup.timeout) {
                renderElements(lastPair.getRight(), ConfigSetup.getDefaultOverlayStyle(), sw, sh, damageElement);
                lastRenderedTime = time;
            } else if (ConfigSetup.waitingForServerTimeout > 0 && lastRenderedTime != -1 && time > lastRenderedTime + ConfigSetup.waitingForServerTimeout) {
                // It has been a while. Show some info on client that we are
                // waiting for server information
                ProbeInfo info = getWaitingInfo(mode, mouseOver, blockPos, player);
                registerProbeInfo(dimension, blockPos, info);
                lastPair = Pair.of(time, info);
                lastPairTime = time;
                renderElements(lastPair.getRight(), ConfigSetup.getDefaultOverlayStyle(), sw, sh, damageElement);
                lastRenderedTime = time;
            }
        } else {
            if (time > cacheEntry.getLeft() + ConfigSetup.timeout) {
                // This info is slightly old. Update it

                // To make sure we don't ask it too many times before the server got a chance to send the answer
                // we increase the time a bit here
                cachedInfo.put(key, Pair.of(time + 500, cacheEntry.getRight()));
                requestBlockInfo(mode, mouseOver, blockPos, player);
            }
            renderElements(cacheEntry.getRight(), ConfigSetup.getDefaultOverlayStyle(), sw, sh, damageElement);
            lastRenderedTime = time;
            lastPair = cacheEntry;
            lastPairTime = time;
        }
    }

    // Information for when the server is laggy
    @Nonnull
    private static ProbeInfo getWaitingInfo(ProbeMode mode, RayTraceResult mouseOver, BlockPos blockPos, @Nonnull EntityPlayerSP player) {
        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();

        World world = player.getEntityWorld();
        IBlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        ItemStack pickBlock = block.getPickBlock(blockState, mouseOver, world, blockPos, player);
        IProbeHitData data = new ProbeHitData(blockPos, mouseOver.hitVec, mouseOver.sideHit, pickBlock);

        IProbeConfig probeConfig = TheOneProbe.theOneProbeImp.createProbeConfig();
        try {
            DefaultProbeInfoProvider.showStandardBlockInfo(probeConfig, mode, probeInfo, blockState, block, world, blockPos, player, data);
        } catch (Exception e) {
            ThrowableIdentity.registerThrowable(e);
            probeInfo.text(ERROR + "Error (see log for details)!");
        }

        probeInfo.text(ERROR + "Waiting for server...");
        return probeInfo;
    }

    @Nonnull
    private static ProbeInfo getWaitingEntityInfo(ProbeMode mode, @Nonnull RayTraceResult mouseOver, Entity entity, EntityPlayerSP player) {
        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();
        IProbeHitEntityData data = new ProbeHitEntityData(mouseOver.hitVec);

        IProbeConfig probeConfig = TheOneProbe.theOneProbeImp.createProbeConfig();
        try {
            DefaultProbeInfoEntityProvider.showStandardInfo(mode, probeInfo, entity, probeConfig);
        } catch (Exception e) {
            ThrowableIdentity.registerThrowable(e);
            probeInfo.text(ERROR + "Error (see log for details)!");
        }

        probeInfo.text(ERROR + "Waiting for server...");
        return probeInfo;
    }

    private static void requestBlockInfo(ProbeMode mode, RayTraceResult mouseOver, BlockPos blockPos, @Nonnull EntityPlayerSP player) {
        World world = player.getEntityWorld();
        IBlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        ItemStack pickBlock = block.getPickBlock(blockState, mouseOver, world, blockPos, player);
        //noinspection ConstantConditions
        if (pickBlock == null || (!pickBlock.isEmpty() && pickBlock.getItem() == null)) {
            // Protection for some invalid items.
            pickBlock = ItemStack.EMPTY;
        }
        //noinspection ConstantConditions
        if (pickBlock != null && (!pickBlock.isEmpty()) && ConfigSetup.getDontSendNBTSet().contains(pickBlock.getItem().getRegistryName())) {
            pickBlock = pickBlock.copy();
            pickBlock.setTagCompound(null);
        }
        PacketHandler.INSTANCE.sendToServer(new PacketGetInfo(world.provider.getDimension(), blockPos, mode, mouseOver, pickBlock));
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

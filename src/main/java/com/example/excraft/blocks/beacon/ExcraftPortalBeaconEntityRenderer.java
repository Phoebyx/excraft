package com.example.excraft.blocks.beacon;

import com.example.excraft.blocks.portal.ExcraftPortalTint;
import com.example.excraft.data.ExcraftTimer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;


import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ExcraftPortalBeaconEntityRenderer implements BlockEntityRenderer<ExcraftPortalBeaconBlockEntity> {
    public ExcraftPortalBeaconEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(ExcraftPortalBeaconBlockEntity beaconEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        BeaconRenderer.renderBeaconBeam(
                poseStack,
                multiBufferSource,
                BeaconRenderer.BEAM_LOCATION,
                v,
                1.0F,
                beaconEntity.getLevel().getGameTime(),
                0,
                512,
                beaconColour(beaconEntity),
                sizeOverDistance(beaconEntity),
                sizeOverDistance(beaconEntity)
        );
    }

    private int beaconColour(ExcraftPortalBeaconBlockEntity beaconEntity) {
        List<Integer> colors = ExcraftPortalTint.getCurrentColour();
        long gameTime = beaconEntity.getLevel().getGameTime();
        return colors.get(Math.clamp(ExcraftTimer.intPortalBlockColor(gameTime),0,colors.size() - 1) );
    }

    @Override
    public boolean shouldRender(ExcraftPortalBeaconBlockEntity blockEntity, Vec3 cameraPos) {
        return Vec3.atCenterOf(blockEntity.getBlockPos()).multiply((double)1.0F, (double)0.0F, (double)1.0F).closerThan(cameraPos.multiply((double)1.0F, (double)0.0F, (double)1.0F), (double)this.getViewDistance());
    }

    public float sizeOverDistance(ExcraftPortalBeaconBlockEntity blockEntity) {
        Minecraft minecraft = Minecraft.getInstance();
        Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().getPosition();
        BlockPos pos = blockEntity.getBlockPos();
        float distance = (float) Math.pow(cameraPos.distanceTo(pos.getCenter()) * 0.003F,2);
        return Math.max(distance, 0.2F);
    }

    @Override
    public boolean shouldRenderOffScreen(ExcraftPortalBeaconBlockEntity blockEntity) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(ExcraftPortalBeaconBlockEntity blockEntity) {
        return AABB.INFINITE;
    }

    @Override
    public int getViewDistance() {
        return Integer.MAX_VALUE;
    }
}


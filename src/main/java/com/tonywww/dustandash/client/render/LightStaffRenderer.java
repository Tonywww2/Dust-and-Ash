package com.tonywww.dustandash.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tonywww.dustandash.entity.LightStaffEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public final class LightStaffRenderer extends EntityRenderer<LightStaffEntity> {
    private static final int RING_SEGMENTS = 48;

    public LightStaffRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(
            LightStaffEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight) {
        poseStack.pushPose();
        poseStack.scale(1.5f, 1.5f, 1.5f);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.lightning());
        Matrix4f matrix = poseStack.last().pose();
        renderBeam(matrix, consumer, 0.11f, 0f, 3.9f, 255, 225, 112, 210);
        renderBeam(matrix, consumer, 0.045f, 0f, 3.9f, 255, 255, 235, 245);
        for (int plane = 0; plane < 4; plane++) {
            renderVerticalRing(matrix, consumer, plane * Math.PI / 4d,
                    4.45f, 0.92f, 0.72f, 255, 226, 120, 220);
            renderVerticalRing(matrix, consumer, plane * Math.PI / 4d,
                    4.45f, 0.70f, 0.65f, 255, 255, 230, 240);
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private static void renderBeam(
            Matrix4f matrix,
            VertexConsumer consumer,
            float radius,
            float bottom,
            float top,
            int red,
            int green,
            int blue,
            int alpha) {
        int sides = 8;
        for (int side = 0; side < sides; side++) {
            double first = Math.PI * 2d * side / sides;
            double second = Math.PI * 2d * (side + 1) / sides;
            float firstX = (float) Math.cos(first) * radius;
            float firstZ = (float) Math.sin(first) * radius;
            float secondX = (float) Math.cos(second) * radius;
            float secondZ = (float) Math.sin(second) * radius;
            consumer.vertex(matrix, firstX, bottom, firstZ).color(red, green, blue, alpha).endVertex();
            consumer.vertex(matrix, firstX, top, firstZ).color(red, green, blue, alpha).endVertex();
            consumer.vertex(matrix, secondX, top, secondZ).color(red, green, blue, alpha).endVertex();
            consumer.vertex(matrix, secondX, bottom, secondZ).color(red, green, blue, alpha).endVertex();
        }
    }

    private static void renderVerticalRing(
            Matrix4f matrix,
            VertexConsumer consumer,
            double planeAngle,
            float centerY,
            float outerRadius,
            float innerRadius,
            int red,
            int green,
            int blue,
            int alpha) {
        float directionX = (float) Math.cos(planeAngle);
        float directionZ = (float) Math.sin(planeAngle);
        for (int index = 0; index < RING_SEGMENTS; index++) {
            double first = Math.PI * 2d * index / RING_SEGMENTS;
            double second = Math.PI * 2d * (index + 1) / RING_SEGMENTS;
            ringVertex(matrix, consumer, first, outerRadius, centerY, directionX, directionZ,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, second, outerRadius, centerY, directionX, directionZ,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, second, innerRadius, centerY, directionX, directionZ,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, first, innerRadius, centerY, directionX, directionZ,
                    red, green, blue, alpha);
        }
    }

    private static void ringVertex(
            Matrix4f matrix,
            VertexConsumer consumer,
            double angle,
            float radius,
            float centerY,
            float directionX,
            float directionZ,
            int red,
            int green,
            int blue,
            int alpha) {
        float horizontal = (float) Math.cos(angle) * radius;
        consumer.vertex(
                        matrix,
                        directionX * horizontal,
                        centerY + (float) Math.sin(angle) * radius,
                        directionZ * horizontal)
                .color(red, green, blue, alpha)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(LightStaffEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
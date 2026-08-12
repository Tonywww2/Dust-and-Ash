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
    private static final float RING_HALF_DEPTH = 0.02f;

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
            pointVertex(matrix, consumer, firstX, bottom, firstZ, red, green, blue, alpha);
            pointVertex(matrix, consumer, firstX, top, firstZ, red, green, blue, alpha);
            pointVertex(matrix, consumer, secondX, top, secondZ, red, green, blue, alpha);
            pointVertex(matrix, consumer, secondX, bottom, secondZ, red, green, blue, alpha);
        }
        renderBeamCap(matrix, consumer, radius, bottom, false, red, green, blue, alpha);
        renderBeamCap(matrix, consumer, radius, top, true, red, green, blue, alpha);
    }

    private static void renderBeamCap(
            Matrix4f matrix,
            VertexConsumer consumer,
            float radius,
            float y,
            boolean facesUp,
            int red,
            int green,
            int blue,
            int alpha) {
        int[][] capQuads = {
                {0, 1, 2, 3},
                {0, 3, 4, 7},
                {4, 5, 6, 7}
        };
        for (int[] capQuad : capQuads) {
            if (facesUp) {
                for (int index = capQuad.length - 1; index >= 0; index--) {
                    beamCapVertex(matrix, consumer, radius, y, capQuad[index],
                            red, green, blue, alpha);
                }
            } else {
                for (int vertexIndex : capQuad) {
                    beamCapVertex(matrix, consumer, radius, y, vertexIndex,
                            red, green, blue, alpha);
                }
            }
        }
    }

    private static void beamCapVertex(
            Matrix4f matrix,
            VertexConsumer consumer,
            float radius,
            float y,
            int vertexIndex,
            int red,
            int green,
            int blue,
            int alpha) {
        double angle = Math.PI * 2d * vertexIndex / 8d;
        pointVertex(
                matrix,
                consumer,
                (float) Math.cos(angle) * radius,
                y,
                (float) Math.sin(angle) * radius,
                red,
                green,
                blue,
                alpha);
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
        float normalX = -directionZ;
        float normalZ = directionX;
        for (int index = 0; index < RING_SEGMENTS; index++) {
            double first = Math.PI * 2d * index / RING_SEGMENTS;
            double second = Math.PI * 2d * (index + 1) / RING_SEGMENTS;
            ringVertex(matrix, consumer, first, outerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, second, outerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, second, innerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, first, innerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, RING_HALF_DEPTH,
                    red, green, blue, alpha);

            ringVertex(matrix, consumer, first, innerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, -RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, second, innerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, -RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, second, outerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, -RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, first, outerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, -RING_HALF_DEPTH,
                    red, green, blue, alpha);

            ringVertex(matrix, consumer, first, outerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, -RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, second, outerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, -RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, second, outerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, first, outerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, RING_HALF_DEPTH,
                    red, green, blue, alpha);

            ringVertex(matrix, consumer, first, innerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, second, innerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, second, innerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, -RING_HALF_DEPTH,
                    red, green, blue, alpha);
            ringVertex(matrix, consumer, first, innerRadius, centerY,
                    directionX, directionZ, normalX, normalZ, -RING_HALF_DEPTH,
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
            float normalX,
            float normalZ,
            float depth,
            int red,
            int green,
            int blue,
            int alpha) {
        float horizontal = (float) Math.cos(angle) * radius;
        pointVertex(
                matrix,
                consumer,
                directionX * horizontal + normalX * depth,
                centerY + (float) Math.sin(angle) * radius,
                directionZ * horizontal + normalZ * depth,
                red,
                green,
                blue,
                alpha);
    }

    private static void pointVertex(
            Matrix4f matrix,
            VertexConsumer consumer,
            float x,
            float y,
            float z,
            int red,
            int green,
            int blue,
            int alpha) {
        consumer.vertex(matrix, x, y, z)
                .color(red, green, blue, alpha)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(LightStaffEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
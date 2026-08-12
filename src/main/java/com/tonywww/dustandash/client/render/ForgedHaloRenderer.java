package com.tonywww.dustandash.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class ForgedHaloRenderer implements ICurioRenderer {
    private static final int SEGMENTS = 48;
    private static final float SURFACE_HALF_DEPTH = 0.015f;
    private final boolean light;

    public ForgedHaloRenderer(boolean light) {
        this.light = light;
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack poseStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource bufferSource,
            int packedLight,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        poseStack.pushPose();
        ICurioRenderer.translateIfSneaking(poseStack, slotContext.entity());
        ICurioRenderer.rotateIfSneaking(poseStack, slotContext.entity());
        poseStack.translate(0d, 0.16d, 0.24d);
        poseStack.scale(2f, 2f, 2f);

        Matrix4f matrix = poseStack.last().pose();
        if (this.light) {
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.lightning());
            renderHalo(matrix, consumer, 255, 230, 120, 210);
            renderRing(matrix, consumer, 0.31f, 0.275f, 255, 255, 225, 235, 0.004f);
        } else {
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
            renderDarkHalo(matrix, poseStack.last().normal(), consumer);
        }
        poseStack.popPose();
    }

    private static void renderDarkHalo(
            Matrix4f matrix, Matrix3f normal, VertexConsumer consumer) {
        renderDarkHaloLayer(matrix, normal, consumer, -SURFACE_HALF_DEPTH);
        renderDarkHaloLayer(matrix, normal, consumer, SURFACE_HALF_DEPTH);
    }

    private static void renderDarkHaloLayer(
            Matrix4f matrix,
            Matrix3f normal,
            VertexConsumer consumer,
            float z) {
        renderLineRing(matrix, normal, consumer, 0.59f, z, 24, 20, 28);
        renderLineRing(matrix, normal, consumer, 0.56f, z, 48, 38, 52);
        renderLineRing(matrix, normal, consumer, 0.45f, z, 32, 26, 36);
        renderLineRing(matrix, normal, consumer, 0.31f, z, 72, 54, 76);
        for (int index = 0; index < 12; index++) {
            double angle = Math.PI * 2d * index / 12d;
            float directionX = (float) Math.cos(angle);
            float directionY = (float) Math.sin(angle);
            renderLine(matrix, normal, consumer,
                    directionX * 0.31f, directionY * 0.31f, z,
                    directionX * 0.56f, directionY * 0.56f, z,
                    42, 32, 46);
        }
    }

    private static void renderLineRing(
            Matrix4f matrix,
            Matrix3f normal,
            VertexConsumer consumer,
            float radius,
            float z,
            int red,
            int green,
            int blue) {
        for (int index = 0; index < SEGMENTS; index++) {
            double first = Math.PI * 2d * index / SEGMENTS;
            double second = Math.PI * 2d * (index + 1) / SEGMENTS;
            renderLine(matrix, normal, consumer,
                    (float) Math.cos(first) * radius,
                    (float) Math.sin(first) * radius,
                    z,
                    (float) Math.cos(second) * radius,
                    (float) Math.sin(second) * radius,
                    z,
                    red, green, blue);
        }
    }

    private static void renderLine(
            Matrix4f matrix,
            Matrix3f normal,
            VertexConsumer consumer,
            float firstX,
            float firstY,
            float firstZ,
            float secondX,
            float secondY,
            float secondZ,
            int red,
            int green,
            int blue) {
        float directionX = secondX - firstX;
        float directionY = secondY - firstY;
        float directionZ = secondZ - firstZ;
        float directionLength = (float) Math.sqrt(
                directionX * directionX
                        + directionY * directionY
                        + directionZ * directionZ);
        if (directionLength > 0f) {
            directionX /= directionLength;
            directionY /= directionLength;
            directionZ /= directionLength;
        }
        lineVertex(matrix, normal, consumer,
                firstX, firstY, firstZ,
                directionX, directionY, directionZ,
                red, green, blue);
        lineVertex(matrix, normal, consumer,
                secondX, secondY, secondZ,
                directionX, directionY, directionZ,
                red, green, blue);
    }

    private static void lineVertex(
            Matrix4f matrix,
            Matrix3f normal,
            VertexConsumer consumer,
            float x,
            float y,
            float z,
            float directionX,
            float directionY,
            float directionZ,
            int red,
            int green,
            int blue) {
        consumer.vertex(matrix, x, y, z)
                .color(red, green, blue, 255)
                .normal(normal, directionX, directionY, directionZ)
                .endVertex();
    }

    private static void renderHalo(
            Matrix4f matrix, VertexConsumer consumer, int red, int green, int blue, int alpha) {
        renderRing(matrix, consumer, 0.59f, 0.52f, red, green, blue, alpha, 0f);
        renderRing(matrix, consumer, 0.45f, 0.425f, red, green, blue, alpha, 0.002f);
        for (int index = 0; index < 12; index++) {
            double angle = Math.PI * 2d * index / 12d;
            renderSpoke(matrix, consumer, angle, 0.31f, 0.52f, 0.014f,
                    red, green, blue, alpha, 0.001f);
        }
    }

    private static void renderRing(
            Matrix4f matrix,
            VertexConsumer consumer,
            float outerRadius,
            float innerRadius,
            int red,
            int green,
            int blue,
            int alpha,
            float centerZ) {
        float frontZ = centerZ + SURFACE_HALF_DEPTH;
        float backZ = centerZ - SURFACE_HALF_DEPTH;
        for (int index = 0; index < SEGMENTS; index++) {
            double first = Math.PI * 2d * index / SEGMENTS;
            double second = Math.PI * 2d * (index + 1) / SEGMENTS;
            vertex(matrix, consumer, first, outerRadius, frontZ, red, green, blue, alpha);
            vertex(matrix, consumer, second, outerRadius, frontZ, red, green, blue, alpha);
            vertex(matrix, consumer, second, innerRadius, frontZ, red, green, blue, alpha);
            vertex(matrix, consumer, first, innerRadius, frontZ, red, green, blue, alpha);

            vertex(matrix, consumer, first, innerRadius, backZ, red, green, blue, alpha);
            vertex(matrix, consumer, second, innerRadius, backZ, red, green, blue, alpha);
            vertex(matrix, consumer, second, outerRadius, backZ, red, green, blue, alpha);
            vertex(matrix, consumer, first, outerRadius, backZ, red, green, blue, alpha);

            vertex(matrix, consumer, first, outerRadius, backZ, red, green, blue, alpha);
            vertex(matrix, consumer, second, outerRadius, backZ, red, green, blue, alpha);
            vertex(matrix, consumer, second, outerRadius, frontZ, red, green, blue, alpha);
            vertex(matrix, consumer, first, outerRadius, frontZ, red, green, blue, alpha);

            vertex(matrix, consumer, first, innerRadius, frontZ, red, green, blue, alpha);
            vertex(matrix, consumer, second, innerRadius, frontZ, red, green, blue, alpha);
            vertex(matrix, consumer, second, innerRadius, backZ, red, green, blue, alpha);
            vertex(matrix, consumer, first, innerRadius, backZ, red, green, blue, alpha);
        }
    }

    private static void renderSpoke(
            Matrix4f matrix,
            VertexConsumer consumer,
            double angle,
            float innerRadius,
            float outerRadius,
            float halfWidth,
            int red,
            int green,
            int blue,
            int alpha,
            float centerZ) {
        float directionX = (float) Math.cos(angle);
        float directionY = (float) Math.sin(angle);
        float perpendicularX = -directionY * halfWidth;
        float perpendicularY = directionX * halfWidth;
        float innerLeftX = directionX * innerRadius + perpendicularX;
        float innerLeftY = directionY * innerRadius + perpendicularY;
        float outerLeftX = directionX * outerRadius + perpendicularX;
        float outerLeftY = directionY * outerRadius + perpendicularY;
        float outerRightX = directionX * outerRadius - perpendicularX;
        float outerRightY = directionY * outerRadius - perpendicularY;
        float innerRightX = directionX * innerRadius - perpendicularX;
        float innerRightY = directionY * innerRadius - perpendicularY;
        float frontZ = centerZ + SURFACE_HALF_DEPTH;
        float backZ = centerZ - SURFACE_HALF_DEPTH;

        quad(matrix, consumer,
                innerRightX, innerRightY, frontZ,
                outerRightX, outerRightY, frontZ,
                outerLeftX, outerLeftY, frontZ,
                innerLeftX, innerLeftY, frontZ,
                red, green, blue, alpha);
        quad(matrix, consumer,
                innerLeftX, innerLeftY, backZ,
                outerLeftX, outerLeftY, backZ,
                outerRightX, outerRightY, backZ,
                innerRightX, innerRightY, backZ,
                red, green, blue, alpha);
        quad(matrix, consumer,
                innerLeftX, innerLeftY, frontZ,
                outerLeftX, outerLeftY, frontZ,
                outerLeftX, outerLeftY, backZ,
                innerLeftX, innerLeftY, backZ,
                red, green, blue, alpha);
        quad(matrix, consumer,
                outerRightX, outerRightY, frontZ,
                innerRightX, innerRightY, frontZ,
                innerRightX, innerRightY, backZ,
                outerRightX, outerRightY, backZ,
                red, green, blue, alpha);
        quad(matrix, consumer,
                outerLeftX, outerLeftY, frontZ,
                outerRightX, outerRightY, frontZ,
                outerRightX, outerRightY, backZ,
                outerLeftX, outerLeftY, backZ,
                red, green, blue, alpha);
        quad(matrix, consumer,
                innerRightX, innerRightY, frontZ,
                innerLeftX, innerLeftY, frontZ,
                innerLeftX, innerLeftY, backZ,
                innerRightX, innerRightY, backZ,
                red, green, blue, alpha);
    }

    private static void quad(
            Matrix4f matrix,
            VertexConsumer consumer,
            float firstX,
            float firstY,
            float firstZ,
            float secondX,
            float secondY,
            float secondZ,
            float thirdX,
            float thirdY,
            float thirdZ,
            float fourthX,
            float fourthY,
            float fourthZ,
            int red,
            int green,
            int blue,
            int alpha) {
        pointVertex(matrix, consumer, firstX, firstY, firstZ, red, green, blue, alpha);
        pointVertex(matrix, consumer, secondX, secondY, secondZ, red, green, blue, alpha);
        pointVertex(matrix, consumer, thirdX, thirdY, thirdZ, red, green, blue, alpha);
        pointVertex(matrix, consumer, fourthX, fourthY, fourthZ, red, green, blue, alpha);
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

    private static void vertex(
            Matrix4f matrix,
            VertexConsumer consumer,
            double angle,
            float radius,
            float z,
            int red,
            int green,
            int blue,
            int alpha) {
        consumer.vertex(
                        matrix,
                        (float) Math.cos(angle) * radius,
                        (float) Math.sin(angle) * radius,
                        z)
                .color(red, green, blue, alpha)
                .endVertex();
    }
}
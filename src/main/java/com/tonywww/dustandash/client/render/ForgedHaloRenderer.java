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
        renderLineRing(matrix, normal, consumer, 0.59f, 24, 20, 28);
        renderLineRing(matrix, normal, consumer, 0.56f, 48, 38, 52);
        renderLineRing(matrix, normal, consumer, 0.45f, 32, 26, 36);
        renderLineRing(matrix, normal, consumer, 0.31f, 72, 54, 76);
        for (int index = 0; index < 12; index++) {
            double angle = Math.PI * 2d * index / 12d;
            float directionX = (float) Math.cos(angle);
            float directionY = (float) Math.sin(angle);
            lineVertex(matrix, normal, consumer,
                    directionX * 0.31f, directionY * 0.31f, 0.001f, 42, 32, 46);
            lineVertex(matrix, normal, consumer,
                    directionX * 0.56f, directionY * 0.56f, 0.001f, 42, 32, 46);
        }
    }

    private static void renderLineRing(
            Matrix4f matrix,
            Matrix3f normal,
            VertexConsumer consumer,
            float radius,
            int red,
            int green,
            int blue) {
        for (int index = 0; index < SEGMENTS; index++) {
            double first = Math.PI * 2d * index / SEGMENTS;
            double second = Math.PI * 2d * (index + 1) / SEGMENTS;
            lineVertex(matrix, normal, consumer,
                    (float) Math.cos(first) * radius,
                    (float) Math.sin(first) * radius,
                    0f,
                    red, green, blue);
            lineVertex(matrix, normal, consumer,
                    (float) Math.cos(second) * radius,
                    (float) Math.sin(second) * radius,
                    0f,
                    red, green, blue);
        }
    }

    private static void lineVertex(
            Matrix4f matrix,
            Matrix3f normal,
            VertexConsumer consumer,
            float x,
            float y,
            float z,
            int red,
            int green,
            int blue) {
        consumer.vertex(matrix, x, y, z)
                .color(red, green, blue, 255)
                .normal(normal, 0f, 0f, 1f)
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
            float z) {
        for (int index = 0; index < SEGMENTS; index++) {
            double first = Math.PI * 2d * index / SEGMENTS;
            double second = Math.PI * 2d * (index + 1) / SEGMENTS;
            vertex(matrix, consumer, first, outerRadius, z, red, green, blue, alpha);
            vertex(matrix, consumer, second, outerRadius, z, red, green, blue, alpha);
            vertex(matrix, consumer, second, innerRadius, z, red, green, blue, alpha);
            vertex(matrix, consumer, first, innerRadius, z, red, green, blue, alpha);
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
            float z) {
        float directionX = (float) Math.cos(angle);
        float directionY = (float) Math.sin(angle);
        float perpendicularX = -directionY * halfWidth;
        float perpendicularY = directionX * halfWidth;
        consumer.vertex(matrix,
                        directionX * innerRadius + perpendicularX,
                        directionY * innerRadius + perpendicularY,
                        z)
                .color(red, green, blue, alpha).endVertex();
        consumer.vertex(matrix,
                        directionX * outerRadius + perpendicularX,
                        directionY * outerRadius + perpendicularY,
                        z)
                .color(red, green, blue, alpha).endVertex();
        consumer.vertex(matrix,
                        directionX * outerRadius - perpendicularX,
                        directionY * outerRadius - perpendicularY,
                        z)
                .color(red, green, blue, alpha).endVertex();
        consumer.vertex(matrix,
                        directionX * innerRadius - perpendicularX,
                        directionY * innerRadius - perpendicularY,
                        z)
                .color(red, green, blue, alpha).endVertex();
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
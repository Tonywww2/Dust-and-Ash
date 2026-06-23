package com.tonywww.dustandash.cthulhu.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.cthulhu.client.CthulhuClientRenderState;
import com.tonywww.dustandash.cthulhu.client.CthulhuRenderMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Optional;
import java.util.Random;

/**
 * Replaces the vanilla render of tagged minions with a chaotic, glitch-corrupted
 * visualization. The intent is that the original silhouette is almost lost in the
 * distortion: jittering ghost copies, fractured wireframes and a swarm of corrupted glyphs.
 */
@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, value = Dist.CLIENT)
public final class CthulhuClientRenderEvents {

    /** Cthulhu teal accent used as the base corruption color. */
    private static final float[] TEAL = {0.10F, 0.95F, 0.78F};
    private static final char[] GLYPHS = "CTHULHUSTATIC0123456789/\\#@%&*<>{}[]!?".toCharArray();
    /** Sub-tick flicker resolution: how many distinct chaos states per game tick. */
    private static final int FLICKER_STEPS = 4;

    private CthulhuClientRenderEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static <T extends LivingEntity, M extends EntityModel<T>> void onLivingRenderPre(RenderLivingEvent.Pre<T, M> event) {
        @SuppressWarnings("unchecked")
        T entity = (T) event.getEntity();
        Optional<CthulhuRenderMode> mode = CthulhuClientRenderState.getMode(entity.getId());
        if (mode.isEmpty()) {
            return;
        }

        // Suppress the clean vanilla model so the original outline is buried in the distortion.
        event.setCanceled(true);

        Random rng = chaosRandom(entity, event.getPartialTick());
        switch (mode.get()) {
            case NOISE -> renderNoise(event, entity, rng);
            case WIREFRAME -> renderWireframe(event, entity, rng);
            case TEXT_STATIC -> renderTextStatic(event, entity, rng);
        }
    }

    // ------------------------------------------------------------------
    // NOISE: datamosh / glitch. Many distorted, jittering, color-split copies of the model.
    // ------------------------------------------------------------------
    private static <T extends LivingEntity, M extends EntityModel<T>> void renderNoise(RenderLivingEvent.Pre<T, M> event, T entity, Random rng) {
        PoseStack poseStack = event.getPoseStack();
        ResourceLocation texture = event.getRenderer().getTextureLocation(entity);
        RenderType type = RenderType.entityTranslucent(texture);
        MultiBufferSource bufferSource = event.getMultiBufferSource();
        EntityModel<T> model = event.getRenderer().getModel();

        int copies = 10;
        for (int i = 0; i < copies; i++) {
            poseStack.pushPose();
            // Positional jitter.
            poseStack.translate(
                    (rng.nextFloat() - 0.5F) * 0.55F,
                    (rng.nextFloat() - 0.5F) * 0.55F,
                    (rng.nextFloat() - 0.5F) * 0.55F
            );
            // Non-uniform scale + rotation = heavy melting distortion.
            poseStack.scale(
                    0.75F + rng.nextFloat() * 0.7F,
                    0.65F + rng.nextFloat() * 0.85F,
                    0.75F + rng.nextFloat() * 0.7F
            );
            poseStack.mulPose(Axis.YP.rotationDegrees((rng.nextFloat() - 0.5F) * 70.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees((rng.nextFloat() - 0.5F) * 35.0F));
            applyShear(poseStack, (rng.nextFloat() - 0.5F) * 0.6F, (rng.nextFloat() - 0.5F) * 0.4F);

            float[] color = glitchColor(rng);
            float alpha = 0.28F + rng.nextFloat() * 0.42F;
            model.renderToBuffer(
                    poseStack,
                    bufferSource.getBuffer(type),
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    color[0], color[1], color[2], alpha
            );
            poseStack.popPose();
        }

        // RGB channel split: a red copy and a cyan copy pulled apart horizontally.
        float split = 0.18F + rng.nextFloat() * 0.12F;
        renderColorSplitCopy(poseStack, model, bufferSource, type, split, 0.0F, 1.0F, 0.05F, 0.05F);
        renderColorSplitCopy(poseStack, model, bufferSource, type, -split, 0.0F, 0.05F, 1.0F, 0.95F);
    }

    private static <T extends LivingEntity> void renderColorSplitCopy(PoseStack poseStack, EntityModel<T> model,
                                                                      MultiBufferSource bufferSource, RenderType type,
                                                                      float dx, float dy, float r, float g, float b) {
        poseStack.pushPose();
        poseStack.translate(dx, dy, 0.0D);
        model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(type),
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                r, g, b, 0.35F
        );
        poseStack.popPose();
    }

    // ------------------------------------------------------------------
    // WIREFRAME: fractured cage. Many jittered, scaled, rotated boxes + chaotic scatter lines.
    // ------------------------------------------------------------------
    private static <T extends LivingEntity, M extends EntityModel<T>> void renderWireframe(RenderLivingEvent.Pre<T, M> event, T entity, Random rng) {
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getMultiBufferSource();
        VertexConsumer lines = bufferSource.getBuffer(RenderType.lines());

        float halfWidth = Math.max(0.25F, entity.getBbWidth() * 0.5F);
        float height = Math.max(0.5F, entity.getBbHeight());
        float center = height * 0.5F;

        // Fractured boxes at random scales / orientations.
        int boxes = 16;
        for (int i = 0; i < boxes; i++) {
            poseStack.pushPose();
            poseStack.translate(0.0D, center, 0.0D);
            poseStack.translate(
                    (rng.nextFloat() - 0.5F) * halfWidth,
                    (rng.nextFloat() - 0.5F) * height * 0.5F,
                    (rng.nextFloat() - 0.5F) * halfWidth
            );
            poseStack.mulPose(Axis.YP.rotationDegrees(rng.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees((rng.nextFloat() - 0.5F) * 90.0F));
            float scale = 0.25F + rng.nextFloat() * 1.1F;
            float bw = halfWidth * scale;
            float bh = height * 0.5F * scale;
            float[] color = glitchColor(rng);
            float alpha = 0.4F + rng.nextFloat() * 0.55F;
            LevelRenderer.renderLineBox(
                    poseStack, lines,
                    -bw, -bh, -bw, bw, bh, bw,
                    color[0], color[1], color[2], alpha
            );
            poseStack.popPose();
        }

        // Chaotic scatter lines criss-crossing the body volume.
        Matrix4f pose = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        int segments = 26;
        for (int i = 0; i < segments; i++) {
            float x1 = (rng.nextFloat() - 0.5F) * halfWidth * 2.4F;
            float y1 = rng.nextFloat() * height;
            float z1 = (rng.nextFloat() - 0.5F) * halfWidth * 2.4F;
            float x2 = (rng.nextFloat() - 0.5F) * halfWidth * 2.4F;
            float y2 = rng.nextFloat() * height;
            float z2 = (rng.nextFloat() - 0.5F) * halfWidth * 2.4F;
            float[] color = glitchColor(rng);
            float alpha = 0.35F + rng.nextFloat() * 0.5F;
            addLine(lines, pose, normal, x1, y1, z1, x2, y2, z2, color, alpha);
        }
    }

    private static void addLine(VertexConsumer buffer, Matrix4f pose, Matrix3f normal,
                                float x1, float y1, float z1, float x2, float y2, float z2,
                                float[] color, float alpha) {
        float nx = x2 - x1;
        float ny = y2 - y1;
        float nz = z2 - z1;
        float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len < 1.0E-4F) {
            ny = 1.0F;
            len = 1.0F;
        }
        nx /= len;
        ny /= len;
        nz /= len;
        buffer.vertex(pose, x1, y1, z1).color(color[0], color[1], color[2], alpha).normal(normal, nx, ny, nz).endVertex();
        buffer.vertex(pose, x2, y2, z2).color(color[0], color[1], color[2], alpha).normal(normal, nx, ny, nz).endVertex();
    }

    // ------------------------------------------------------------------
    // TEXT_STATIC: a swarm of corrupted glyphs engulfing the entity, plus a faint ghost shell.
    // ------------------------------------------------------------------
    private static <T extends LivingEntity, M extends EntityModel<T>> void renderTextStatic(RenderLivingEvent.Pre<T, M> event, T entity, Random rng) {
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getMultiBufferSource();

        // Faint distorted ghost so a hint of the creature remains under the text.
        EntityModel<T> model = event.getRenderer().getModel();
        RenderType ghostType = RenderType.entityTranslucent(event.getRenderer().getTextureLocation(entity));
        for (int i = 0; i < 3; i++) {
            poseStack.pushPose();
            poseStack.translate((rng.nextFloat() - 0.5F) * 0.3F, (rng.nextFloat() - 0.5F) * 0.3F, (rng.nextFloat() - 0.5F) * 0.3F);
            poseStack.scale(0.95F + rng.nextFloat() * 0.2F, 0.95F + rng.nextFloat() * 0.2F, 0.95F + rng.nextFloat() * 0.2F);
            model.renderToBuffer(poseStack, bufferSource.getBuffer(ghostType), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                    0.1F, 0.6F, 0.5F, 0.18F);
            poseStack.popPose();
        }

        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        Quaternionf cameraOrientation = minecraft.getEntityRenderDispatcher().cameraOrientation();
        float halfWidth = Math.max(0.3F, entity.getBbWidth() * 0.6F);
        float height = Math.max(0.6F, entity.getBbHeight());

        int glyphCount = 34;
        for (int i = 0; i < glyphCount; i++) {
            char glyph = GLYPHS[rng.nextInt(GLYPHS.length)];
            String text = String.valueOf(glyph);

            poseStack.pushPose();
            poseStack.translate(
                    (rng.nextFloat() - 0.5F) * halfWidth * 2.6F,
                    rng.nextFloat() * (height + 0.4F),
                    (rng.nextFloat() - 0.5F) * halfWidth * 2.6F
            );
            poseStack.mulPose(cameraOrientation);
            float glyphScale = 0.014F + rng.nextFloat() * 0.03F;
            poseStack.scale(-glyphScale, -glyphScale, glyphScale);

            int color = glitchTextColor(rng);
            font.drawInBatch(
                    text,
                    -font.width(text) / 2.0F,
                    0.0F,
                    color,
                    false,
                    poseStack.last().pose(),
                    bufferSource,
                    Font.DisplayMode.SEE_THROUGH,
                    0,
                    LightTexture.FULL_BRIGHT
            );
            poseStack.popPose();
        }
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    /** Per-frame chaotic RNG: stable across an entity but reshuffled several times per tick. */
    private static Random chaosRandom(LivingEntity entity, float partialTick) {
        long tick = entity.level().getGameTime();
        int sub = (int) (partialTick * FLICKER_STEPS);
        long seed = (tick * FLICKER_STEPS + sub) * 2654435761L + entity.getId() * 40503L;
        return new Random(seed);
    }

    /** Applies an X/Y shear to the current pose for extra distortion. */
    private static void applyShear(PoseStack poseStack, float shearXY, float shearZY) {
        Matrix4f shear = new Matrix4f();
        // column 1 (Y) contributions into X and Z rows.
        shear.m10(shearXY);
        shear.m12(shearZY);
        poseStack.last().pose().mul(shear);
    }

    /** Mostly corrupted teal with occasional magenta / red / white glitch flashes. */
    private static float[] glitchColor(Random rng) {
        float roll = rng.nextFloat();
        if (roll < 0.55F) {
            float v = 0.6F + rng.nextFloat() * 0.4F;
            return new float[]{TEAL[0] * v, TEAL[1] * v, TEAL[2] * v};
        } else if (roll < 0.74F) {
            return new float[]{0.05F, 0.8F + rng.nextFloat() * 0.2F, 1.0F};
        } else if (roll < 0.88F) {
            return new float[]{1.0F, 0.05F, 0.7F + rng.nextFloat() * 0.3F};
        } else if (roll < 0.96F) {
            return new float[]{1.0F, 0.1F, 0.1F};
        }
        return new float[]{1.0F, 1.0F, 1.0F};
    }

    private static int glitchTextColor(Random rng) {
        int alpha = (int) ((0.45F + rng.nextFloat() * 0.55F) * 255.0F) << 24;
        float[] c = glitchColor(rng);
        int r = (int) (c[0] * 255.0F);
        int g = (int) (c[1] * 255.0F);
        int b = (int) (c[2] * 255.0F);
        return alpha | (r << 16) | (g << 8) | b;
    }
}

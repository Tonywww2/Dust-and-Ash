package com.tonywww.dustandash.cthulhu.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tonywww.dustandash.cthulhu.entity.CthulhuBossPhase1Entity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuGraphemeEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuLawFieldEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuPillarEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuStormGolemEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;

public class CthulhuTextEntityRenderer<T extends Entity> extends EntityRenderer<T> {

    public CthulhuTextEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0f;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0d, entity.getBbHeight() + 0.25d, 0.0d);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(-0.035f, -0.035f, 0.035f);

        String label = labelFor(entity);
        Font font = this.getFont();
        Matrix4f matrix = poseStack.last().pose();
        float x = -font.width(label) / 2.0f;
        font.drawInBatch(label, x, 0.0f, colorFor(entity), false, matrix, buffer, Font.DisplayMode.SEE_THROUGH, 0, packedLight);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    private static String labelFor(Entity entity) {
        if (entity instanceof CthulhuBossPhase1Entity boss) {
            return "AZATHOTH / " + Math.max(0, Math.round(boss.getHealth())) + "/" + Math.round(boss.getMaxHealth());
        }
        if (entity instanceof CthulhuStormGolemEntity stormGolem) {
            if (stormGolem.isAbsoluteDefenseActive()) {
                return "TEXT STORM / EXIST " + stormGolem.getAbsoluteDefenseTicksLeft();
            }
            return "TEXT STORM / " + Math.max(0, Math.round(stormGolem.getHealth())) + "/" + Math.round(stormGolem.getMaxHealth());
        }
        if (entity instanceof CthulhuGraphemeEntity grapheme) {
            return String.valueOf(grapheme.getLetter());
        }
        if (entity instanceof CthulhuPillarEntity pillar) {
            if (pillar.isLinkBroken()) {
                return pillar.getPillarType().name() + " /BREAK " + pillar.getBreakTicksLeft();
            }
            if (pillar.isCustomInvulnerable()) {
                return pillar.getPillarType().name() + " [" + pillar.getCustomInvulTicksLeft() + "]";
            }
            if (pillar.getOutputWindowTicksLeft() > 0) {
                return pillar.getPillarType().name() + " *";
            }
            return pillar.getPillarType().name();
        }
        if (entity instanceof CthulhuLawFieldEntity lawField) {
            return lawField.getFieldType().name() + " [" + lawField.getDropLetter() + "]";
        }
        return "PILLAR";
    }

    private static int colorFor(Entity entity) {
        if (entity instanceof CthulhuGraphemeEntity) {
            return 0xFFFFFFFF;
        }
        if (entity instanceof CthulhuBossPhase1Entity) {
            return 0xFFB8B8B8;
        }
        if (entity instanceof CthulhuStormGolemEntity stormGolem && stormGolem.isAbsoluteDefenseActive()) {
            return 0xFFFF5555;
        }
        if (entity instanceof CthulhuStormGolemEntity) {
            return 0xFFFFFFFF;
        }
        if (entity instanceof CthulhuPillarEntity pillar && pillar.isLinkBroken()) {
            return 0xFFFF5555;
        }
        if (entity instanceof CthulhuPillarEntity pillar && pillar.isCustomInvulnerable()) {
            return 0xFF777777;
        }
        if (entity instanceof CthulhuLawFieldEntity) {
            return 0xFFBDBDBD;
        }
        return 0xFFE8E8E8;
    }
}

package net.blazinblaze.happyghastmod.entity.renderer;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.attachment.HappyGhastClientAttachments;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.blazinblaze.happyghastmod.entity.model.HappyGhastHarnessModel;
import net.blazinblaze.happyghastmod.entity.model.HappyGhastModel;
import net.blazinblaze.happyghastmod.entity.model.HappyGhastModelLayers;
import net.blazinblaze.happyghastmod.entity.state.HappyGhastRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.feature.WolfArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BlazeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.render.entity.state.GhastEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class HappyGhastRenderer extends MobEntityRenderer<HappyGhast, HappyGhastRenderState, HappyGhastModel> {
    private static final Identifier TEXTURE = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast.png");
    private static final Identifier TEXTURE_AGED = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast_aged.png");
    private static final Identifier TEXTURE_VERY_AGED = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast_very_aged.png");
    private static final Identifier TEXTURE_SAD = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast_sad.png");
    private static final Identifier TEXTURE_AGED_SAD = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast_aged_sad.png");
    private static final Identifier TEXTURE_VERY_AGED_SAD = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast_very_aged_sad.png");
    private static final Identifier SHOOTING_TEXTURE = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast_shooting.png");
    private static final Identifier SHOOTING_TEXTURE_AGED = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast_shooting_aged.png");
    private static final Identifier SHOOTING_TEXTURE_VERY_AGED = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast_shooting_very_aged.png");
    private static final Identifier SHOOTING_TEXTURE_SAD = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast_shooting_sad.png");
    private static final Identifier SHOOTING_TEXTURE_AGED_SAD = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast_shooting_aged_sad.png");
    private static final Identifier SHOOTING_TEXTURE_VERY_AGED_SAD = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/happy_ghast/happy_ghast_shooting_very_aged_sad.png");

    public HappyGhastRenderer(EntityRendererFactory.Context context) {
        //super(context, new HappyGhastModel(context.getPart(EntityModelLayers.GHAST)), 1.5F);
        super(context, new HappyGhastModel(context.getPart(HappyGhastModelLayers.HAPPY_GHAST)), 1.5F);
        this.addFeature(new HarnessFeatureRenderer(this, context.getEntityModels(), context.getEquipmentRenderer()));
    }

    public Identifier getTexture(HappyGhastRenderState happyGhastRenderState) {
        if(happyGhastRenderState.shooting) {
            if(happyGhastRenderState.aged == 0) {
                if(happyGhastRenderState.inNether) {
                    return SHOOTING_TEXTURE_SAD;
                }else {
                    return SHOOTING_TEXTURE;
                }
            }else if(happyGhastRenderState.aged == 1) {
                if(happyGhastRenderState.inNether) {
                    return SHOOTING_TEXTURE_AGED_SAD;
                }else {
                    return SHOOTING_TEXTURE_AGED;
                }
            }else if(happyGhastRenderState.aged == 2) {
                if(happyGhastRenderState.inNether) {
                    return SHOOTING_TEXTURE_VERY_AGED_SAD;
                }else {
                    return SHOOTING_TEXTURE_VERY_AGED;
                }
            }
        }else {
            if(happyGhastRenderState.aged == 0) {
                if(happyGhastRenderState.inNether) {
                    return TEXTURE_SAD;
                }else {
                    return TEXTURE;
                }
            }else if(happyGhastRenderState.aged == 1) {
                if(happyGhastRenderState.inNether) {
                    return TEXTURE_AGED_SAD;
                }else {
                    return TEXTURE_AGED;
                }
            }else if(happyGhastRenderState.aged == 2) {
                if(happyGhastRenderState.inNether) {
                    return TEXTURE_VERY_AGED_SAD;
                }else {
                    return TEXTURE_VERY_AGED;
                }
            }
        }
        return happyGhastRenderState.shooting ? SHOOTING_TEXTURE : TEXTURE;
    }

    public HappyGhastRenderState createRenderState() {
        return new HappyGhastRenderState();
    }

    public void updateRenderState(HappyGhast happyGhast, HappyGhastRenderState happyGhastRenderState, float f) {
        super.updateRenderState(happyGhast, happyGhastRenderState, f);
        happyGhastRenderState.saddleStack = happyGhast.getEquippedStack(EquipmentSlot.SADDLE).copy();
        happyGhastRenderState.isBeingRidden = happyGhast.hasControllingPassenger();
        happyGhastRenderState.shooting = happyGhast.isShooting();
        happyGhastRenderState.isCrowned = happyGhast.getRoyalty();
        happyGhastRenderState.aged = happyGhast.getAgedValue();
        happyGhastRenderState.inNether = happyGhast.getInNether();
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player.getAttachedOrCreate(HappyGhastClientAttachments.SHOULD_RENDER_CROWN, HappyGhastClientAttachments.SHOULD_RENDER_CROWN.initializer())) {
            happyGhastRenderState.shouldShowCrownGhast = true;
        }else {
            if(player.getVehicle() == happyGhast) {
                happyGhastRenderState.shouldShowCrownGhast = false;
            } else {
                happyGhastRenderState.shouldShowCrownGhast = true;
            }
        }
    }
}

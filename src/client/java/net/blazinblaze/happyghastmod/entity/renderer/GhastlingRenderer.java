package net.blazinblaze.happyghastmod.entity.renderer;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.entity.custom.Ghastling;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.blazinblaze.happyghastmod.entity.model.GhastlingModel;
import net.blazinblaze.happyghastmod.entity.state.GhastlingRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.render.entity.state.GhastEntityRenderState;
import net.minecraft.util.Identifier;

public class GhastlingRenderer extends MobEntityRenderer<Ghastling, GhastlingRenderState, GhastlingModel> {
    private static final Identifier TEXTURE = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/ghastling/ghastling.png");
    private static final Identifier TEXTURE_SAD = Identifier.of(HappyGhastMod.MOD_ID, "textures/entity/ghastling/ghastling_sad.png");

    public GhastlingRenderer(EntityRendererFactory.Context context) {
        super(context, new GhastlingModel(context.getPart(EntityModelLayers.GHAST)), 0.75F);
    }

    public Identifier getTexture(GhastlingRenderState ghastEntityRenderState) {
        if(ghastEntityRenderState.inNether) {
            return TEXTURE_SAD;
        }
        return TEXTURE;
    }

    public GhastlingRenderState createRenderState() {
        return new GhastlingRenderState();
    }

    public void updateRenderState(Ghastling ghastEntity, GhastlingRenderState ghastlingRenderState, float f) {
        super.updateRenderState(ghastEntity, ghastlingRenderState, f);
        ghastlingRenderState.inNether = ghastEntity.getInNether();
    }}

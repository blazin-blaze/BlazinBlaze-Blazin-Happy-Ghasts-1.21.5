package net.blazinblaze.happyghastmod.entity.model;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.attachment.HappyGhastClientAttachments;
import net.blazinblaze.happyghastmod.entity.state.HappyGhastRenderState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelTransformer;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

public class HappyGhastHarnessModel extends EntityModel<HappyGhastRenderState> {
    private final ModelPart body;
    private final ModelPart gogglesDown;
    private final ModelPart gogglesUp;
    private final ModelPart crown;
    private final ModelPart speedUpgrade;
    private final ModelPart strengthUpgrade;
    private final ModelPart heartUpgrade;
    private final ModelPart fireballUpgrade;
    private final ModelPart royaltyUpgrade;

    public HappyGhastHarnessModel(ModelPart root) {
        super(root);
        this.gogglesDown = root.getChild("gogglesDown");
        this.gogglesUp = root.getChild("gogglesUp");
        this.body = root.getChild("body");
        this.crown = root.getChild("crown");
        this.speedUpgrade = root.getChild("speedUpgrade");
        this.strengthUpgrade = root.getChild("strengthUpgrade");
        this.heartUpgrade = root.getChild("heartUpgrade");
        this.fireballUpgrade = root.getChild("fireballUpgrade");
        this.royaltyUpgrade = root.getChild("royaltyUpgrade");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData gogglesDown = modelPartData.addChild("gogglesDown", ModelPartBuilder.create().uv(49, 34).cuboid(-9.0F, -25.0F, -8.0F, 18.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 37F, 0.0F));

        ModelPartData gogglesUp = modelPartData.addChild("gogglesUp", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 34.0F, 0.0F));

        ModelPartData gogglesUp_r1 = gogglesUp.addChild("gogglesUp_r1", ModelPartBuilder.create().uv(0, 33).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -20.5F, -9.0F, -0.5236F, 0.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -28.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 37F, 0.0F));

        ModelPartData crown = modelPartData.addChild("crown", ModelPartBuilder.create().uv(0, 46).cuboid(-8.0F, -30.0F, -8.0F, 16.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 37.0F, 0.0F));

        ModelPartData speedUpgrade = modelPartData.addChild("speedUpgrade", ModelPartBuilder.create().uv(1, 68).cuboid(8.0F, -22.0F, -3.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 37.0F, 0.0F));

        ModelPartData strengthUpgrade = modelPartData.addChild("strengthUpgrade", ModelPartBuilder.create().uv(11, 68).cuboid(8.0F, -22.0F, -1.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 37.0F, 0.0F));

        ModelPartData heartUpgrade = modelPartData.addChild("heartUpgrade", ModelPartBuilder.create().uv(25, 68).cuboid(8.0F, -22.0F, 1.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 37.0F, 0.0F));

        ModelPartData fireballUpgrade = modelPartData.addChild("fireballUpgrade", ModelPartBuilder.create().uv(38, 68).cuboid(8.0F, -22.0F, 3.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 37.0F, 0.0F));

        ModelPartData royaltyUpgrade = modelPartData.addChild("royaltyUpgrade", ModelPartBuilder.create().uv(51, 68).cuboid(8.0F, -22.0F, 5.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 37.0F, 0.0F));

        return TexturedModelData.of(modelData, 128, 128);
    }

    public static @NotNull TexturedModelData createAdultBodyLayer() {
        return getTexturedModelData().transform(ModelTransformer.scaling(4.5F));
    }

    @Override
    public void setAngles(HappyGhastRenderState happyGhastRenderState) {
        super.setAngles(happyGhastRenderState);
        if(happyGhastRenderState.isBeingRidden) {
            this.gogglesUp.visible = false;
            this.gogglesDown.visible = true;
        } else {
            this.gogglesUp.visible = true;
            this.gogglesDown.visible = false;
        }
        if(happyGhastRenderState.isCrowned) {
            if(happyGhastRenderState.shouldShowCrownGhast) {
                this.gogglesUp.setOrigin(0.0F, 32.0F, -1.0F);
                this.crown.hidden = false;
            }else {
                this.gogglesUp.setOrigin(0.0F, 33F, 0.0F);
                this.crown.hidden = true;
            }
            this.royaltyUpgrade.visible = true;
            this.crown.visible = true;
        }else {
            this.gogglesUp.setOrigin(0.0F, 33F, 0.0F);
            this.crown.visible = false;
            this.royaltyUpgrade.visible = false;
        }
        if(happyGhastRenderState.speedUpgrade) {
            this.speedUpgrade.visible = true;
        }else {
            this.speedUpgrade.visible = false;
        }
        if(happyGhastRenderState.strengthUpgrade) {
            this.strengthUpgrade.visible = true;
        }else {
            this.strengthUpgrade.visible = false;
        }
        if(happyGhastRenderState.heartUpgrade) {
            this.heartUpgrade.visible = true;
        }else {
            this.heartUpgrade.visible = false;
        }
        if(happyGhastRenderState.fireballUpgrade) {
            this.fireballUpgrade.visible = true;
        }else {
            this.fireballUpgrade.visible = false;
        }
    }
}

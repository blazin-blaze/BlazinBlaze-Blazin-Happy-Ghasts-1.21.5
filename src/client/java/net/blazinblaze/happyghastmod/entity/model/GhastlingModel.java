package net.blazinblaze.happyghastmod.entity.model;

import net.blazinblaze.happyghastmod.entity.renderer.GhastlingRenderer;
import net.blazinblaze.happyghastmod.entity.state.GhastlingRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.render.entity.model.ModelTransformer;
import net.minecraft.client.render.entity.state.GhastEntityRenderState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class GhastlingModel extends EntityModel<GhastlingRenderState> {
    private final ModelPart[] tentacles = new ModelPart[9];

    public GhastlingModel(ModelPart modelPart) {
        super(modelPart);

        for(int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i] = modelPart.getChild(getTentacleName(i));
        }

    }

    private static String getTentacleName(int index) {
        return "tentacle" + index;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), ModelTransform.origin(0.0F, 17.6F, 0.0F));
        Random random = Random.create(1660L);

        for(int i = 0; i < 9; ++i) {
            float f = (((float)(i % 3) - (float)(i / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
            float g = ((float)(i / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
            int j = random.nextInt(7) + 8;
            modelPartData.addChild(getTentacleName(i), ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, (float)j, 2.0F), ModelTransform.origin(f, 24.6F, g));
        }

        return TexturedModelData.of(modelData, 64, 32).transform(ModelTransformer.scaling(4.5F));
    }

    public void setAngles(GhastlingRenderState ghastEntityRenderState) {
        super.setAngles(ghastEntityRenderState);

        for(int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i].pitch = 0.2F * MathHelper.sin(ghastEntityRenderState.age * 0.3F + (float)i) + 0.4F;
        }

    }
}

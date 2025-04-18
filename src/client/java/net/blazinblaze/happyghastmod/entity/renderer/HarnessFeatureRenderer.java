package net.blazinblaze.happyghastmod.entity.renderer;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.entity.model.HappyGhastHarnessModel;
import net.blazinblaze.happyghastmod.entity.model.HappyGhastLayerTypes;
import net.blazinblaze.happyghastmod.entity.model.HappyGhastModel;
import net.blazinblaze.happyghastmod.entity.model.HappyGhastModelLayers;
import net.blazinblaze.happyghastmod.entity.state.HappyGhastRenderState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.render.entity.state.WolfEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;

public class HarnessFeatureRenderer extends FeatureRenderer<HappyGhastRenderState, HappyGhastModel> {
    private final HappyGhastHarnessModel model;
    private final EquipmentRenderer equipmentRenderer;

    public HarnessFeatureRenderer(FeatureRendererContext<HappyGhastRenderState, HappyGhastModel> context, LoadedEntityModels loader, EquipmentRenderer equipmentRenderer) {
        super(context);
        this.model = new HappyGhastHarnessModel(loader.getModelPart(HappyGhastModelLayers.HAPPY_GHAST_HARNESS));
        this.equipmentRenderer = equipmentRenderer;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, HappyGhastRenderState state, float limbAngle, float limbDistance) {
        ItemStack itemStack = state.saddleStack;
        EquippableComponent equippableComponent = (EquippableComponent)itemStack.get(DataComponentTypes.EQUIPPABLE);
        if (equippableComponent != null && !equippableComponent.assetId().isEmpty()) {
            HappyGhastHarnessModel happyGhastModel = this.model;
            happyGhastModel.setAngles(state);
            this.equipmentRenderer.render(HappyGhastLayerTypes.HAPPY_GHAST_HARNESS, (RegistryKey)equippableComponent.assetId().get(), happyGhastModel, itemStack, matrices, vertexConsumers, light);
        }
    }
}

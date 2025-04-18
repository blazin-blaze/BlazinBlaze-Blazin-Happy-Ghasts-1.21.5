package net.blazinblaze.happyghastmod.entity.model;

import com.google.common.collect.Sets;
import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.util.Set;

public class HappyGhastModelLayers {
    public static final EntityModelLayer HAPPY_GHAST_HARNESS = register("happy-ghast-mod", "happy_ghast_harness");
    public static final EquipmentModel HAPPY_GHAST_SADDLE_MODEL = registerEquipment("entity/happy_ghast_cyan_harness");

    //private static EntityModelLayer createMain(String name) {
        //return new EntityModelLayer(Identifier.of(HappyGhastMod.MOD_ID, name), "saddle");
    //}

    private static EquipmentModel registerEquipment(String id) {
        return EquipmentModel.builder().addMainHumanoidLayer(Identifier.of(HappyGhastMod.MOD_ID, id), false).build();
    }

    private static EntityModelLayer register(String id, String layer) {
        EntityModelLayer entityModelLayer = create(id, layer);
        return entityModelLayer;
    }

    private static EntityModelLayer create(String id, String layer) {
        return new EntityModelLayer(Identifier.of(HappyGhastMod.MOD_ID, id), layer);
    }

    public static final EntityModelLayer HAPPY_GHAST = createMain("happy_ghast");

    private static EntityModelLayer createMain(String name) {
        return new EntityModelLayer(Identifier.of(HappyGhastMod.MOD_ID, name), "main");
    }

    public static void registerModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(HappyGhastModelLayers.HAPPY_GHAST, HappyGhastModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HappyGhastModelLayers.HAPPY_GHAST_HARNESS, HappyGhastHarnessModel::createAdultBodyLayer);
    }
}

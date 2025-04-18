package net.blazinblaze.happyghastmod.entity.model;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.client.render.entity.equipment.EquipmentModel;

public class HappyGhastLayerTypes {
    static {
        EquipmentModel.LayerType.values();
    }

    public static EquipmentModel.LayerType HAPPY_GHAST_HARNESS = ClassTinkerers.getEnum(EquipmentModel.LayerType.class, "HAPPY_GHAST_HARNESS");
}

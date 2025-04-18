package net.blazinblaze.happyghastmod.mm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class EarlyRisers implements Runnable {
    @Override
    public void run() {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();
        String layer = remapper.mapClassName("intermediary", "net.minecraft.class_10186$class_10190");
        ClassTinkerers.enumBuilder(layer, String.class).addEnum("HAPPY_GHAST_HARNESS", "happy_ghast_harness").build();
        //ClassTinkerers.enumBuilder("net.minecraft.client.render.entity.equipment.EquipmentModel.LayerType", String.class).addEnum("HAPPY_GHAST_HARNESS", "happy_ghast_harness").build();
    }
}

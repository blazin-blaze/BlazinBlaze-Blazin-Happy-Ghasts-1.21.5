package net.blazinblaze.happyghastmod.mm;

import com.chocohead.mm.api.ClassTinkerers;
import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.util.Identifier;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ServiceLoader;
import java.util.Set;

public class EarlyRisers implements Runnable {
    @Override
    public void run() {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();
        String layer = remapper.mapClassName("intermediary", "net.minecraft.class_10186$class_10190");
        ClassTinkerers.enumBuilder(layer, String.class).addEnum("HAPPY_GHAST_HARNESS", "happy_ghast_harness").build();
    }
}

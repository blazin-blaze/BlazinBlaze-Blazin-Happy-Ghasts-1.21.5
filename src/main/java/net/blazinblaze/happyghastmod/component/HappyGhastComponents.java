package net.blazinblaze.happyghastmod.component;

import com.mojang.serialization.Codec;
import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class HappyGhastComponents {
    public static void initialize() {
        HappyGhastMod.LOGGER.info("Registering {} components", HappyGhastMod.MOD_ID);
        // Technically this method can stay empty, but some developers like to notify
        // the console, that certain parts of the mod have been successfully initialized
    }

    public static final ComponentType<Boolean> IS_IN_UPGRADE_SLOT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(HappyGhastMod.MOD_ID, "is_in_upgrade_slot"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );

}

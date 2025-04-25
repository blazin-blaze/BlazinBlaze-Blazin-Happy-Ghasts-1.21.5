package net.blazinblaze.happyghastmod.entity;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.entity.custom.Ghastling;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhastFireballEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class HappyGhastEntities {

    private static <T extends Entity> EntityType<T> register(RegistryKey<EntityType<?>> key, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    private static RegistryKey<EntityType<?>> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(HappyGhastMod.MOD_ID, id));
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return register(keyOf(id), type);
    }

    public static final EntityType<HappyGhast> HAPPY_GHAST = register("happy_ghast", EntityType.Builder.<HappyGhast>create(HappyGhast::new, SpawnGroup.CREATURE).dropsNothing().dimensions(4.0F, 4.0F).eyeHeight(2.6F).passengerAttachments(
            new Vec3d(0D, 4.1D, 1.9D),
            new Vec3d(1.9D, 4.1D, 0D),
            new Vec3d(-1.9D, 4.1D, 0D),
            new Vec3d(0D, 4.1D, -1.9D)
    ).vehicleAttachment(0.5F));
    public static final EntityType<Ghastling> GHASTLING = register("ghastling", EntityType.Builder.<Ghastling>create(Ghastling::new, SpawnGroup.CREATURE).dropsNothing().dimensions(2.0F, 2.0F).eyeHeight(1.3F));
    public static final EntityType<HappyGhastFireballEntity> HAPPY_GHAST_FIREBALL_ENTITY = register("happy_ghast_fireball_entity", EntityType.Builder.<HappyGhastFireballEntity>create(HappyGhastFireballEntity::new, SpawnGroup.MISC).dropsNothing().dimensions(1.0F,1.0F).maxTrackingRange(8).trackingTickInterval(20));

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(HAPPY_GHAST, HappyGhast.createGhastAttributes());
        FabricDefaultAttributeRegistry.register(GHASTLING, Ghastling.createGhastAttributes());
    }

    //.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(BlazinBlazeTwitchMod.MOD_ID, "nuclear_tnt_entity")

    public static void registerModEntites() {
        HappyGhastMod.LOGGER.info("Registering mod entities for " + HappyGhastMod.MOD_ID);
    }

}

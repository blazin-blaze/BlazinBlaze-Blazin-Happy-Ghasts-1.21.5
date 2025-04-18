package net.blazinblaze.happyghastmod.sound;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class HappyGhastSounds {
    private HappyGhastSounds() {
    }

    public static final SoundEvent HAPPY_GHAST_AMBIENT = registerSound("entity-happy_ghast-ambient");
    public static final SoundEvent HAPPY_GHAST_HURT = registerSound("entity-happy_ghast-hurt");
    public static final SoundEvent HAPPY_GHAST_DEATH = registerSound("entity-happy_ghast-death");
    public static final SoundEvent HAPPY_GHAST_GOGGLES_DOWN = registerSound("entity-happy_ghast-goggles-down");
    public static final SoundEvent HAPPY_GHAST_GOGGLES_UP = registerSound("entity-happy_ghast-goggles-up");
    public static final SoundEvent HAPPY_GHAST_HARNESS_EQUIP = registerSound("entity-happy-ghast-harness-equip");
    public static final SoundEvent HAPPY_GHAST_HARNESS_UNEQUIP = registerSound("entity-happy-ghast-harness-unequip");
    public static final SoundEvent HAPPY_GHAST_RIDE = registerSound("entity-happy-ghast-ride");

    public static final SoundEvent GHASTLING_AMBIENT = registerSound("entity-ghastling-ambient");
    public static final SoundEvent GHASTLING_HURT = registerSound("entity-ghastling-hurt");
    public static final SoundEvent GHASTLING_DEATH = registerSound("entity-ghastling-death");
    public static final SoundEvent GHASTLING_SPAWN = registerSound("entity-ghastling-spawn");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.of(HappyGhastMod.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }


    public static void initialize() {
        HappyGhastMod.LOGGER.info("Registering " + HappyGhastMod.MOD_ID + " Sounds");
    }
}

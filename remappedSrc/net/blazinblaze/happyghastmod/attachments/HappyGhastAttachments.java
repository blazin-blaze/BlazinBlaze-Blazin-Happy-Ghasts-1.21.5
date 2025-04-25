package net.blazinblaze.happyghastmod.attachments;

import com.mojang.serialization.Codec;
import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;

public class HappyGhastAttachments {
    // Register a type of attached data. This data can be attached to anything, this is only a type
    public static final AttachmentType<Boolean> JUMPED_INPUT = AttachmentRegistry.create(
                    Identifier.of(HappyGhastMod.MOD_ID,"jumped_input"),
                    builder -> builder.initializer(()-> Boolean.FALSE).persistent(Codec.BOOL).syncWith(PacketCodecs.BOOLEAN, AttachmentSyncPredicate.all())
    );

    public static final AttachmentType<Boolean> SPRINT_INPUT = AttachmentRegistry.create(
            Identifier.of(HappyGhastMod.MOD_ID,"sprint_input"),
            builder -> builder.initializer(()-> Boolean.FALSE).persistent(Codec.BOOL).syncWith(PacketCodecs.BOOLEAN, AttachmentSyncPredicate.all())
    );

    public static final AttachmentType<Integer> SNOWBALL_COUNT = AttachmentRegistry.create(
            Identifier.of(HappyGhastMod.MOD_ID,"snowball_count"),
            builder -> builder.initializer(()-> 0).persistent(Codec.INT).syncWith(PacketCodecs.INTEGER, AttachmentSyncPredicate.all())
    );

    public static void init() {
        // This empty method can be called from the mod initializer to ensure our component type is registered at mod initialization time
        // ModAttachmentTypes.init();
    }
}

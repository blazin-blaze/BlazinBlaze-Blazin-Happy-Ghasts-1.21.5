package net.blazinblaze.happyghastmod.attachment;

import com.mojang.serialization.Codec;
import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;

public class HappyGhastClientAttachments {
    public static final AttachmentType<Boolean> SHOULD_RENDER_CROWN = AttachmentRegistry.create(
            Identifier.of(HappyGhastMod.MOD_ID,"should_render_crown"),
            builder -> builder.initializer(()-> Boolean.TRUE).persistent(Codec.BOOL).syncWith(PacketCodecs.BOOLEAN, AttachmentSyncPredicate.all())
    );

    public static void init() {
        // This empty method can be called from the mod initializer to ensure our component type is registered at mod initialization time
        // ModAttachmentTypes.init();
    }
}

package net.blazinblaze.happyghastmod.mixin;

import net.blazinblaze.happyghastmod.attachments.HappyGhastAttachments;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ControlsMixin {
    @Shadow
    public abstract ServerPlayerEntity getPlayer();

    @Inject(method = "onPlayerInput", at = @At("TAIL"))
    private void onPlayerInput(PlayerInputC2SPacket packet, CallbackInfo ci) {
        boolean forward = packet.input().forward();
        ServerPlayerEntity player = this.getPlayer();
        Boolean data = player.getAttachedOrCreate(HappyGhastAttachments.JUMPED_INPUT, HappyGhastAttachments.JUMPED_INPUT.initializer());
        player.setAttached(HappyGhastAttachments.JUMPED_INPUT, packet.input().jump());
        Boolean data2 = player.getAttachedOrCreate(HappyGhastAttachments.SPRINT_INPUT, HappyGhastAttachments.SPRINT_INPUT.initializer());
        player.setAttached(HappyGhastAttachments.SPRINT_INPUT, packet.input().sprint());
    }
}

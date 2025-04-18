package net.blazinblaze.happyghastmod.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.blazinblaze.happyghastmod.attachment.HappyGhastClientAttachments;
import net.blazinblaze.happyghastmod.attachments.HappyGhastAttachments;
import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public abstract class CameraMixin {

    @ModifyReturnValue(method = "getFov", at = @At("TAIL"))
    private float zoomFurtherWhenOnHappyGhast(float original, Camera camera) {
        Boolean data = camera.getFocusedEntity().getAttachedOrCreate(HappyGhastClientAttachments.SHOULD_RENDER_CROWN, HappyGhastClientAttachments.SHOULD_RENDER_CROWN.initializer());
        if (camera.getFocusedEntity().getVehicle() != null) {
            if(camera.getFocusedEntity().getVehicle().getType() == HappyGhastEntities.HAPPY_GHAST) {
                if(!camera.isThirdPerson()) {
                    camera.getFocusedEntity().setAttached(HappyGhastClientAttachments.SHOULD_RENDER_CROWN, false);
                }else {
                    camera.getFocusedEntity().setAttached(HappyGhastClientAttachments.SHOULD_RENDER_CROWN, true);
                }
                original *= 1.25F;
            }else {
                camera.getFocusedEntity().setAttached(HappyGhastClientAttachments.SHOULD_RENDER_CROWN, true);
            }
        }else {
            camera.getFocusedEntity().setAttached(HappyGhastClientAttachments.SHOULD_RENDER_CROWN, true);
        }
        return original;
    }
}

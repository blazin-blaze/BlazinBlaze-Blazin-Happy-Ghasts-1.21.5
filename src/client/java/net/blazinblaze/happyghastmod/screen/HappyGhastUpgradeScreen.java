package net.blazinblaze.happyghastmod.screen;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.screen.HappyGhastUpgradeScreenHandler;
import net.blazinblaze.happyghastmod.slot.HappyGhastUpgradeSlot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class HappyGhastUpgradeScreen extends HandledScreen<HappyGhastUpgradeScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(HappyGhastMod.MOD_ID, "textures/gui/container/happy_ghast_upgrade_screen.png");

    public HappyGhastUpgradeScreen(HappyGhastUpgradeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 133;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        if(slot instanceof HappyGhastUpgradeSlot) {
            if(!slot.hasStack()) {
                if(!this.getScreenHandler().getCursorStack().isEmpty()) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    ClientPlayerEntity player = client.player;
                    if(player != null) {
                        player.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, 0.3F);
                    }
                }
            }
        }
        super.onMouseClick(slot, slotId, button, actionType);
    }

    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }
}

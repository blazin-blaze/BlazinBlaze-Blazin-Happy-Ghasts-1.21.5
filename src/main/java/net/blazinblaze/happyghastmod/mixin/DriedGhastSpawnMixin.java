package net.blazinblaze.happyghastmod.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.block.HappyGhastBlocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.NetherFossilGenerator;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.structure.NetherFossilStructure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherFossilGenerator.class)
public class DriedGhastSpawnMixin {
    @Inject(method = "addPieces", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/StructurePiecesHolder;addPiece(Lnet/minecraft/structure/StructurePiece;)V", shift = At.Shift.AFTER))
    private static void changeFOSSILS(StructureTemplateManager manager, StructurePiecesHolder holder, Random random, BlockPos pos, CallbackInfo ci, @Local BlockRotation rotation) {
        //holder.addPiece(new NetherFossilGenerator.Piece(manager, (Identifier) Util.getRandom((new Identifier[]{Identifier.ofVanilla("nether_fossils/fossil_1"), Identifier.ofVanilla("nether_fossils/fossil_2"), Identifier.ofVanilla("nether_fossils/fossil_3"), Identifier.ofVanilla("nether_fossils/fossil_4"), Identifier.ofVanilla("nether_fossils/fossil_5"), Identifier.ofVanilla("nether_fossils/fossil_6"), Identifier.ofVanilla("nether_fossils/fossil_7"), Identifier.ofVanilla("nether_fossils/fossil_8"), Identifier.ofVanilla("nether_fossils/fossil_9"), Identifier.ofVanilla("nether_fossils/fossil_10"), Identifier.ofVanilla("nether_fossils/fossil_11"), Identifier.of(HappyGhastMod.MOD_ID, "structures/nether_fossils/fossil_12"), Identifier.ofVanilla("nether_fossils/fossil_13"), Identifier.of(HappyGhastMod.MOD_ID, "structures/nether_fossils/fossil_14")}), random), pos, rotation));
        holder.addPiece(new NetherFossilGenerator.Piece(manager, (Identifier) Util.getRandom((new Identifier[]{Identifier.of(HappyGhastMod.MOD_ID, "nether_fossils/fossil_12"), Identifier.of(HappyGhastMod.MOD_ID, "nether_fossils/fossil_14")}), random), pos, rotation));
    }
}

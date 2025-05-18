package org.geysermc.hydraulic.mixin.ext;

import com.fasterxml.jackson.databind.JsonNode;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import org.geysermc.geyser.level.block.GeyserJavaBlockState;
import org.geysermc.geyser.registry.populator.BlockRegistryPopulator;
import org.geysermc.geyser.registry.type.GeyserBedrockBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Map;

@Mixin(value = BlockRegistryPopulator.class, remap = false)
public class BlockRegistryPopulatorMixin {
//    @Inject(
//        method = {"registerBedrockBlocks"},
//        at = @At(
//            value = "INVOKE",
//            target = "Ljava/util/Map$Entry;getKey()Ljava/lang/Object;",
//            ordinal = 0
//        )
//    )
//    private static void replaceJavaRuntimeIdInBedrock(
//        CallbackInfo ci,
//        @Local(name = "entry") Map.Entry<Object, JsonNode> entry,
//        @Local(name = "javaRuntimeId") LocalIntRef javaRuntimeId
//    ) throws CommandSyntaxException {
//        int newId;
//
//        if (entry.getKey() instanceof String str) {
//            newId = Block.getId(
//                    BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK, str, false).blockState()
//            );
//        } else if (entry.getKey() instanceof GeyserJavaBlockState state) {
//            newId = Block.getId(
//                    BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK, state.identifier(), false).blockState()
//            );
//        } else {
//            throw new RuntimeException("Argument type invalid. " + entry.getKey().getClass().getSimpleName());
//        }
//
//        javaRuntimeId.set(newId);
//    }
//
//    @ModifyArgs(
//            method = "registerJavaBlocks",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lorg/geysermc/geyser/registry/MappedRegistry;register(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
//            )
//    )
//    private static void replaceJavaRuntimeIdInJava(
//            Args args
//    ) throws CommandSyntaxException {
//        int newId;
//        Object arg0 = args.get(0);
//
//        if (arg0 instanceof String str) {
//            newId = Block.getId(
//                    BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK, str, false).blockState()
//            );
//        } else if (arg0 instanceof GeyserJavaBlockState state) {
//            newId = Block.getId(
//                    BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK, state.identifier(), false).blockState()
//            );
//        } else {
//            throw new RuntimeException("Argument type invalid. " + arg0.getClass().getSimpleName());
//        }
//
//        args.set(1, newId);
//    }
//
//    @Inject(
//        method = "registerBedrockBlocks",
//        at = @At(
//            value = "INVOKE",
//            target = "Lorg/geysermc/geyser/registry/type/BlockMappings$BlockMappingsBuilder;commandBlock(Lorg/cloudburstmc/protocol/bedrock/data/definitions/BlockDefinition;)Lorg/geysermc/geyser/registry/type/BlockMappings$BlockMappingsBuilder;"
//        )
//    )
//    private static void mapInsertedBlockStates(
//        CallbackInfo ci,
//        @Local(name = "javaRuntimeId") int javaRuntimeId,
//        @Local(name = "javaToBedrockBlocks") GeyserBedrockBlock[] javaToBedrockBlocks,
//        @Local(name = "javaToVanillaBedrockBlocks") GeyserBedrockBlock[] javaToVanillaBedrockBlocks
//    ) {
//        GeyserBedrockBlock lastBedrockBlock = javaToBedrockBlocks[0];
//        GeyserBedrockBlock lastVanillaBlock = javaToVanillaBedrockBlocks[0];
//        for (int i = 1; i < javaRuntimeId; i++) {
//            if (javaToBedrockBlocks[i] == null) {
//                javaToBedrockBlocks[i] = lastBedrockBlock;
//            } else {
//                lastBedrockBlock = javaToBedrockBlocks[i];
//            }
//            if (javaToVanillaBedrockBlocks[i] == null) {
//                javaToVanillaBedrockBlocks[i] = lastVanillaBlock;
//            } else {
//                lastVanillaBlock = javaToVanillaBedrockBlocks[i];
//            }
//        }
//    }
}

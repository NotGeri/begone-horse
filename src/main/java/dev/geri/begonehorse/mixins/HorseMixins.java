package dev.geri.begonehorse.mixins;


import com.google.common.collect.ImmutableList;
import dev.geri.begonehorse.BegoneHorse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;

@Mixin(HorseEntityModel.class)
public abstract class HorseMixins {

    @Shadow
    @Final
    private ModelPart tail;

    @Inject(cancellable = true, at = @At("HEAD"), method = "getHeadParts")
    public void getHeadParts(CallbackInfoReturnable<Iterable<ModelPart>> cir) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || player.isDead()) return;

        if (!BegoneHorse.getState().shouldPartBeRendered("head")) {
            cir.setReturnValue(Collections.emptyList());
        }
    }

    @Inject(cancellable = true, at = @At("HEAD"), method = "getBodyParts")
    public void getBodyParts(CallbackInfoReturnable<Iterable<ModelPart>> cir) {

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || player.isDead()) return;

        if (BegoneHorse.getState() == BegoneHorse.HoarsState.TAIL) {
            cir.setReturnValue(ImmutableList.of(tail));
            return;
        }

        if (!BegoneHorse.getState().shouldPartBeRendered("body")) {
            cir.setReturnValue(Collections.emptyList());
        }
    }

}

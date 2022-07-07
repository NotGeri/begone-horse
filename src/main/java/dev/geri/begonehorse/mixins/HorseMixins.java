package dev.geri.begonehorse.mixins;


import dev.geri.begonehorse.BegoneHorse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;

@Mixin(HorseEntityModel.class)
public abstract class HorseMixins {

    @Inject(cancellable = true, at = @At("HEAD"), method = "getHeadParts")
    public void getHeadParts(CallbackInfoReturnable<Iterable<ModelPart>> cir) {


        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && player.isAlive() && BegoneHorse.isHeadless()) {
            cir.setReturnValue(Collections.emptyList());
        }
    }

}

package io.github.boid.oldBabies.mixin.compatibility;

import io.github.boid.oldBabies.OldBabies;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_model_features.EMF;

@Mixin(EMF.class)
public class EMFMixin {

    @Inject(method = "init", at = @At("HEAD"))
    private static void beforeInit(CallbackInfo ci) {
        OldBabies.initEMF();
    }


}

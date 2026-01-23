package io.github.boid.oldBabies.mixin;

import io.github.boid.oldBabies.Constants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelLayers.class)
public abstract class ModelLayersMixin {

    @Shadow
    private static ModelLayerLocation register(String model, String layer) {
        return null;
    }

    @Shadow
    private static ModelLayerLocation register(String model) {return null;}

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void afterClassInit(CallbackInfo ci) {
        Constants.SHEEP_BABY_WOOL_UNDERCOAT = register("sheep_baby", "wool_undercoat");
        Constants.COLD_CHICKEN_BABY = register("cold_chicken_baby");
        Constants.COLD_PIG_BABY = register("cold_pig_baby");
    }

}

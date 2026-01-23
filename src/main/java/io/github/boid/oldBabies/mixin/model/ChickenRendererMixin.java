package io.github.boid.oldBabies.mixin.model;

import io.github.boid.oldBabies.Constants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChickenRenderer.class)
public class ChickenRendererMixin {

    @Redirect(method = "bakeModels", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;bakeLayer(Lnet/minecraft/client/model/geom/ModelLayerLocation;)Lnet/minecraft/client/model/geom/ModelPart;", ordinal = 3))
    private static ModelPart redirectBakeLayer(EntityRendererProvider.Context instance, ModelLayerLocation id) {
        return instance.bakeLayer(Constants.COLD_CHICKEN_BABY);
    }

}

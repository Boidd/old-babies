package io.github.boid.oldBabies.mixin.model.equine;

import io.github.boid.oldBabies.LayerDefinitionOverrides;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.DonkeyRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DonkeyRenderer.class)
public class DonkeyRendererMixin {

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;bakeLayer(Lnet/minecraft/client/model/geom/ModelLayerLocation;)Lnet/minecraft/client/model/geom/ModelPart;"))
    private static ModelPart redirectBakeLayer(EntityRendererProvider.Context instance, ModelLayerLocation id) {
        return LayerDefinitionOverrides.getOldLayerDefinition(id).bakeRoot();
    }

}

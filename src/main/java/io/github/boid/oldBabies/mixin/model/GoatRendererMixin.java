package io.github.boid.oldBabies.mixin.model;

import net.minecraft.client.renderer.entity.GoatRenderer;
import net.minecraft.client.renderer.entity.state.GoatRenderState;
import net.minecraft.world.entity.animal.goat.Goat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GoatRenderer.class)
public class GoatRendererMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/goat/Goat;Lnet/minecraft/client/renderer/entity/state/GoatRenderState;F)V", at = @At("TAIL"))
    private void afterExtractRenderState(Goat entity, GoatRenderState state, float partialTicks, CallbackInfo ci) {
        if (entity.isBaby()) {
            state.hasLeftHorn = false;
            state.hasRightHorn = false;
        }
    }

}

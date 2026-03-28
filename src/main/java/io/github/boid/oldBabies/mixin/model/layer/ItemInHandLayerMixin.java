package io.github.boid.oldBabies.mixin.model.layer;

import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {

    @Inject(method = "useBabyOffset", at = @At("HEAD"), cancellable = true)
    private <S extends ArmedEntityRenderState> void beforeUseBabyOffset(S state, CallbackInfoReturnable<Boolean> cir) {
        if (OldBabies.getConfig().isEntityEnabled(OldBabies.getConfigType(state.entityType))) cir.setReturnValue(false);
    }

}

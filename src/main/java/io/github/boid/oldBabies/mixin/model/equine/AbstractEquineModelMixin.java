package io.github.boid.oldBabies.mixin.model.equine;

import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.animal.equine.AbstractEquineModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractEquineModel.class)
public class AbstractEquineModelMixin<T extends EquineRenderState> {

    @Shadow @Final private ModelPart tail;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EquineRenderState;)V", at = @At(value = "TAIL"))
    private void fixTailAnimation(T state, CallbackInfo ci) {
        if (OldBabies.getConfig().isEntityEnabled(EntityType.HORSE)) {
            this.tail.xRot = ((float)Math.PI / 6F) + state.walkAnimationSpeed * 0.75F;
        }
    }

}

package io.github.boid.oldBabies.mixin.model.equine;

import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.animal.equine.AbstractEquineModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractEquineModel.class)
public class AbstractEquineModelMixin<T extends EquineRenderState> {

    @Unique
    private float ageScale;

    @Shadow @Final private ModelPart tail;
    @Shadow @Final protected ModelPart headParts;


    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EquineRenderState;)V", at = @At("HEAD"))
    private void beforeSetupAnim(T state, CallbackInfo ci) {
        this.ageScale = state.ageScale;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EquineRenderState;)V", at = @At(value = "TAIL"))
    private void fixTailAnimation(T state, CallbackInfo ci) {
        if (OldBabies.getConfig().isEntityEnabled(EntityType.HORSE)) {
            this.tail.xRot = ((float)Math.PI / 6F) + state.walkAnimationSpeed * 0.75F;
        }
    }

    //restore conditional baby multiplier to fix rearing animation
    @Inject(method = "animateHeadPartsPlacement", at = @At("HEAD"), cancellable = true)
    private void beforeAnimateHeadPartsPlacement(float eating, float standing, CallbackInfo ci) {
        float ageScale = this.ageScale;
        ModelPart head = this.headParts;
        head.y += Mth.lerp(eating, Mth.lerp(standing, 0.0F, -8.0F * ageScale), 7.0F);
        head.z = Mth.lerp(standing, head.z, -4.0F * ageScale);
        ci.cancel();
    }

    @Inject(method = "getLegStandingYOffset", at = @At("RETURN"), cancellable = true)
    private void returnGetLegStandingYOffset(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() * this.ageScale);
    }

    @Inject(method = "getLegStandingZOffset", at = @At("RETURN"), cancellable = true)
    private void returnGetLegStandingZOffset(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() * this.ageScale);
    }

}

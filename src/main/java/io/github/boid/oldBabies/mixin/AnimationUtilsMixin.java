package io.github.boid.oldBabies.mixin;

import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.UndeadRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimationUtils.class)
public class AnimationUtilsMixin {

    @Unique
    private static boolean originalBabyState = false;

    @Inject(method = "animateZombieArms", at = @At("HEAD"))
    private static void beforeAnimateZombieArms(ModelPart leftArm, ModelPart rightArm, boolean aggressive, UndeadRenderState state, CallbackInfo ci) {
        originalBabyState = state.isBaby;
        if (OldBabies.getConfig().isEntityEnabled(state.entityType) && state.isBaby) {
            state.isBaby = false;
        }
    }

    @Inject(method = "animateZombieArms", at = @At("TAIL"))
    private static void afterAnimateZombieArms(ModelPart leftArm, ModelPart rightArm, boolean aggressive, UndeadRenderState state, CallbackInfo ci) {
        state.isBaby = originalBabyState;
    }

}

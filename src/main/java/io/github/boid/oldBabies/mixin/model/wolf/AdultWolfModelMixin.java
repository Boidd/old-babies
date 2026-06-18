package io.github.boid.oldBabies.mixin.model.wolf;

import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdultWolfModel.class)
public class AdultWolfModelMixin {

    @Shadow @Final
    private ModelPart upperBody;

    //fix baby wolf sitting pose
    @Inject(method = "setSittingPose", at = @At("TAIL"))
    private void afterSetSittingPose(WolfRenderState state, CallbackInfo ci) {
        this.upperBody.y += ((2.0f * state.ageScale) - 2.0f);
    }

}

package io.github.boid.oldBabies.mixin.model.wolf;

import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.animal.wolf.WolfModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.world.entity.animal.wolf.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("deprecation")
@Mixin(WolfRenderer.class)
public abstract class WolfRendererMixin extends AgeableMobRenderer<Wolf, WolfRenderState, WolfModel> {

    public WolfRendererMixin(EntityRendererProvider.Context context, WolfModel adultModel, WolfModel babyModel, float shadow) {
        super(context, adultModel, babyModel, shadow);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void fixBabyModel(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.babyModel = new AdultWolfModel(context.bakeLayer(ModelLayers.WOLF_BABY));
    }

}

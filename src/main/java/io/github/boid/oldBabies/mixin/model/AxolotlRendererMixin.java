package io.github.boid.oldBabies.mixin.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.axolotl.AdultAxolotlModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.AxolotlRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.AxolotlRenderState;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("deprecation")
@Mixin(AxolotlRenderer.class)
public abstract class AxolotlRendererMixin extends AgeableMobRenderer<Axolotl, AxolotlRenderState, EntityModel<AxolotlRenderState>> {

    public AxolotlRendererMixin(EntityRendererProvider.Context context, EntityModel<AxolotlRenderState> adultModel, EntityModel<AxolotlRenderState> babyModel, float shadow) {
        super(context, adultModel, babyModel, shadow);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void fixBabyModel(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.babyModel = new AdultAxolotlModel(context.bakeLayer(ModelLayers.AXOLOTL_BABY));
    }

}

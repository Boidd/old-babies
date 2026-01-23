package io.github.boid.oldBabies.mixin.model.cat;

import net.minecraft.client.model.animal.feline.AbstractFelineModel;
import net.minecraft.client.model.animal.feline.AdultCatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.world.entity.animal.feline.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("deprecation")
@Mixin(CatRenderer.class)
public abstract class CatRendererMixin extends AgeableMobRenderer<Cat, CatRenderState, AbstractFelineModel<CatRenderState>> {

    public CatRendererMixin(EntityRendererProvider.Context context, AbstractFelineModel<CatRenderState> adultModel, AbstractFelineModel<CatRenderState> babyModel, float shadow) {
        super(context, adultModel, babyModel, shadow);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void fixBabyModel(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.babyModel = new AdultCatModel(context.bakeLayer(ModelLayers.CAT_BABY));
    }
}

package io.github.boid.oldBabies.mixin;

import io.github.boid.oldBabies.ModelSwappers;
import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@SuppressWarnings("deprecation")
@Mixin(AgeableMobRenderer.class)
public abstract class AgeableMobRendererMixin<T extends Mob, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends MobRenderer<T, S, M> {

    @Shadow
    public M babyModel;

    public AgeableMobRendererMixin(EntityRendererProvider.Context context, M model, float shadow) {
        super(context, model, shadow);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("TAIL"))
    private void overrideBabyModels(EntityRendererProvider.Context context, EntityModel<S> adultModel, EntityModel<S> babyModel, float shadow, CallbackInfo ci) {
        try {
            Optional<EntityModel<?>> replacementModel = ModelSwappers.tryReplaceEntityModel(context, (Class<? extends EntityModel<?>>) babyModel.getClass());
            replacementModel.ifPresent(entityModel -> this.babyModel = (M) entityModel);
        } catch (Exception exception) {
            OldBabies.LOGGER.error("Failed to swap the model {}: {}", babyModel.getClass().getName(), exception.getMessage());
        }
    }

}

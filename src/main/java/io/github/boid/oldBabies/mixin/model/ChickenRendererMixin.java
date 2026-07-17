package io.github.boid.oldBabies.mixin.model;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.boid.oldBabies.Constants;
import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.chicken.Chicken;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChickenRenderer.class)
public abstract class ChickenRendererMixin extends MobRenderer<Chicken, ChickenRenderState, ChickenModel> {

    public ChickenRendererMixin(EntityRendererProvider.Context context, ChickenModel model, float shadow) {
        super(context, model, shadow);
    }

    @Nullable @Unique
    private ChickenModel storedModel = null;

    @Redirect(method = "bakeModels", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;bakeLayer(Lnet/minecraft/client/model/geom/ModelLayerLocation;)Lnet/minecraft/client/model/geom/ModelPart;", ordinal = 3))
    private static ModelPart redirectBakeLayer(EntityRendererProvider.Context instance, ModelLayerLocation id) {
        if (OldBabies.getConfig().isEntityEnabled(EntityTypes.CHICKEN)) {
            return instance.bakeLayer(Constants.COLD_CHICKEN_BABY);
        }
        return instance.bakeLayer(id);
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/ChickenRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/MobRenderer;submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"), order = 900)
    private void beforeSubmitModified(ChickenRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        storedModel = OldBabies.detectEMF() && OldBabies.getConfig().isEntityEnabled(EntityTypes.CHICKEN) ? this.model : null;
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/ChickenRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/MobRenderer;submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"), order = 1100)
    private void beforeFinalSubmit(ChickenRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (storedModel != null) {
            this.model = storedModel;
        }
    }

}

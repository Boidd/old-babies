package io.github.boid.oldBabies.mixin.model;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.boid.oldBabies.Constants;
import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigRenderer.class)
public abstract class PigRendererMixin extends MobRenderer<Pig, PigRenderState, PigModel> {

    @Nullable @Unique
    private PigModel storedModel = null;

    public PigRendererMixin(EntityRendererProvider.Context context, PigModel model, float shadow) {
        super(context, model, shadow);
    }

    @Redirect(method = "bakeModels", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;bakeLayer(Lnet/minecraft/client/model/geom/ModelLayerLocation;)Lnet/minecraft/client/model/geom/ModelPart;", ordinal = 3))
    private static ModelPart redirectBakeLayer(EntityRendererProvider.Context instance, ModelLayerLocation id) {
        if (OldBabies.getConfig().isEntityEnabled(EntityTypes.PIG)) {
            return instance.bakeLayer(Constants.COLD_PIG_BABY);
        }
        return instance.bakeLayer(id);
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/PigRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/MobRenderer;submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"), order = 900)
    private void beforeSubmitModified(PigRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        storedModel = OldBabies.detectEMF() && OldBabies.getConfig().isEntityEnabled(EntityTypes.PIG) ? this.model : null;
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/PigRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/MobRenderer;submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"), order = 1100)
    private void beforeFinalSubmit(PigRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (storedModel != null) {
            this.model = storedModel;
        }
    }

}

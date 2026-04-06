package io.github.boid.oldBabies.mixin.model.cat;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.animal.feline.AbstractFelineModel;
import net.minecraft.client.model.animal.feline.AdultCatModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatCollarLayer.class)
public class CatCollarLayerMixin {

    @Unique
    private AdultCatModel modifiedBabyModel;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterInit(RenderLayerParent<CatRenderState, AbstractFelineModel<CatRenderState>> renderer, EntityModelSet modelSet, CallbackInfo ci) {
        this.modifiedBabyModel = new AdultCatModel(modelSet.bakeLayer(ModelLayers.CAT_BABY_COLLAR));
    }

    @ModifyVariable(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/CatRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/CatCollarLayer;coloredCutoutModelCopyLayerRender(Lnet/minecraft/client/model/Model;Lnet/minecraft/resources/Identifier;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;II)V"), order = 999, name = "model")
    private AbstractFelineModel<CatRenderState> modifyModel(AbstractFelineModel<CatRenderState> model) {
        if (OldBabies.getConfig().isEntityEnabled(EntityType.CAT)) return this.modifiedBabyModel;
        return model;
    }

    @SuppressWarnings("unchecked")
    @Redirect(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/CatRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/CatCollarLayer;coloredCutoutModelCopyLayerRender(Lnet/minecraft/client/model/Model;Lnet/minecraft/resources/Identifier;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;II)V"))
    private void redirectLayerRender(Model<? super LivingEntityRenderState> model, Identifier identifier, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, LivingEntityRenderState livingEntityRenderState, int j, int k) {
        boolean enabled = OldBabies.getConfig().isEntityEnabled(EntityType.CAT) && livingEntityRenderState.isBaby;
        RenderLayer.coloredCutoutModelCopyLayerRender(enabled ? (Model<? super LivingEntityRenderState>) ((Object) modifiedBabyModel) : model, OldBabies.removeBabyFromIdentifier(identifier, livingEntityRenderState.entityType), poseStack, submitNodeCollector, i, livingEntityRenderState, j, k);
    }

}

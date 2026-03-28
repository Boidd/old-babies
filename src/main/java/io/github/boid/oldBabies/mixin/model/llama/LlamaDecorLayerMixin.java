package io.github.boid.oldBabies.mixin.model.llama;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.animal.llama.LlamaModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.client.renderer.entity.state.LlamaRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LlamaDecorLayer.class)
public class LlamaDecorLayerMixin {

    @Shadow
    @Final
    private EquipmentLayerRenderer equipmentRenderer;
    @Unique
    private LlamaModel oldBabyModel;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterInit(RenderLayerParent<LlamaRenderState, LlamaModel> renderer, EntityModelSet modelSet, EquipmentLayerRenderer equipmentRenderer, CallbackInfo ci) {
        this.oldBabyModel = new LlamaModel(modelSet.bakeLayer(ModelLayers.LLAMA_BABY_DECOR));
    }

    @SuppressWarnings("unchecked")
    @Redirect(method = "renderEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;II)V"))
    private <S> void redirectRenderLayers(EquipmentLayerRenderer instance, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId, Model<? super S> model, S state, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int outlineColor) {
        LlamaRenderState llamaState = (LlamaRenderState) state;
        boolean useOldModel = OldBabies.getConfig().isEntityEnabled(EntityType.LLAMA) && llamaState.isBaby;
        Identifier id = OldBabies.removeBabyFromIdentifier(equipmentAssetId.identifier(), EntityType.LLAMA);
        ResourceKey<EquipmentAsset> equipmentAssetKey = ResourceKey.create(equipmentAssetId.registryKey(), id);
        this.equipmentRenderer.renderLayers(layerType, equipmentAssetKey, (Model<? super S>)(useOldModel ? this.oldBabyModel : model), state, itemStack, poseStack, submitNodeCollector, lightCoords, llamaState.outlineColor);
    }

}

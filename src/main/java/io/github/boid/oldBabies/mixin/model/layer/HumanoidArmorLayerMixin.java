package io.github.boid.oldBabies.mixin.model.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin {

    @Shadow
    protected abstract boolean usesInnerModel(EquipmentSlot slot);

    @Unique
    private EquipmentSlot equipmentSlot = null;

    @Inject(method = "renderArmorPiece", at = @At("HEAD"))
    private <S extends HumanoidRenderState> void beforeRenderArmorPiece(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, EquipmentSlot slot, int lightCoords, S state, CallbackInfo ci) {
        this.equipmentSlot = slot;
    }

    @Redirect(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;II)V"))
    private <S> void redirectRenderLayers(EquipmentLayerRenderer instance, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId, Model<? super S> model, S state, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int outlineColor) {
        EquipmentClientInfo.LayerType typeToUse = layerType;
        if (layerType == EquipmentClientInfo.LayerType.HUMANOID_BABY) {
            HumanoidRenderState renderState = (HumanoidRenderState) state;
            if (OldBabies.getConfig().isEntityEnabled(OldBabies.getConfigType(renderState.entityType))) {
                typeToUse = this.usesInnerModel(this.equipmentSlot) ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS : EquipmentClientInfo.LayerType.HUMANOID;
            }
        }
        instance.renderLayers(typeToUse, equipmentAssetId, model, state, itemStack, poseStack, submitNodeCollector, lightCoords, outlineColor);
    }

}

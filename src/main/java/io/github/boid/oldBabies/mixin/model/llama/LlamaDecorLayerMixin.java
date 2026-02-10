package io.github.boid.oldBabies.mixin.model.llama;

import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.animal.llama.LlamaModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LlamaDecorLayer.class)
public class LlamaDecorLayerMixin {

    @Mutable @Shadow @Final private LlamaModel babyModel;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterInit(RenderLayerParent<?, ?> renderer, EntityModelSet modelSet, EquipmentLayerRenderer equipmentRenderer, CallbackInfo ci) {
        this.babyModel = new LlamaModel(modelSet.bakeLayer(ModelLayers.LLAMA_BABY_DECOR));
    }

    @ModifyVariable(method = "renderEquipment", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private ResourceKey<EquipmentAsset> modifyEquipmentAsset(ResourceKey<EquipmentAsset> orig) {
        Identifier id = OldBabies.removeBabyFromIdentifier(orig.identifier());
        return ResourceKey.create(orig.registryKey(), id);
    }

}

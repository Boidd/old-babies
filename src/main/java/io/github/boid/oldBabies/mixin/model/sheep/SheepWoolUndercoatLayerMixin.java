package io.github.boid.oldBabies.mixin.model.sheep;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.boid.oldBabies.Constants;
import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.SheepWoolUndercoatLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepWoolUndercoatLayer.class)
public class SheepWoolUndercoatLayerMixin {

    @Shadow @Final private static Identifier SHEEP_WOOL_UNDERCOAT_LOCATION;

    @Unique
    private EntityModel<SheepRenderState> babyModel;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterInit(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet modelSet, CallbackInfo ci) {
        if (OldBabies.getConfig().isEntityEnabled(EntityType.SHEEP)) this.babyModel = new SheepFurModel(modelSet.bakeLayer(Constants.SHEEP_BABY_WOOL_UNDERCOAT));
    }

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/SheepRenderState;FF)V", at = @At("RETURN"))
    private void afterSubmit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SheepRenderState state, float yRot, float xRot, CallbackInfo ci) {
        if (OldBabies.getConfig().isEntityEnabled(EntityType.SHEEP) && state.isBaby && !state.isInvisible && !(!state.isJebSheep && state.woolColor == DyeColor.WHITE)) {
            SheepWoolUndercoatLayer.coloredCutoutModelCopyLayerRender(this.babyModel, SHEEP_WOOL_UNDERCOAT_LOCATION, poseStack, submitNodeCollector, lightCoords, state, state.getWoolColor(), 1);
        }
    }

}

package io.github.boid.oldBabies.mixin.model;

import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.fox.FoxModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.FoxRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxModel.class)
public abstract class FoxModelMixin extends EntityModel<FoxRenderState> {

    @Shadow @Final protected ModelPart rightHindLeg;
    @Shadow @Final protected ModelPart leftHindLeg;
    @Shadow @Final protected ModelPart rightFrontLeg;
    @Shadow @Final protected ModelPart leftFrontLeg;
    @Shadow @Final public ModelPart head;
    @Shadow @Final protected ModelPart body;
    @Shadow @Final protected ModelPart tail;
    @Shadow private float legMotionPos;

    protected FoxModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/FoxRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void beforeSetupAnim(FoxRenderState state, CallbackInfo ci) {
        if (OldBabies.getConfig().isEntityEnabled(state.entityType) && state.isBaby) {
            setupOldBabyAnim(state);
            ci.cancel();
        }
    }

    @Unique
    private void setupOldBabyAnim(final FoxRenderState state) {
        this.resetPose();
        float animationSpeed = state.walkAnimationSpeed;
        float animationPos = state.walkAnimationPos;
        this.rightHindLeg.xRot = Mth.cos(animationPos * 0.6662F) * 1.4F * animationSpeed;
        this.leftHindLeg.xRot = Mth.cos(animationPos * 0.6662F + (float)Math.PI) * 1.4F * animationSpeed;
        this.rightFrontLeg.xRot = Mth.cos(animationPos * 0.6662F + (float)Math.PI) * 1.4F * animationSpeed;
        this.leftFrontLeg.xRot = Mth.cos(animationPos * 0.6662F) * 1.4F * animationSpeed;
        this.head.zRot = state.headRollAngle;
        this.rightHindLeg.visible = true;
        this.leftHindLeg.visible = true;
        this.rightFrontLeg.visible = true;
        this.leftFrontLeg.visible = true;
        float ageScale = state.ageScale;
        if (state.isCrouching) {
            this.body.xRot += 0.10471976F;
            float crouch = state.crouchAmount;
            this.body.y += crouch * ageScale;
            this.head.y += crouch * ageScale;
        } else if (state.isSleeping) {
            this.body.zRot = (-(float)Math.PI / 2F);
            this.body.y += 5.0F * ageScale;
            this.tail.xRot = -2.1816616F;
            this.body.z += 2.0F;
            this.head.x += 2.0F * ageScale;
            this.head.y += 2.99F * ageScale;
            this.head.yRot = -2.0943952F;
            this.head.zRot = 0.0F;
            this.rightHindLeg.visible = false;
            this.leftHindLeg.visible = false;
            this.rightFrontLeg.visible = false;
            this.leftFrontLeg.visible = false;
        } else if (state.isSitting) {
            this.body.xRot = ((float)Math.PI / 6F);
            this.body.y -= 7.0F * ageScale;
            this.body.z += 3.0F * ageScale;
            this.tail.xRot = ((float)Math.PI / 4F);
            this.tail.z -= ageScale;
            this.head.xRot = 0.0F;
            this.head.yRot = 0.0F;
            --this.head.y;
            this.head.z -= 0.375F;
            this.rightHindLeg.xRot = -1.3089969F;
            this.rightHindLeg.y += 4.0F * ageScale;
            this.rightHindLeg.z -= 0.25F * ageScale;
            this.leftHindLeg.xRot = -1.3089969F;
            this.leftHindLeg.y += 4.0F * ageScale;
            this.leftHindLeg.z -= 0.25F * ageScale;
            this.rightFrontLeg.xRot = -0.2617994F;
            this.leftFrontLeg.xRot = -0.2617994F;
        }

        if (!state.isSleeping && !state.isFaceplanted && !state.isCrouching) {
            this.head.xRot = state.xRot * ((float)Math.PI / 180F);
            this.head.yRot = state.yRot * ((float)Math.PI / 180F);
        }

        if (state.isSleeping) {
            this.head.xRot = 0.0F;
            this.head.yRot = -2.0943952F;
            this.head.zRot = Mth.cos(state.ageInTicks * 0.027F) / 22.0F;
        }

        if (state.isCrouching) {
            float wiggleAmount = Mth.cos(state.ageInTicks) * 0.01F;
            this.body.yRot = wiggleAmount;
            this.rightHindLeg.zRot = wiggleAmount;
            this.leftHindLeg.zRot = wiggleAmount;
            this.rightFrontLeg.zRot = wiggleAmount / 2.0F;
            this.leftFrontLeg.zRot = wiggleAmount / 2.0F;
        }

        if (state.isFaceplanted) {
            float legMoveFactor = 0.1F;
            float constantMultiplier = 0.4662F;
            float firstLegRot = Mth.cos(this.legMotionPos * constantMultiplier) * legMoveFactor;
            float secondLegRot = Mth.cos(this.legMotionPos * constantMultiplier + (float) Math.PI) * legMoveFactor;
            this.legMotionPos += 0.67F;
            this.rightHindLeg.xRot = firstLegRot;
            this.leftHindLeg.xRot = secondLegRot;
            this.rightFrontLeg.xRot = secondLegRot;
            this.leftFrontLeg.xRot = firstLegRot;
        }

    }


}

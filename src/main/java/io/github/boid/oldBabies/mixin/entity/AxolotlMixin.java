package io.github.boid.oldBabies.mixin.entity;

import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Axolotl.class)
public abstract class AxolotlMixin {

    @Shadow
    protected abstract void tickAdultAnimations();

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;tickBabyAnimations()V"))
    private void redirectTickBabyAnimations(Axolotl instance) {
        this.tickAdultAnimations();
    }

}

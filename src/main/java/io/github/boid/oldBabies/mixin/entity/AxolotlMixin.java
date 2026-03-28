package io.github.boid.oldBabies.mixin.entity;

import io.github.boid.oldBabies.OldBabies;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Axolotl.class)
public abstract class AxolotlMixin {

    @Shadow
    protected abstract void tickAdultAnimations();

    @Shadow
    protected abstract void tickBabyAnimations();

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;tickBabyAnimations()V"))
    private void redirectTickBabyAnimations(Axolotl instance) {
        if (OldBabies.getConfig().isEntityEnabled(EntityType.AXOLOTL)) {
            this.tickAdultAnimations();
        } else {
            this.tickBabyAnimations();
        }
    }

}

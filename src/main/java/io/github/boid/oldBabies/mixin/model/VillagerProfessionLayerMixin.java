package io.github.boid.oldBabies.mixin.model;

import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerProfessionLayer.class)
public class VillagerProfessionLayerMixin {

    @Inject(method = "getIdentifier(Ljava/lang/String;Lnet/minecraft/resources/Identifier;)Lnet/minecraft/resources/Identifier;", at = @At("RETURN"), cancellable = true)
    private void returnGetIdentifier(String type, Identifier key, CallbackInfoReturnable<Identifier> cir) {
        cir.setReturnValue(OldBabies.removeBabyFromIdentifier(cir.getReturnValue(), EntityType.VILLAGER));
    }

    @Inject(method = "getIdentifier(Ljava/lang/String;Lnet/minecraft/core/Holder;)Lnet/minecraft/resources/Identifier;", at = @At("RETURN"), cancellable = true)
    private void returnGetIdentifier(String type, Holder<?> holder, CallbackInfoReturnable<Identifier> cir) {
        cir.setReturnValue(OldBabies.removeBabyFromIdentifier(cir.getReturnValue(), EntityType.VILLAGER));
    }

}

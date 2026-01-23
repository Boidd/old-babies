package io.github.boid.oldBabies.mixin.model.sheep;

import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.resources.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SheepWoolLayer.class)
public class SheepWoolLayerMixin {

    @ModifyVariable(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/SheepRenderState;FF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/SheepRenderState;isInvisible:Z", opcode = Opcodes.GETFIELD), name = "location")
    private Identifier fixTextureLocation(Identifier value) {
        return OldBabies.removeBabyFromIdentifier(value);
    }

}

package io.github.boid.oldBabies.mixin;

import com.google.common.collect.ImmutableMap;
import io.github.boid.oldBabies.LayerDefinitionOverrides;
import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerDefinitions.class)
public class LayerDefinitionsMixin {

    @Redirect(method = "createRoots", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"))
    private static ImmutableMap<ModelLayerLocation, LayerDefinition> restoreBabySheepWool(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> instance) {
        OldBabies.LOGGER.debug("Rebuilding map");
        ImmutableMap<ModelLayerLocation, LayerDefinition> newMap = LayerDefinitionOverrides.rebuildMap(instance);
        OldBabies.LOGGER.debug("Map rebuilt");
        return newMap;
    }

}

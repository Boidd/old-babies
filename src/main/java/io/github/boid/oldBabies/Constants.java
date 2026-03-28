package io.github.boid.oldBabies;

import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.MeshTransformer;

import java.util.Set;

public class Constants {

    public static final MeshTransformer DEFAULT_BABY_TRANSFORMER = new BabyModelTransform(Set.of("head"));
    public static final MeshTransformer GENERIC_SHRINK_BABY_TRANSFORMER = MeshTransformer.scaling(0.5F);

    public static final MeshTransformer FELINE_BABY_TRANSFORMER = new BabyModelTransform(true, 10.0F, 4.0F, Set.of("head"));
    public static final MeshTransformer SHEEP_BABY_TRANSFORMER = new BabyModelTransform(false, 8.0F, 4.0F, 2.0F, 2.0F, 24.0F, Set.of("head"));
    public static final MeshTransformer CHICKEN_BABY_TRANSFORMER = new BabyModelTransform(false, 5.0F, 2.0F, 2.0F, 1.99F, 24.0F, Set.of("head", "beak", "red_thing"));
    public static final MeshTransformer EQUINE_BABY_TRANSFORMER = new BabyModelTransform(true, 16.2F, 1.36F, 2.7272F, 2.0F, 20.0F, Set.of("head_parts"));
    public static final MeshTransformer TURTLE_BABY_TRANSFORMER = new BabyModelTransform(true, 120.0F, 0.0F, 9.0F, 6.0F, 120.0F, Set.of("head"));
    public static final MeshTransformer FOX_BABY_TRANSFORMER = new BabyModelTransform(true, 8.0F, 3.35F, Set.of("head"));
    public static final MeshTransformer GOAT_BABY_TRANSFORMER = new BabyModelTransform(true, 19.0F, 1.0F, 2.5F, 2.0F, 24.0F, Set.of("head"));
    public static final MeshTransformer ARMADILLO_BABY_TRANSFORMER = MeshTransformer.scaling(0.6F);
    public static final MeshTransformer CAMEL_BABY_TRANSFORMER = MeshTransformer.scaling(0.45F);
    public static final MeshTransformer POLAR_BEAR_BABY_TRANSFORMER = new BabyModelTransform(true, 16.0F, 4.0F, 2.25F, 2.0F, 24.0F, Set.of("head"));
    public static final MeshTransformer HUMANOID_BABY_TRANSFORMER = new BabyModelTransform(true, 16.0F, 0.0F, 2.0F, 2.0F, 24.0F, Set.of("head"));
    public static final MeshTransformer HOGLIN_BABY_TRANSFORMER = new BabyModelTransform(true, 8.0F, 6.0F, 1.9F, 2.0F, 24.0F, Set.of("head"));
    public static final MeshTransformer PANDA_BABY_TRANSFORMER = new BabyModelTransform(true, 23.0F, 4.8F, 2.7F, 3.0F, 49.0F, Set.of("head"));

    public static ModelLayerLocation SHEEP_BABY_WOOL_UNDERCOAT;
    public static ModelLayerLocation COLD_CHICKEN_BABY;
    public static ModelLayerLocation COLD_PIG_BABY;

}

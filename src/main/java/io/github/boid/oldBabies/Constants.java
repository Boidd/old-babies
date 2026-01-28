package io.github.boid.oldBabies;

import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.MeshTransformer;

import java.util.Set;

public class Constants {

    public static final MeshTransformer DEFAULT_BABY_TRANSFORMER = new BabyModelTransform(Set.of("head"));

    public static final MeshTransformer FELINE_BABY_TRANSFORMER = new BabyModelTransform(true, 10.0F, 4.0F, Set.of("head"));
    public static final MeshTransformer SHEEP_BABY_TRANSFORMER = new BabyModelTransform(false, 8.0F, 4.0F, 2.0F, 2.0F, 24.0F, Set.of("head"));
    public static final MeshTransformer CHICKEN_BABY_TRANSFORMER = new BabyModelTransform(false, 5.0F, 2.0F, 2.0F, 1.99F, 24.0F, Set.of("head", "beak", "red_thing"));
    public static final MeshTransformer EQUINE_BABY_TRANSFORMER = new BabyModelTransform(true, 16.2F, 1.36F, 2.7272F, 2.0F, 20.0F, Set.of("head_parts"));
    public static final MeshTransformer GENERIC_AQUATIC_BABY_TRANSFORMER = MeshTransformer.scaling(0.5F);
    public static final MeshTransformer TURTLE_BABY_TRANSFORMER = new BabyModelTransform(true, 120.0F, 0.0F, 9.0F, 6.0F, 120.0F, Set.of("head"));

    public static ModelLayerLocation SHEEP_BABY_WOOL_UNDERCOAT;
    public static ModelLayerLocation COLD_CHICKEN_BABY;
    public static ModelLayerLocation COLD_PIG_BABY;

}

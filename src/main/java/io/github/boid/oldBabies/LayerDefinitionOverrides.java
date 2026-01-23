package io.github.boid.oldBabies;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.animal.chicken.AdultChickenModel;
import net.minecraft.client.model.animal.chicken.ColdChickenModel;
import net.minecraft.client.model.animal.cow.ColdCowModel;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.animal.cow.WarmCowModel;
import net.minecraft.client.model.animal.equine.DonkeyModel;
import net.minecraft.client.model.animal.feline.AdultCatModel;
import net.minecraft.client.model.animal.feline.AdultFelineModel;
import net.minecraft.client.model.animal.pig.ColdPigModel;
import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;

import java.util.ArrayList;
import java.util.List;

public class LayerDefinitionOverrides {

    private record Entry(ModelLayerLocation location, LayerDefinition definition, MeshTransformer babyTransformer) {

        boolean tryApply(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> mapBuilder, ModelLayerLocation currentLocation) {
            if (currentLocation.equals(location)) {
                apply(mapBuilder);
                return true;
            }
            return false;
        }

        void apply(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> mapBuilder) {
            mapBuilder.put(location, definition.apply(babyTransformer));
        }

    }

    private static final LayerDefinition donkeyLayerDefinition = LayerDefinition.create(OldBabies.createFullScaleEquineBabyMesh(CubeDeformation.NONE), 64, 64).apply(DonkeyModel.DONKEY_TRANSFORMER);

    private static final List<Entry> overrides = new ArrayList<>(){{
        add(new Entry(ModelLayers.SHEEP_BABY, SheepModel.createBodyLayer(), Constants.SHEEP_BABY_TRANSFORMER));
        add(new Entry(ModelLayers.SHEEP_BABY_WOOL, SheepFurModel.createFurLayer(), Constants.SHEEP_BABY_TRANSFORMER));
        add(new Entry(ModelLayers.CHICKEN_BABY, AdultChickenModel.createBodyLayer(), Constants.CHICKEN_BABY_TRANSFORMER));
        add(new Entry(ModelLayers.PIG_BABY, PigModel.createBodyLayer(CubeDeformation.NONE), PigModel.BABY_TRANSFORMER));
        add(new Entry(ModelLayers.COW_BABY, CowModel.createBodyLayer(), CowModel.BABY_TRANSFORMER));
        add(new Entry(ModelLayers.WARM_COW_BABY, WarmCowModel.createBodyLayer(), CowModel.BABY_TRANSFORMER));
        add(new Entry(ModelLayers.COLD_COW_BABY, ColdCowModel.createBodyLayer(), CowModel.BABY_TRANSFORMER));
        add(new Entry(ModelLayers.WOLF_BABY, LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32), Constants.DEFAULT_BABY_TRANSFORMER));
        add(new Entry(ModelLayers.CAT_BABY, LayerDefinition.create(AdultCatModel.createBodyMesh(CubeDeformation.NONE), 64, 32), Constants.FELINE_BABY_TRANSFORMER));
        add(new Entry(ModelLayers.CAT_BABY_COLLAR, LayerDefinition.create(AdultFelineModel.createBodyMesh(AdultCatModel.COLLAR_DEFORMATION), 64, 32).apply(AdultCatModel.CAT_TRANSFORMER), Constants.FELINE_BABY_TRANSFORMER));
        add(new Entry(ModelLayers.OCELOT_BABY, LayerDefinition.create(AdultFelineModel.createBodyMesh(CubeDeformation.NONE), 64, 32), Constants.FELINE_BABY_TRANSFORMER));
        add(new Entry(ModelLayers.DONKEY_BABY, donkeyLayerDefinition.apply(MeshTransformer.scaling(0.87F)), Constants.EQUINE_BABY_TRANSFORMER));
        add(new Entry(ModelLayers.MULE_BABY, donkeyLayerDefinition.apply(MeshTransformer.scaling(0.92F)), Constants.EQUINE_BABY_TRANSFORMER));
    }};

    private static final List<Entry> additionalEntries = new ArrayList<>(){{
        add(new Entry(Constants.SHEEP_BABY_WOOL_UNDERCOAT, SheepModel.createBodyLayer(), Constants.SHEEP_BABY_TRANSFORMER));
        add(new Entry(Constants.COLD_CHICKEN_BABY, ColdChickenModel.createBodyLayer(), Constants.CHICKEN_BABY_TRANSFORMER));
        add(new Entry(Constants.COLD_PIG_BABY, ColdPigModel.createBodyLayer(CubeDeformation.NONE), PigModel.BABY_TRANSFORMER));
    }};

    public static ImmutableMap<ModelLayerLocation, LayerDefinition> rebuildMap(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> originalBuilder) {
        ImmutableMap<ModelLayerLocation, LayerDefinition> originalMap = originalBuilder.build();
        ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> newMap = new ImmutableMap.Builder<>();
        for (ModelLayerLocation location : originalMap.keySet()) {
            boolean overridden = false;
            for (Entry override : overrides) {
                overridden = override.tryApply(newMap, location) || overridden;
            }
            if (overridden) continue;
            LayerDefinition definition = originalMap.get(location);
            if (definition != null) {
                newMap.put(location, definition);
            }
        }
        for (Entry entry : additionalEntries) {
            entry.apply(newMap);
        }
        return newMap.build();
    }

}

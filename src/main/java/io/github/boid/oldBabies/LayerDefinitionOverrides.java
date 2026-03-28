package io.github.boid.oldBabies;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.animal.armadillo.AdultArmadilloModel;
import net.minecraft.client.model.animal.axolotl.AdultAxolotlModel;
import net.minecraft.client.model.animal.bee.AdultBeeModel;
import net.minecraft.client.model.animal.camel.AdultCamelModel;
import net.minecraft.client.model.animal.chicken.AdultChickenModel;
import net.minecraft.client.model.animal.chicken.ColdChickenModel;
import net.minecraft.client.model.animal.cow.ColdCowModel;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.animal.cow.WarmCowModel;
import net.minecraft.client.model.animal.dolphin.DolphinModel;
import net.minecraft.client.model.animal.equine.DonkeyModel;
import net.minecraft.client.model.animal.feline.AdultCatModel;
import net.minecraft.client.model.animal.feline.AdultFelineModel;
import net.minecraft.client.model.animal.fox.AdultFoxModel;
import net.minecraft.client.model.animal.goat.GoatModel;
import net.minecraft.client.model.animal.llama.LlamaModel;
import net.minecraft.client.model.animal.panda.PandaModel;
import net.minecraft.client.model.animal.pig.ColdPigModel;
import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.model.animal.polarbear.PolarBearModel;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.animal.sniffer.SnifferModel;
import net.minecraft.client.model.animal.squid.SquidModel;
import net.minecraft.client.model.animal.turtle.AdultTurtleModel;
import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.model.monster.hoglin.HoglinModel;
import net.minecraft.client.model.monster.piglin.AdultPiglinModel;
import net.minecraft.client.model.monster.piglin.AdultZombifiedPiglinModel;
import net.minecraft.client.model.monster.piglin.PiglinModel;
import net.minecraft.client.model.monster.strider.AdultStriderModel;
import net.minecraft.client.model.monster.zombie.DrownedModel;
import net.minecraft.client.model.monster.zombie.ZombieVillagerModel;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LayerDefinitionOverrides {

    private record Entry(ModelLayerLocation location, LayerDefinition definition, MeshTransformer babyTransformer, EntityType<?> configKey) {

        boolean tryApply(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> mapBuilder, ModelLayerLocation currentLocation) {
            if (currentLocation.equals(location) && OldBabies.getConfig().isEntityEnabled(configKey)) {
                apply(mapBuilder);
                return true;
            }
            return false;
        }

        void apply(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> mapBuilder) {
            mapBuilder.put(location, definition.apply(babyTransformer));
        }

    }

    private record ArmorEntry(ArmorModelSet<ModelLayerLocation> location, ArmorModelSet<LayerDefinition> armorModelSet, EntityType<?> configKey) {

        boolean tryApply(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> mapBuilder) {
            if (OldBabies.getConfig().isEntityEnabled(configKey)) {
                apply(mapBuilder);
                return true;
            }
            return false;
        }

        void apply(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> mapBuilder) {
            this.location.putFrom(armorModelSet, mapBuilder);
        }

    }

    private static final MeshTransformer villagerLikeScale = MeshTransformer.scaling(0.9375F);

    private static final LayerDefinition donkeyLayerDefinition = LayerDefinition.create(OldBabies.createFullScaleEquineBabyMesh(CubeDeformation.NONE), 64, 64).apply(DonkeyModel.DONKEY_TRANSFORMER);
    private static final LayerDefinition squidLayerDefinition = SquidModel.createBodyLayer();
    private static final LayerDefinition humanoidLayer = LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64);

    private static final LayerDefinition villagerLayer = LayerDefinition.create(VillagerModel.createBodyModel(), 64, 64).apply(villagerLikeScale);
    private static final LayerDefinition villagerNoHatLayer = LayerDefinition.create(VillagerModel.createNoHatModel(), 64, 64).apply(villagerLikeScale);

    private static Map<ModelLayerLocation, LayerDefinition> oldLayerDefinitions;

    private static final List<Entry> overrides = new ArrayList<>(){{
        add(new Entry(ModelLayers.SHEEP_BABY, SheepModel.createBodyLayer(), Constants.SHEEP_BABY_TRANSFORMER, EntityType.SHEEP));
        add(new Entry(ModelLayers.SHEEP_BABY_WOOL, SheepFurModel.createFurLayer(), Constants.SHEEP_BABY_TRANSFORMER, EntityType.SHEEP));
        add(new Entry(ModelLayers.CHICKEN_BABY, AdultChickenModel.createBodyLayer(), Constants.CHICKEN_BABY_TRANSFORMER, EntityType.CHICKEN));
        add(new Entry(ModelLayers.PIG_BABY, PigModel.createBodyLayer(CubeDeformation.NONE), PigModel.BABY_TRANSFORMER, EntityType.PIG));
        add(new Entry(ModelLayers.COW_BABY, CowModel.createBodyLayer(), CowModel.BABY_TRANSFORMER, EntityType.COW));
        add(new Entry(ModelLayers.WARM_COW_BABY, WarmCowModel.createBodyLayer(), CowModel.BABY_TRANSFORMER, EntityType.COW));
        add(new Entry(ModelLayers.COLD_COW_BABY, ColdCowModel.createBodyLayer(), CowModel.BABY_TRANSFORMER, EntityType.COW));
        add(new Entry(ModelLayers.MOOSHROOM_BABY, CowModel.createBodyLayer(), CowModel.BABY_TRANSFORMER, EntityType.MOOSHROOM));
        add(new Entry(ModelLayers.WOLF_BABY, LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32), Constants.DEFAULT_BABY_TRANSFORMER, EntityType.WOLF));
        add(new Entry(ModelLayers.CAT_BABY, LayerDefinition.create(AdultCatModel.createBodyMesh(CubeDeformation.NONE), 64, 32), Constants.FELINE_BABY_TRANSFORMER, EntityType.CAT));
        add(new Entry(ModelLayers.CAT_BABY_COLLAR, LayerDefinition.create(AdultFelineModel.createBodyMesh(AdultCatModel.COLLAR_DEFORMATION), 64, 32).apply(AdultCatModel.CAT_TRANSFORMER), Constants.FELINE_BABY_TRANSFORMER, EntityType.CAT));
        add(new Entry(ModelLayers.OCELOT_BABY, LayerDefinition.create(AdultFelineModel.createBodyMesh(CubeDeformation.NONE), 64, 32), Constants.FELINE_BABY_TRANSFORMER, EntityType.OCELOT));
        add(new Entry(ModelLayers.HORSE_BABY, LayerDefinition.create(OldBabies.createFullScaleEquineBabyMesh(CubeDeformation.NONE), 64, 64), Constants.EQUINE_BABY_TRANSFORMER, EntityType.HORSE));
        add(new Entry(ModelLayers.DONKEY_BABY, donkeyLayerDefinition.apply(MeshTransformer.scaling(0.87F)), Constants.EQUINE_BABY_TRANSFORMER, EntityType.DONKEY));
        add(new Entry(ModelLayers.MULE_BABY, donkeyLayerDefinition.apply(MeshTransformer.scaling(0.92F)), Constants.EQUINE_BABY_TRANSFORMER, EntityType.DONKEY));
        add(new Entry(ModelLayers.SQUID_BABY, squidLayerDefinition, Constants.GENERIC_SHRINK_BABY_TRANSFORMER, EntityType.SQUID));
        add(new Entry(ModelLayers.GLOW_SQUID_BABY, squidLayerDefinition, Constants.GENERIC_SHRINK_BABY_TRANSFORMER, EntityType.SQUID));
        add(new Entry(ModelLayers.DOLPHIN_BABY, DolphinModel.createBodyLayer(), Constants.GENERIC_SHRINK_BABY_TRANSFORMER, EntityType.DOLPHIN));
        add(new Entry(ModelLayers.AXOLOTL_BABY, AdultAxolotlModel.createBodyLayer(), Constants.GENERIC_SHRINK_BABY_TRANSFORMER, EntityType.AXOLOTL));
        add(new Entry(ModelLayers.TURTLE_BABY, AdultTurtleModel.createBodyLayer(), Constants.TURTLE_BABY_TRANSFORMER, EntityType.TURTLE));
        add(new Entry(ModelLayers.BEE_BABY, AdultBeeModel.createBodyLayer(), Constants.GENERIC_SHRINK_BABY_TRANSFORMER, EntityType.BEE));
        add(new Entry(ModelLayers.FOX_BABY, AdultFoxModel.createBodyLayer(), Constants.FOX_BABY_TRANSFORMER, EntityType.FOX));
        add(new Entry(ModelLayers.GOAT_BABY, GoatModel.createBodyLayer(), Constants.GOAT_BABY_TRANSFORMER, EntityType.GOAT));
        add(new Entry(ModelLayers.ARMADILLO_BABY, AdultArmadilloModel.createBodyLayer(), Constants.ARMADILLO_BABY_TRANSFORMER, EntityType.ARMADILLO));
        add(new Entry(ModelLayers.CAMEL_BABY, AdultCamelModel.createBodyLayer(), Constants.CAMEL_BABY_TRANSFORMER, EntityType.CAMEL));
        add(new Entry(ModelLayers.POLAR_BEAR_BABY, PolarBearModel.createBodyLayer(), Constants.POLAR_BEAR_BABY_TRANSFORMER, EntityType.POLAR_BEAR));
        add(new Entry(ModelLayers.LLAMA_BABY, LlamaModel.createBodyLayer(CubeDeformation.NONE), OldBabies::createLlamaBabyTransformer, EntityType.LLAMA));
        add(new Entry(ModelLayers.TRADER_LLAMA_BABY, LlamaModel.createBodyLayer(CubeDeformation.NONE), OldBabies::createLlamaBabyTransformer, EntityType.LLAMA));
        add(new Entry(ModelLayers.LLAMA_BABY_DECOR, LlamaModel.createBodyLayer(new CubeDeformation(0.5f)), OldBabies::createLlamaBabyTransformer, EntityType.LLAMA));
        add(new Entry(ModelLayers.ZOMBIE_BABY, humanoidLayer, Constants.HUMANOID_BABY_TRANSFORMER, EntityType.ZOMBIE));
        add(new Entry(ModelLayers.DROWNED_BABY, DrownedModel.createBodyLayer(CubeDeformation.NONE), Constants.HUMANOID_BABY_TRANSFORMER, EntityType.DROWNED));
        add(new Entry(ModelLayers.DROWNED_BABY_OUTER_LAYER, DrownedModel.createBodyLayer(new CubeDeformation(0.25F)), Constants.HUMANOID_BABY_TRANSFORMER, EntityType.DROWNED));
        add(new Entry(ModelLayers.HUSK_BABY, humanoidLayer.apply(MeshTransformer.scaling(1.0625F)), Constants.HUMANOID_BABY_TRANSFORMER, EntityType.ZOMBIE));
        add(new Entry(ModelLayers.VILLAGER_BABY, villagerLayer, Constants.GENERIC_SHRINK_BABY_TRANSFORMER, EntityType.VILLAGER));
        add(new Entry(ModelLayers.VILLAGER_BABY_NO_HAT, villagerNoHatLayer, Constants.GENERIC_SHRINK_BABY_TRANSFORMER, EntityType.VILLAGER));
        add(new Entry(ModelLayers.ZOMBIE_VILLAGER_BABY, ZombieVillagerModel.createBodyLayer(), Constants.HUMANOID_BABY_TRANSFORMER, EntityType.VILLAGER));
        add(new Entry(ModelLayers.ZOMBIE_VILLAGER_BABY_NO_HAT, ZombieVillagerModel.createNoHatLayer(), Constants.HUMANOID_BABY_TRANSFORMER, EntityType.VILLAGER));
        add(new Entry(ModelLayers.PIGLIN_BABY, AdultPiglinModel.createBodyLayer(), Constants.HUMANOID_BABY_TRANSFORMER, EntityType.PIGLIN));
        add(new Entry(ModelLayers.ZOMBIFIED_PIGLIN_BABY, AdultZombifiedPiglinModel.createBodyLayer(), Constants.HUMANOID_BABY_TRANSFORMER, EntityType.ZOMBIFIED_PIGLIN));
        add(new Entry(ModelLayers.HOGLIN_BABY, HoglinModel.createBodyLayer(), Constants.HOGLIN_BABY_TRANSFORMER, EntityType.HOGLIN));
        add(new Entry(ModelLayers.ZOGLIN_BABY, HoglinModel.createBodyLayer(), Constants.HOGLIN_BABY_TRANSFORMER, EntityType.HOGLIN));
        add(new Entry(ModelLayers.PANDA_BABY, PandaModel.createBodyLayer(), Constants.PANDA_BABY_TRANSFORMER, EntityType.PANDA));
        add(new Entry(ModelLayers.SNIFFER_BABY, SnifferModel.createBodyLayer(), Constants.GENERIC_SHRINK_BABY_TRANSFORMER, EntityType.SNIFFER));
        add(new Entry(ModelLayers.STRIDER_BABY, AdultStriderModel.createBodyLayer(), Constants.GENERIC_SHRINK_BABY_TRANSFORMER, EntityType.STRIDER));
    }};


    private static final List<Entry> additionalEntries = new ArrayList<>(){{
        add(new Entry(Constants.SHEEP_BABY_WOOL_UNDERCOAT, SheepModel.createBodyLayer(), Constants.SHEEP_BABY_TRANSFORMER, EntityType.SHEEP));
        add(new Entry(Constants.COLD_CHICKEN_BABY, ColdChickenModel.createBodyLayer(), Constants.CHICKEN_BABY_TRANSFORMER, EntityType.CHICKEN));
        add(new Entry(Constants.COLD_PIG_BABY, ColdPigModel.createBodyLayer(CubeDeformation.NONE), PigModel.BABY_TRANSFORMER, EntityType.PIG));
    }};

    private static final ArmorModelSet<LayerDefinition> humanoidArmor = HumanoidModel.createArmorMeshSet(new CubeDeformation(0.5F), new CubeDeformation(1.0f)).map((mesh) -> LayerDefinition.create(mesh, 64, 32));
    private static final ArmorModelSet<LayerDefinition> babyHumanoidArmor = humanoidArmor.map((layerDefinition) -> layerDefinition.apply(Constants.HUMANOID_BABY_TRANSFORMER));
    private static final ArmorModelSet<LayerDefinition> piglinArmor = PiglinModel.createArmorMeshSet(new CubeDeformation(0.5F), new CubeDeformation(1.02F)).map((mesh) -> LayerDefinition.create(mesh, 64, 32));
    private static final ArmorModelSet<LayerDefinition> babyPiglinArmor = piglinArmor.map((layerDefinition) -> layerDefinition.apply(Constants.HUMANOID_BABY_TRANSFORMER));


    private static final List<ArmorEntry> armorEntries = new ArrayList<>(){{
        add(new ArmorEntry(ModelLayers.ZOMBIE_BABY_ARMOR, babyHumanoidArmor, EntityType.ZOMBIE));
        add(new ArmorEntry(ModelLayers.ZOMBIE_VILLAGER_BABY_ARMOR, babyHumanoidArmor, EntityType.ZOMBIE_VILLAGER));
        add(new ArmorEntry(ModelLayers.HUSK_BABY_ARMOR, babyHumanoidArmor, EntityType.ZOMBIE));
        add(new ArmorEntry(ModelLayers.DROWNED_BABY_ARMOR, babyHumanoidArmor, EntityType.DROWNED));
        add(new ArmorEntry(ModelLayers.PIGLIN_BABY_ARMOR, babyPiglinArmor, EntityType.PIGLIN));
        add(new ArmorEntry(ModelLayers.ZOMBIFIED_PIGLIN_BABY_ARMOR, babyPiglinArmor, EntityType.ZOMBIFIED_PIGLIN));
    }};

    public static List<EntityType<?>> getLayerDefinitionTypes() {
        List<EntityType<?>> list = new ArrayList<>();
        for (Entry entry : overrides) {
            list.add(entry.configKey());
        }
        return list;
    }

    public static LayerDefinition getOldLayerDefinition(ModelLayerLocation location) {
        return oldLayerDefinitions.get(location);
    }

    public static ImmutableMap<ModelLayerLocation, LayerDefinition> rebuildMap(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> originalBuilder) {
        ImmutableMap<ModelLayerLocation, LayerDefinition> originalMap = originalBuilder.build();
        oldLayerDefinitions = originalMap;
        ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> newMap = new ImmutableMap.Builder<>();
        List<ArmorModelSet<ModelLayerLocation>> armorModelSets = new ArrayList<>();
        for (ArmorEntry entry : armorEntries) {
            if (entry.tryApply(newMap)) armorModelSets.add(entry.location);
        }
        for (ModelLayerLocation location : originalMap.keySet()) {
            boolean overridden = false;
            for (ArmorModelSet<ModelLayerLocation> set : armorModelSets) {
                if (set.head() == location || set.chest() == location || set.legs() == location || set.feet() == location) {
                    overridden = true;
                    break;
                }
            }
            if (overridden) continue;
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

package io.github.boid.oldBabies;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.armadillo.AdultArmadilloModel;
import net.minecraft.client.model.animal.armadillo.BabyArmadilloModel;
import net.minecraft.client.model.animal.axolotl.AdultAxolotlModel;
import net.minecraft.client.model.animal.axolotl.BabyAxolotlModel;
import net.minecraft.client.model.animal.bee.AdultBeeModel;
import net.minecraft.client.model.animal.bee.BabyBeeModel;
import net.minecraft.client.model.animal.camel.AdultCamelModel;
import net.minecraft.client.model.animal.camel.BabyCamelModel;
import net.minecraft.client.model.animal.equine.BabyDonkeyModel;
import net.minecraft.client.model.animal.equine.BabyHorseModel;
import net.minecraft.client.model.animal.equine.DonkeyModel;
import net.minecraft.client.model.animal.equine.HorseModel;
import net.minecraft.client.model.animal.feline.AdultCatModel;
import net.minecraft.client.model.animal.feline.AdultOcelotModel;
import net.minecraft.client.model.animal.feline.BabyCatModel;
import net.minecraft.client.model.animal.feline.BabyOcelotModel;
import net.minecraft.client.model.animal.fox.AdultFoxModel;
import net.minecraft.client.model.animal.fox.BabyFoxModel;
import net.minecraft.client.model.animal.goat.BabyGoatModel;
import net.minecraft.client.model.animal.goat.GoatModel;
import net.minecraft.client.model.animal.llama.BabyLlamaModel;
import net.minecraft.client.model.animal.llama.LlamaModel;
import net.minecraft.client.model.animal.panda.BabyPandaModel;
import net.minecraft.client.model.animal.panda.PandaModel;
import net.minecraft.client.model.animal.polarbear.BabyPolarBearModel;
import net.minecraft.client.model.animal.polarbear.PolarBearModel;
import net.minecraft.client.model.animal.sniffer.SnifferModel;
import net.minecraft.client.model.animal.sniffer.SniffletModel;
import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.animal.wolf.BabyWolfModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.hoglin.BabyHoglinModel;
import net.minecraft.client.model.monster.hoglin.HoglinModel;
import net.minecraft.client.model.monster.piglin.*;
import net.minecraft.client.model.monster.strider.AdultStriderModel;
import net.minecraft.client.model.monster.strider.BabyStriderModel;
import net.minecraft.client.model.monster.zombie.*;
import net.minecraft.client.model.npc.BabyVillagerModel;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ModelSwappers {

    private record Swapper(Class<?> originalModel, Class<?> newModel, ModelLayerLocation modelLayer, EntityType<?> configKey) {

        @SuppressWarnings("unchecked")
        private <M extends EntityModel<?>> Optional<M> apply(EntityRendererProvider.Context context, Class<M> inputModel) {
            try {
                if (originalModel.equals(inputModel) && OldBabies.getConfig().isEntityEnabled(configKey)) {
                    List<Constructor<M>> constructors = Arrays.stream((Constructor<M>[]) newModel.getConstructors()).toList();
                    if (!constructors.isEmpty()) {
                        for (Constructor<?> constructor : constructors) {
                            try {
                                M model = (M) constructor.newInstance(context.bakeLayer(modelLayer));
                                return Optional.of(model);
                            } catch (Exception exception) {
                                OldBabies.LOGGER.error(exception.toString());
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                sendErrorMessage(inputModel, exception);
            }
            return Optional.empty();
        }

    }

    private static final List<Swapper> MODEL_SWAPPERS = new ArrayList<>(){{
        add(new Swapper(BabyAxolotlModel.class, AdultAxolotlModel.class, ModelLayers.AXOLOTL_BABY, EntityType.AXOLOTL));
        add(new Swapper(BabyBeeModel.class, AdultBeeModel.class, ModelLayers.BEE_BABY, EntityType.BEE));
        add(new Swapper(BabyFoxModel.class, AdultFoxModel.class, ModelLayers.FOX_BABY, EntityType.FOX));
        add(new Swapper(BabyGoatModel.class, GoatModel.class, ModelLayers.GOAT_BABY, EntityType.GOAT));
        add(new Swapper(BabyCatModel.class, AdultCatModel.class, ModelLayers.CAT_BABY, EntityType.CAT));
        add(new Swapper(BabyOcelotModel.class, AdultOcelotModel.class, ModelLayers.OCELOT_BABY, EntityType.OCELOT));
        add(new Swapper(BabyWolfModel.class, AdultWolfModel.class, ModelLayers.WOLF_BABY, EntityType.WOLF));
        add(new Swapper(BabyArmadilloModel.class, AdultArmadilloModel.class, ModelLayers.ARMADILLO_BABY, EntityType.ARMADILLO));
        add(new Swapper(BabyCamelModel.class, AdultCamelModel.class, ModelLayers.CAMEL_BABY, EntityType.CAMEL));
        add(new Swapper(BabyPolarBearModel.class, PolarBearModel.class, ModelLayers.POLAR_BEAR_BABY, EntityType.POLAR_BEAR));
        add(new Swapper(BabyLlamaModel.class, LlamaModel.class, ModelLayers.LLAMA_BABY, EntityType.LLAMA));
        add(new Swapper(BabyHorseModel.class, HorseModel.class, ModelLayers.HORSE_BABY, EntityType.HORSE));
        add(new Swapper(BabyPiglinModel.class, AdultPiglinModel.class, ModelLayers.PIGLIN_BABY, EntityType.PIGLIN));
        add(new Swapper(BabyZombifiedPiglinModel.class, AdultZombifiedPiglinModel.class, ModelLayers.ZOMBIFIED_PIGLIN_BABY, EntityType.ZOMBIFIED_PIGLIN));
        add(new Swapper(BabyVillagerModel.class, VillagerModel.class, ModelLayers.VILLAGER_BABY, EntityType.VILLAGER));
        add(new Swapper(BabyZombieModel.class, ZombieModel.class, ModelLayers.ZOMBIE_BABY, EntityType.ZOMBIE));
        add(new Swapper(BabyZombieVillagerModel.class, ZombieVillagerModel.class, ModelLayers.ZOMBIE_VILLAGER_BABY, EntityType.VILLAGER));
        add(new Swapper(BabyDrownedModel.class, DrownedModel.class, ModelLayers.DROWNED_BABY, EntityType.DROWNED));
        add(new Swapper(BabyDonkeyModel.class, DonkeyModel.class, ModelLayers.DONKEY_BABY, EntityType.DONKEY));
        add(new Swapper(BabyHoglinModel.class, HoglinModel.class, ModelLayers.HOGLIN_BABY, EntityType.HOGLIN));
        add(new Swapper(BabyPandaModel.class, PandaModel.class, ModelLayers.PANDA_BABY, EntityType.PANDA));
        add(new Swapper(SniffletModel.class, SnifferModel.class, ModelLayers.SNIFFER_BABY, EntityType.SNIFFER));
        add(new Swapper(BabyStriderModel.class, AdultStriderModel.class, ModelLayers.STRIDER_BABY, EntityType.STRIDER));
    }};

    public static List<EntityType<?>> getSwapperTypes() {
        List<EntityType<?>> list = new ArrayList<>();
        for (Swapper swapper : MODEL_SWAPPERS) {
            list.add(swapper.configKey());
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static Optional<EntityModel<?>> tryReplaceEntityModel(EntityRendererProvider.Context context, Class<? extends EntityModel<?>> inputModel) {
        for (Swapper swapper : MODEL_SWAPPERS) {
            try {
                Optional<EntityModel<?>> model = (Optional<EntityModel<?>>) swapper.apply(context, inputModel);
                if (model.isPresent()) {
                    return model;
                }
            } catch (Exception exception) {
                sendErrorMessage(inputModel, exception);
            }
        }
        return Optional.empty();
    }

    private static void sendErrorMessage(Class<?> inputClass, Exception exception) {
        OldBabies.LOGGER.warn("Error trying to create swapped entity model for {}: {}", inputClass.getName(), exception.getMessage());
    }

}

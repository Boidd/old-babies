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
import net.minecraft.client.model.animal.polarbear.BabyPolarBearModel;
import net.minecraft.client.model.animal.polarbear.PolarBearModel;
import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.animal.wolf.BabyWolfModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ModelSwappers {

    private record Swapper(Class<? extends EntityModel<?>> originalModel, Class<? extends EntityModel<?>> newModel, ModelLayerLocation modelLayer) {

        @SuppressWarnings("unchecked")
        private <M extends EntityModel<?>> Optional<M> apply(EntityRendererProvider.Context context, Class<M> inputModel) {
            try {
                if (originalModel.equals(inputModel)) {
                    List<Constructor<M>> constructors = Arrays.stream((Constructor<M>[]) newModel.getConstructors()).toList();
                    if (!constructors.isEmpty()) {
                        for (Constructor<?> constructor : constructors) {
                            try {
                                return Optional.of((M) constructor.newInstance(context.bakeLayer(modelLayer)));
                            } catch (Exception exception) {
                                //not the right constructor if there are multiple, ignore this
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
        add(new Swapper(BabyAxolotlModel.class, AdultAxolotlModel.class, ModelLayers.AXOLOTL_BABY));
        add(new Swapper(BabyBeeModel.class, AdultBeeModel.class, ModelLayers.BEE_BABY));
        add(new Swapper(BabyFoxModel.class, AdultFoxModel.class, ModelLayers.FOX_BABY));
        add(new Swapper(BabyGoatModel.class, GoatModel.class, ModelLayers.GOAT_BABY));
        add(new Swapper(BabyCatModel.class, AdultCatModel.class, ModelLayers.CAT_BABY));
        add(new Swapper(BabyOcelotModel.class, AdultOcelotModel.class, ModelLayers.OCELOT_BABY));
        add(new Swapper(BabyWolfModel.class, AdultWolfModel.class, ModelLayers.WOLF_BABY));
        add(new Swapper(BabyArmadilloModel.class, AdultArmadilloModel.class, ModelLayers.ARMADILLO_BABY));
        add(new Swapper(BabyCamelModel.class, AdultCamelModel.class, ModelLayers.CAMEL_BABY));
        add(new Swapper(BabyPolarBearModel.class, PolarBearModel.class, ModelLayers.POLAR_BEAR_BABY));
        add(new Swapper(BabyLlamaModel.class, LlamaModel.class, ModelLayers.LLAMA_BABY));
        add(new Swapper(BabyLlamaModel.class, LlamaModel.class, ModelLayers.TRADER_LLAMA_BABY));
    }};

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

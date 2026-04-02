package io.github.boid.oldBabies;

import io.github.boid.oldBabies.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

import static net.minecraft.client.model.animal.equine.AbstractEquineModel.createBodyMesh;

public class OldBabies implements ClientModInitializer {

    public static final String MOD_ID = "old_babies";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    protected static final String CONFIG_PATH = MOD_ID+".json";
    private static Path RESOLVED_CONFIG_PATH;
    private static Config config;

    private static final Map<EntityType<?>, EntityType<?>> typeMatchers = new HashMap<>(){{
        put(EntityType.TRADER_LLAMA, EntityType.LLAMA);
        put(EntityType.MULE, EntityType.DONKEY);
        put(EntityType.ZOMBIE_VILLAGER, EntityType.VILLAGER);
        put(EntityType.HUSK, EntityType.ZOMBIE);
        put(EntityType.ZOMBIE_HORSE, EntityType.HORSE);
        put(EntityType.SKELETON_HORSE, EntityType.HORSE);
        put(EntityType.ZOGLIN, EntityType.HOGLIN);
        put(EntityType.GLOW_SQUID, EntityType.SQUID);
    }};

    private static final Set<EntityType<?>> typesToRevert = new HashSet<>(){{
        addAll(Config.getConfigurableEntities());
        addAll(typeMatchers.keySet());
    }};

    public static boolean isRevertibleType(EntityType<?> type) {
        return typesToRevert.contains(type);
    }

    public static Set<EntityType<?>> getAdditionalTypes(EntityType<?> original) {
        Set<EntityType<?>> set = new HashSet<>();
        for (EntityType<?> key : typeMatchers.keySet()) {
            if (typeMatchers.get(key) == original) set.add(key);
        }
        return set;
    }

    public static Config getConfig() {
        return config;
    }

    public static Path getResolvedConfigPath() {
        return RESOLVED_CONFIG_PATH;
    }

    @Override
    public void onInitializeClient() {
        RESOLVED_CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_PATH);
        LOGGER.info("Initializing old babies");
        config = new Config(RESOLVED_CONFIG_PATH);
    }
    public static EntityType<?> getConfigType(EntityType<?> originalType) {
        EntityType<?> configType = originalType;
        if (typeMatchers.containsKey(configType)) {
            configType = typeMatchers.get(configType);
        }
        return configType;
    }

    public static Identifier removeBabyFromIdentifier(Identifier identifier, EntityType<?> entityType) {
        EntityType<?> typeToTest = getConfigType(entityType);
        if (OldBabies.getConfig().isEntityEnabled(typeToTest)) {
            return removeBabyFromIdentifier(identifier);
        }
        return identifier;
    }

    public static Identifier removeBabyFromIdentifier(Identifier identifier) {
        String path = identifier.getPath();
        if (identifier.getNamespace().equals("minecraft")) {
            path = fixVillagerTextures(path);
            path = path.replaceAll("_baby", "");
            path = path.replaceAll("baby/", "");
            path = path.replaceAll("baby", "");
            path = path.replaceAll("snifflet", "sniffer");
            path = fixPandaTextures(path);
        }
        return Identifier.fromNamespaceAndPath(identifier.getNamespace(), path);
    }

    private static String fixVillagerTextures(String path) {
        if (path.contains("villager")) {
            path = path.replaceAll("baby/", "type/");
        }
        return path;
    }

    private static String fixPandaTextures(String path) {
        if (path.contains("panda")) {
            path = path.replaceAll("aggressive_panda", "panda_aggressive");
            path = path.replaceAll("brown_panda", "panda_brown");
            path = path.replaceAll("lazy_panda", "panda_lazy");
            path = path.replaceAll("playful_panda", "panda_playful");
            path = path.replaceAll("weak_panda", "panda_weak");
            path = path.replaceAll("worried_panda", "panda_worried");
        }
        return path;
    }

    public static MeshDefinition createFullScaleEquineBabyMesh(final CubeDeformation g) {
        MeshDefinition mesh = createBodyMesh(g);
        PartDefinition root = mesh.getRoot();
        CubeDeformation babyLegFudge = g.extend(0.0F, 5.5F, 0.0F);
        root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, babyLegFudge), PartPose.offset(4.0F, 14.0F, 7.0F));
        root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(48, 21).addBox(-1.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, babyLegFudge), PartPose.offset(-4.0F, 14.0F, 7.0F));
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, babyLegFudge), PartPose.offset(4.0F, 14.0F, -10.0F));
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(48, 21).addBox(-1.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, babyLegFudge), PartPose.offset(-4.0F, 14.0F, -10.0F));
        return mesh;
    }

    public static MeshDefinition createLlamaBabyTransformer(MeshDefinition meshDefinition) {
        UnaryOperator<PartPose> unaryOperator = partPose -> partPose.translated(0.0F, 21.0F, 3.52F).scaled(0.71428573F, 0.64935064F, 0.7936508F);
        UnaryOperator<PartPose> unaryOperator2 = partPose -> partPose.translated(0.0F, 33.0F, 0.0F).scaled(0.625F, 0.45454544F, 0.45454544F);
        UnaryOperator<PartPose> unaryOperator3 = partPose -> partPose.translated(0.0F, 33.0F, 0.0F).scaled(0.45454544F, 0.41322312F, 0.45454544F);
        MeshDefinition meshDefinition2 = new MeshDefinition();

        for (Map.Entry<String, PartDefinition> entry : meshDefinition.getRoot().getChildren()) {
            String string = entry.getKey();
            PartDefinition partDefinition = entry.getValue();

            UnaryOperator<PartPose> unaryOperator4 = switch (string) {
                case "head" -> unaryOperator;
                case "body" -> unaryOperator2;
                default -> unaryOperator3;
            };
            meshDefinition2.getRoot().addOrReplaceChild(string, partDefinition.transformed(unaryOperator4));
        }

        return meshDefinition2;
    }

}

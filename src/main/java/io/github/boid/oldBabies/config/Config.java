package io.github.boid.oldBabies.config;

import com.google.gson.*;
import io.github.boid.oldBabies.LayerDefinitionOverrides;
import io.github.boid.oldBabies.ModelSwappers;
import io.github.boid.oldBabies.OldBabies;
import net.minecraft.world.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Config {

    private final Set<EntityType<?>> disabledEntityTypes = new HashSet<>();

    private final Path configPath;

    public void enableEntity(EntityType<?> entityType) {
        disabledEntityTypes.remove(entityType);
    }

    public void disableEntity(EntityType<?> entityType) {
        disabledEntityTypes.add(entityType);
    }

    public static Set<EntityType<?>> getConfigurableEntities() {
        Set<EntityType<?>> set = new HashSet<>();
        set.addAll(ModelSwappers.getSwapperTypes());
        set.addAll(LayerDefinitionOverrides.getLayerDefinitionTypes());
        return set;
    }

    public boolean isEntityEnabled(EntityType<?> entityType) {
        return !disabledEntityTypes.contains(entityType);
    }

    public Config(Path path) {
        this.configPath = path;
        reload();
    }

    public void setFrom(Config config) {
        this.disabledEntityTypes.clear();
        this.disabledEntityTypes.addAll(config.disabledEntityTypes);
    }

    public boolean matches(Config config) {
        return this.disabledEntityTypes.equals(config.disabledEntityTypes);
    }

    public void reload() {
        Path path = this.configPath;
        disabledEntityTypes.clear();
        try {
            File file = new File(path.toUri());
            if (file.exists()) {
                String data = Files.readString(path);
                JsonObject json = JsonParser.parseString(data).getAsJsonObject();
                JsonArray disabledEntitiesArray = json.get("disabled_entities").getAsJsonArray();
                for (JsonElement element : disabledEntitiesArray.asList()) {
                    String entity = element.getAsString();
                    Optional<EntityType<?>> type = EntityType.byString(entity);
                    type.ifPresent(disabledEntityTypes::add);
                }
            } else {
                saveToPath(path);
            }
        } catch (IOException ioException) {
            OldBabies.LOGGER.error("Error loading config: {}", ioException.getMessage());
        }
    }

    public void saveToPath(Path path) {
        JsonObject jsonObject = new JsonObject();
        JsonArray list = new JsonArray();
        for (EntityType<?> disabled : disabledEntityTypes) {
            list.add(EntityType.getKey(disabled).toString());
        }
        jsonObject.add("disabled_entities", list);
        Gson gson = new Gson();
        String data = gson.toJson(jsonObject);
        try {
            Files.writeString(path, data, StandardCharsets.UTF_8);
        } catch (IOException ioException) {
            OldBabies.LOGGER.error("Failed to save config: {}", ioException.getMessage());
        }
    }

}

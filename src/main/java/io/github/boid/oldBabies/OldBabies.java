package io.github.boid.oldBabies;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.UnaryOperator;

import static net.minecraft.client.model.animal.equine.AbstractEquineModel.createBodyMesh;

public class OldBabies implements ClientModInitializer {

    public static final String MOD_ID = "old_babies";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {}

    public static Identifier removeBabyFromIdentifier(Identifier identifier) {
        String path = identifier.getPath();
        if (identifier.getNamespace().equals("minecraft") && !path.contains("rabbit")) {
            path = path.replaceAll("_baby", "");
            path = path.replaceAll("baby", "");
        }
        return Identifier.fromNamespaceAndPath(identifier.getNamespace(), path);
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

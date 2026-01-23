package io.github.boid.oldBabies;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

import static net.minecraft.client.model.animal.equine.AbstractEquineModel.createBodyMesh;

public class OldBabies implements ClientModInitializer {

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

}

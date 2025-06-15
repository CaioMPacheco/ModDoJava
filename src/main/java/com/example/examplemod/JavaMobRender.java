package com.example.examplemod;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class JavaMobRender extends MobRenderer<JavaMob, HumanoidModel<JavaMob>> {

    public JavaMobRender(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(JavaMob mob) {
        return new ResourceLocation(MeuMod.MODID, "textures/entity/javamob.png");
    }

}

package com.example.examplemod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(MeuMod.MODID)
public class MeuMod {

    public static final String MODID = "examplemod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));

    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () ->
            new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> ESPADA_PYTHON = ITEMS.register("espadapython",
            () -> new SwordItem(Tiers.IRON, 50, -2.4F, new Item.Properties()));

    public static final RegistryObject<EntityType<JavaMob>> JAVA_MOB = ENTITY_TYPES.register("java_mob", () ->
            EntityType.Builder.of(JavaMob::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .build("java_mob"));

    public static final RegistryObject<Item> JAVA_MOB_SPAWN_EGG = ITEMS.register("java_mob_spawn_egg", () ->
            new ForgeSpawnEggItem(JAVA_MOB, 0x5C4033, 0xFFC857, new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> MEUMOD_TAB = CREATIVE_MODE_TABS.register("meumod_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.examplemod"))
                    .icon(() -> ESPADA_PYTHON.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(ESPADA_PYTHON.get());
                        output.accept(JAVA_MOB_SPAWN_EGG.get());
                        output.accept(EXAMPLE_BLOCK_ITEM.get());
                    })
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .build());

    public MeuMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Setup iniciado com sucesso!");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(JAVA_MOB_SPAWN_EGG.get());
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ESPADA_PYTHON.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Servidor está iniciando...");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void onAttributeCreate(EntityAttributeCreationEvent event) {
            event.put(JAVA_MOB.get(), JavaMob.createAttributes().build());
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Setup do cliente concluído!");
            LOGGER.info("Jogador: {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(JAVA_MOB.get(), JavaMobRender::new);
        }
    }
}

package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ProjectLinks;
import com.tristankechlo.additionalredstone.init.*;
import com.tristankechlo.additionalredstone.network.PacketHandler;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.tristankechlo.additionalredstone.init.ModRecipes.CIRCUIT_MAKER_RECIPE_TYPE;

@Mod(AdditionalRedstone.MOD_ID)
public class AdditionalRedstone {

    public static final String MOD_NAME = "AdditionalRedstone";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final String MOD_ID = "additionalredstone";
    public static final boolean[][] INPUT_STATES = new boolean[][]{{false, false, false}, {false, false, true}, {false, true, false}, {false, true, true}, {true, false, false}, {true, false, true}, {true, true, false}, {true, true, true}};
    public static boolean JEI_LOADED = false;
    public static final ItemGroup GENERAL = new ModItemGroup();

    public AdditionalRedstone() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PacketHandler.registerPackets();

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModTileEntities.TILE_ENTITIES.register(modEventBus);
        ModContainer.CONTAINER_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        Registry.register(Registry.RECIPE_TYPE, CircuitMakerRecipe.TYPE_ID, CIRCUIT_MAKER_RECIPE_TYPE);

        MinecraftForge.EVENT_BUS.addListener(ProjectLinks::registerAsCommand);
        MinecraftForge.EVENT_BUS.addListener(AdditionalRedstone::getItemFuelTime);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static void getItemFuelTime(final FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().getItem().equals(ModItems.LED.get())) {
            event.setBurnTime(300);
        }
    }

}
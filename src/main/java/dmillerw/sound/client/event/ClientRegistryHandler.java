package dmillerw.sound.client.event;

import dmillerw.sound.core.block.ModBlocks;
import dmillerw.sound.core.item.ModItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by dmillerw
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientRegistryHandler {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent evt) {
        registerItemBlockModel(ModBlocks.sound_muffler_item, "inventory");

        registerItemModel(ModItems.earplug);
        registerItemModel(ModItems.magical_earplugs);
//        registerItemModel(ModItems.mysterious_earplugs);
    }

    private static void registerItemBlockModel(Item item, String tag) {
        ModelResourceLocation resourceLocation = new ModelResourceLocation(item.getRegistryName(), tag);
        ModelLoader.setCustomModelResourceLocation(item, 0, resourceLocation);
    }

    private static void registerItemModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
    }
}
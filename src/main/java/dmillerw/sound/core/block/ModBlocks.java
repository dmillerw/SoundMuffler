package dmillerw.sound.core.block;

import dmillerw.sound.core.lib.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by dmillerw
 */
@GameRegistry.ObjectHolder(ModInfo.MOD_ID)
public class ModBlocks {

    public static final BlockSoundMuffler sound_muffler = null;
    @GameRegistry.ObjectHolder(ModInfo.MOD_ID + ":sound_muffler")
    public static final ItemBlock sound_muffler_item = null;

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    new BlockSoundMuffler().setRegistryName(ModInfo.MOD_ID, "sound_muffler")
            );
        }

        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    new ItemBlock(ModBlocks.sound_muffler).setRegistryName(ModInfo.MOD_ID, "sound_muffler")
            );
        }
    }
}

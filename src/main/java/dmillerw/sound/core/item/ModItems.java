package dmillerw.sound.core.item;

import dmillerw.sound.core.lib.ModInfo;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by dmillerw
 */
@GameRegistry.ObjectHolder(ModInfo.MOD_ID)
public class ModItems {

    public static final ItemEarplug earplug = null;
    public static final ItemMagicalEarplugs magical_earplugs = null;
    public static final ItemMysteriousEarplugs mysterious_earplugs = null;

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    new ItemEarplug().setRegistryName(ModInfo.MOD_ID, "earplug"),
                    new ItemMagicalEarplugs().setRegistryName(ModInfo.MOD_ID, "magical_earplugs"),
                    new ItemMysteriousEarplugs().setRegistryName(ModInfo.MOD_ID, "mysterious_earplugs")
            );
        }
    }
}

package dmillerw.sound.core;

import dmillerw.sound.SoundMuffler;
import dmillerw.sound.core.block.ModBlocks;
import dmillerw.sound.core.block.TileSoundMuffler;
import dmillerw.sound.core.handler.GuiHandler;
import dmillerw.sound.core.handler.PlayerHandler;
import dmillerw.sound.core.item.ModItems;
import dmillerw.sound.core.lib.ModInfo;
import dmillerw.sound.core.network.PacketHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @author dmillerw
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.initialize();

        NetworkRegistry.INSTANCE.registerGuiHandler(SoundMuffler.instance, new GuiHandler());

        MinecraftForge.EVENT_BUS.register(new PlayerHandler());

        GameRegistry.registerTileEntity(TileSoundMuffler.class, ModInfo.MOD_ID + ":sound_muffler");

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(ModBlocks.sound_muffler),
                "WWW",
                "WNW",
                "WRW",
                'W', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE),
                'N', Blocks.NOTEBLOCK,
                'R', Items.REDSTONE
        ));

        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.earplug),
                new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE),
                Items.REDSTONE
        );

        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.magical_earplugs),
                ModItems.earplug,
                ModItems.earplug,
                Items.REDSTONE
        );

    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}

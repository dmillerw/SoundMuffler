package dmillerw.sound.core;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dmillerw.sound.SoundMuffler;
import dmillerw.sound.core.handler.GuiHandler;
import dmillerw.sound.core.item.ItemMagicalEarmuffs;
import dmillerw.sound.core.network.PacketHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @author dmillerw
 */
public class CommonProxy {

    public static Item magicalEarmuffs;

    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.initialize();

        NetworkRegistry.INSTANCE.registerGuiHandler(SoundMuffler.instance, new GuiHandler());

        magicalEarmuffs = new ItemMagicalEarmuffs();
        GameRegistry.registerItem(magicalEarmuffs, "magicalEarmuffs");

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(magicalEarmuffs),
                "SSS",
                "W W",
                "N N",
                'S', "stickWood",
                'W', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                'N', Blocks.noteblock
        ));
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
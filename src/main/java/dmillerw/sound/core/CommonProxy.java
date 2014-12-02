package dmillerw.sound.core;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dmillerw.sound.SoundMuffler;
import dmillerw.sound.core.block.BlockSoundMuffler;
import dmillerw.sound.core.block.TileSoundMuffler;
import dmillerw.sound.core.handler.GuiHandler;
import dmillerw.sound.core.item.ItemMagicalEarmuffs;
import dmillerw.sound.core.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @author dmillerw
 */
public class CommonProxy {

    public static Block soundMuffler;

    public static Item magicalEarmuffs;

    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.initialize();

        NetworkRegistry.INSTANCE.registerGuiHandler(SoundMuffler.instance, new GuiHandler());

        magicalEarmuffs = new ItemMagicalEarmuffs();
        GameRegistry.registerItem(magicalEarmuffs, "magicalEarmuffs");

        soundMuffler = new BlockSoundMuffler();
        GameRegistry.registerBlock(soundMuffler, "soundMuffler");
        GameRegistry.registerTileEntity(TileSoundMuffler.class, "soundmuffler++:soundMuffler");

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(magicalEarmuffs),
                "SSS",
                "W W",
                "N N",
                'S', "stickWood",
                'W', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                'N', Blocks.noteblock
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(soundMuffler),
                "WWW",
                "WNW",
                "WRW",
                'W', Blocks.wool,
                'N', Blocks.noteblock,
                'R', Items.redstone
        ));
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}

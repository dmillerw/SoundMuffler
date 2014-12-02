package dmillerw.sound.core;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dmillerw.sound.SoundMuffler;
import dmillerw.sound.core.block.BlockSoundMuffler;
import dmillerw.sound.core.block.TileSoundMuffler;
import dmillerw.sound.core.handler.GuiHandler;
import dmillerw.sound.core.handler.PlayerHandler;
import dmillerw.sound.core.item.ItemEarplug;
import dmillerw.sound.core.item.ItemMagicalEarplugs;
import dmillerw.sound.core.item.ItemMysteriousEarplugs;
import dmillerw.sound.core.item.bauble.BaubleMagicalEarplugs;
import dmillerw.sound.core.item.bauble.BaubleMysteriousEarplugs;
import dmillerw.sound.core.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @author dmillerw
 */
public class CommonProxy {

    public static Block soundMuffler;

    public static Item earplug;
    public static Item magicalEarplugs;
    public static Item mysteriousEarplugs;

    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.initialize();

        NetworkRegistry.INSTANCE.registerGuiHandler(SoundMuffler.instance, new GuiHandler());

        MinecraftForge.EVENT_BUS.register(new PlayerHandler());

        soundMuffler = new BlockSoundMuffler();
        register(soundMuffler);
        GameRegistry.registerTileEntity(TileSoundMuffler.class, "soundmuffler++:soundMuffler");

        earplug = new ItemEarplug();
        register(earplug);

        magicalEarplugs = new ItemMagicalEarplugs();
        if (Loader.isModLoaded("Baubles")) {
            magicalEarplugs = new BaubleMagicalEarplugs();
        }
        register(magicalEarplugs);

        mysteriousEarplugs = new ItemMysteriousEarplugs();
        if (Loader.isModLoaded("Baubles")) {
            mysteriousEarplugs = new BaubleMysteriousEarplugs();
        }
        register(mysteriousEarplugs);

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(soundMuffler),
                "WWW",
                "WNW",
                "WRW",
                'W', Blocks.wool,
                'N', Blocks.noteblock,
                'R', Items.redstone
        ));

        GameRegistry.addShapelessRecipe(
                new ItemStack(earplug),
                new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                Items.redstone
        );

        GameRegistry.addShapelessRecipe(
                new ItemStack(mysteriousEarplugs),
                earplug,
                earplug,
                Items.redstone
        );

    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    private void register(Block block) {
        GameRegistry.registerBlock(block, block.getUnlocalizedName());
    }

    private void register(Item item) {
        GameRegistry.registerItem(item, item.getUnlocalizedName());
    }
}

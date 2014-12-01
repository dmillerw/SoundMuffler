package dmillerw.sound;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import dmillerw.sound.core.CommonProxy;

/**
 * @author dmillerw
 */
@Mod(modid = "SoundMuffer++", name = "SoundMuffler++", version = "%MOD_VERSION%")
public class SoundMuffler {

    @Mod.Instance("SoundMuffer++")
    public static SoundMuffler instance;

    @SidedProxy(serverSide = "dmillerw.sound.core.CommonProxy", clientSide = "dmillerw.sound.client.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}

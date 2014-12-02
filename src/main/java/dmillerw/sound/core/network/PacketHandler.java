package dmillerw.sound.core.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * @author dmillerw
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("SoundMuffler++");

    public static void initialize() {
        INSTANCE.registerMessage(PacketSoundMuffler.Tile.class, PacketSoundMuffler.Tile.class, 0, Side.SERVER);
        INSTANCE.registerMessage(PacketSoundMuffler.Item.class, PacketSoundMuffler.Item.class, 1, Side.SERVER);
    }
}

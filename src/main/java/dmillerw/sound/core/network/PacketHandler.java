package dmillerw.sound.core.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import dmillerw.sound.core.network.packet.PacketItemSoundMuffler;

/**
 * @author dmillerw
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("SoundMuffler++");

    public static void initialize() {
        INSTANCE.registerMessage(PacketItemSoundMuffler.class, PacketItemSoundMuffler.class, 0, Side.SERVER);
    }
}

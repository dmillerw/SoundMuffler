package dmillerw.sound.core.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import dmillerw.sound.core.network.packet.PacketSoundEntry;

/**
 * @author dmillerw
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("SoundMuffler++");

    public static void initialize() {
        INSTANCE.registerMessage(PacketSoundEntry.class, PacketSoundEntry.class, 0, Side.SERVER);
    }
}

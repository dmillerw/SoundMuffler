package dmillerw.sound.core.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import dmillerw.sound.core.network.packet.PacketAddSoundEntry;
import dmillerw.sound.core.network.packet.PacketRemoveSoundEntry;
import dmillerw.sound.core.network.packet.PacketSetListType;

/**
 * @author dmillerw
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("SoundMuffler++");

    public static void initialize() {
        INSTANCE.registerMessage(PacketAddSoundEntry.class, PacketAddSoundEntry.class, 0, Side.SERVER);
        INSTANCE.registerMessage(PacketRemoveSoundEntry.class, PacketRemoveSoundEntry.class, 1, Side.SERVER);
        INSTANCE.registerMessage(PacketSetListType.class, PacketSetListType.class, 2, Side.SERVER);
    }
}

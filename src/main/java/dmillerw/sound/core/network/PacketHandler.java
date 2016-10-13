package dmillerw.sound.core.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

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

package dmillerw.sound.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import dmillerw.sound.core.network.PacketHandler;

/**
 * @author dmillerw
 */
public abstract class CorePacket<T extends IMessage> implements IMessage, IMessageHandler<T, IMessage> {

    public void sendToServer() {
        PacketHandler.INSTANCE.sendToServer(this);
    }
}

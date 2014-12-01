package dmillerw.sound.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dmillerw.sound.api.IMagicalEarmuffs;
import dmillerw.sound.core.network.CorePacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

/**
 * @author dmillerw
 */
public class PacketRemoveSoundEntry extends CorePacket<PacketRemoveSoundEntry> {

    public String name;

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        try {
            packetBuffer.writeInt(name.length());
            packetBuffer.writeStringToBuffer(name);
        } catch (IOException ex) {}
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        int length = packetBuffer.readInt();
        try {
            name = packetBuffer.readStringFromBuffer(length);
        } catch (IOException ex) {}
    }

    @Override
    public IMessage onMessage(PacketRemoveSoundEntry message, MessageContext ctx) {
        ItemStack held = ctx.getServerHandler().playerEntity.getHeldItem();
        if (held != null && held.getItem() instanceof IMagicalEarmuffs)
            ((IMagicalEarmuffs) held.getItem()).removeSoundEntry(held, name);

        ctx.getServerHandler().playerEntity.updateHeldItem();

        return null;
    }
}

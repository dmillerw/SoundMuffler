package dmillerw.sound.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dmillerw.sound.api.IMagicalEarmuffs;
import dmillerw.sound.api.SoundEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class PacketSoundEntry extends CorePacket<PacketSoundEntry> {

    public static void addSoundEntry(SoundEntry soundEntry) {
        PacketSoundEntry packet = new PacketSoundEntry();
        packet.soundEntry = soundEntry;
        packet.type = Type.ADD;
        packet.sendToServer();
    }

    public static void removeSoundEntry(SoundEntry soundEntry) {
        PacketSoundEntry packet = new PacketSoundEntry();
        packet.soundEntry = soundEntry;
        packet.type = Type.REMOVE;
        packet.sendToServer();
    }

    public SoundEntry soundEntry;

    private Type type;

    @Override
    public void toBytes(ByteBuf buf) {
        soundEntry.toBytes(buf);
        buf.writeByte(type.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        soundEntry = SoundEntry.fromBytes(buf);
        type = Type.values()[buf.readByte()];
    }

    @Override
    public IMessage onMessage(PacketSoundEntry message, MessageContext ctx) {
        ItemStack held = ctx.getServerHandler().playerEntity.getHeldItem();
        if (held != null && held.getItem() instanceof IMagicalEarmuffs) {
            if (type == Type.ADD)
                ((IMagicalEarmuffs) held.getItem()).addSoundEntry(held, message.soundEntry);
            else if (type == Type.REMOVE)
                ((IMagicalEarmuffs) held.getItem()).removeSoundEntry(held, message.soundEntry);
        }

        ctx.getServerHandler().playerEntity.updateHeldItem();

        return null;
    }

    private static enum Type {
        ADD,
        REMOVE
    }
}

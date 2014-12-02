package dmillerw.sound.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.SoundEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class PacketItemSoundMuffler extends CorePacket<PacketItemSoundMuffler> {

    public static void addSoundEntry(SoundEntry soundEntry) {
        PacketItemSoundMuffler packet = new PacketItemSoundMuffler();
        packet.soundEntry = soundEntry;
        packet.type = Type.ADD;
        packet.sendToServer();
    }

    public static void removeSoundEntry(SoundEntry soundEntry) {
        PacketItemSoundMuffler packet = new PacketItemSoundMuffler();
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
    public IMessage onMessage(PacketItemSoundMuffler message, MessageContext ctx) {
        ItemStack held = ctx.getServerHandler().playerEntity.getHeldItem();
        if (held != null && held.getItem() instanceof IItemSoundMuffler) {
            if (type == Type.ADD)
                ((IItemSoundMuffler) held.getItem()).addSoundEntry(held, message.soundEntry);
            else if (type == Type.REMOVE)
                ((IItemSoundMuffler) held.getItem()).removeSoundEntry(held, message.soundEntry);
        }

        ctx.getServerHandler().playerEntity.updateHeldItem();

        return null;
    }

    private static enum Type {
        ADD,
        REMOVE
    }
}

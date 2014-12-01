package dmillerw.sound.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dmillerw.sound.api.IMagicalEarmuffs;
import dmillerw.sound.api.SoundEntry;
import dmillerw.sound.core.network.CorePacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

/**
 * @author dmillerw
 */
public class PacketAddSoundEntry extends CorePacket<PacketAddSoundEntry> {

    public SoundEntry soundEntry;

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        try {
            packetBuffer.writeInt(soundEntry.name.length());
            packetBuffer.writeStringToBuffer(soundEntry.name);
        } catch (IOException ex) {}
        packetBuffer.writeFloat(soundEntry.volumeModifier);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        int length = packetBuffer.readInt();
        String entry = "";
        try {
            entry = packetBuffer.readStringFromBuffer(length);
        } catch (IOException ex) {}
        float volumeModifier = packetBuffer.readFloat();
        soundEntry = new SoundEntry(entry, volumeModifier);
    }

    @Override
    public IMessage onMessage(PacketAddSoundEntry message, MessageContext ctx) {
        ItemStack held = ctx.getServerHandler().playerEntity.getHeldItem();
        if (held != null && held.getItem() instanceof IMagicalEarmuffs)
            ((IMagicalEarmuffs) held.getItem()).addSoundEntry(held, soundEntry);

        ctx.getServerHandler().playerEntity.updateHeldItem();

        return null;
    }
}

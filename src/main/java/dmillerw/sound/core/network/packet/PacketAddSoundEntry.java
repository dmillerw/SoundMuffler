package dmillerw.sound.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dmillerw.sound.api.IMagicalEarmuffs;
import dmillerw.sound.api.SoundEntry;
import dmillerw.sound.core.network.CorePacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class PacketAddSoundEntry extends CorePacket<PacketAddSoundEntry> {

    public SoundEntry soundEntry;

    @Override
    public void toBytes(ByteBuf buf) {
        soundEntry.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        soundEntry = SoundEntry.fromBytes(buf);
    }

    @Override
    public IMessage onMessage(PacketAddSoundEntry message, MessageContext ctx) {
        ItemStack held = ctx.getServerHandler().playerEntity.getHeldItem();
        if (held != null && held.getItem() instanceof IMagicalEarmuffs)
            ((IMagicalEarmuffs) held.getItem()).addSoundEntry(held, message.soundEntry);

        ctx.getServerHandler().playerEntity.updateHeldItem();

        return null;
    }
}

package dmillerw.sound.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dmillerw.sound.api.EnumListType;
import dmillerw.sound.api.IMagicalEarmuffs;
import dmillerw.sound.core.network.CorePacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class PacketSetListType extends CorePacket<PacketSetListType> {

    public EnumListType listType;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(listType.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        listType = EnumListType.values()[buf.readByte()];
    }

    @Override
    public IMessage onMessage(PacketSetListType message, MessageContext ctx) {
        ItemStack held = ctx.getServerHandler().playerEntity.getHeldItem();
        if (held != null && held.getItem() instanceof IMagicalEarmuffs)
            ((IMagicalEarmuffs) held.getItem()).setListType(held, listType);

        ctx.getServerHandler().playerEntity.updateHeldItem();

        return null;
    }
}

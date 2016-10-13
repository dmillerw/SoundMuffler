package dmillerw.sound.core.network;

import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.api.SoundEntry;
import dmillerw.sound.core.block.TileSoundMuffler;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author dmillerw
 */
public abstract class PacketSoundMuffler<T extends IMessage> implements IMessage, IMessageHandler<T, IMessage> {

    public static void sendPacket(SoundEntry soundEntry, Type type) {
        PacketSoundMuffler.Item packet = new PacketSoundMuffler.Item();
        packet.soundEntry = soundEntry;
        packet.type = type;
        packet.sendToServer();
    }

    public static void sendPacket(ITileSoundMuffler tileSoundMuffler, SoundEntry soundEntry, Type type) {
        PacketSoundMuffler.Tile packet = new PacketSoundMuffler.Tile();
        packet.blockPos = tileSoundMuffler.getPosition();
        packet.soundEntry = soundEntry;
        packet.type = type;
        packet.sendToServer();
    }

    public SoundEntry soundEntry;
    public Type type;

    @Override
    public void toBytes(ByteBuf buf) {
        soundEntry.toBytes(buf);
        buf.writeByte(type.ordinal());
        writeData(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        soundEntry = SoundEntry.fromBytes(buf);
        type = Type.values()[buf.readByte()];
        readData(buf);
    }

    @Override
    public IMessage onMessage(T message, MessageContext ctx) {
        handleMessage(message, ctx);
        return null;
    }

    public void sendToServer() {
        PacketHandler.INSTANCE.sendToServer(this);
    }

    public abstract void writeData(ByteBuf buf);
    public abstract void readData(ByteBuf buf);
    public abstract void handleMessage(T message, MessageContext ctx);

    public static class Tile extends PacketSoundMuffler<Tile> {

        public BlockPos blockPos;

        @Override
        public void writeData(ByteBuf buf) {
            buf.writeLong(blockPos.toLong());
        }

        @Override
        public void readData(ByteBuf buf) {
            blockPos = BlockPos.fromLong(buf.readLong());
        }

        @Override
        public void handleMessage(Tile message, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            ITileSoundMuffler tileSoundMuffler = (ITileSoundMuffler) world.getTileEntity(message.blockPos);

            if (tileSoundMuffler != null) {
                if (message.type == Type.ADD)
                    tileSoundMuffler.addSoundEntry(message.soundEntry);
                else if (message.type == Type.REMOVE)
                    tileSoundMuffler.removeSoundEntry(message.soundEntry);

                ((TileSoundMuffler)tileSoundMuffler).markDirtyAndNotify();
            }
        }
    }

    public static class Item extends PacketSoundMuffler<Item> {

        @Override
        public void writeData(ByteBuf buf) {

        }

        @Override
        public void readData(ByteBuf buf) {

        }

        @Override
        public void handleMessage(Item message, MessageContext ctx) {
            ItemStack held = ctx.getServerHandler().playerEntity.getActiveItemStack();
            if (held != null && held.getItem() instanceof IItemSoundMuffler) {
                if (message.type == Type.ADD)
                    ((IItemSoundMuffler) held.getItem()).addSoundEntry(held, message.soundEntry);
                else if (message.type == Type.REMOVE)
                    ((IItemSoundMuffler) held.getItem()).removeSoundEntry(held, message.soundEntry);

                ctx.getServerHandler().playerEntity.updateHeldItem();
            }
        }
    }

    public static enum Type {
        ADD,
        REMOVE
    }
}

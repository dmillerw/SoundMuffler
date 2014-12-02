package dmillerw.sound.core.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.server.FMLServerHandler;
import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.api.SoundEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
        packet.xCoord = tileSoundMuffler.getX();
        packet.yCoord = tileSoundMuffler.getY();
        packet.zCoord = tileSoundMuffler.getZ();
        packet.dimension = tileSoundMuffler.getDimension();
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

        public int xCoord;
        public int yCoord;
        public int zCoord;
        public int dimension;

        @Override
        public void writeData(ByteBuf buf) {
            buf.writeInt(xCoord);
            buf.writeInt(yCoord);
            buf.writeInt(zCoord);
            buf.writeInt(dimension);
        }

        @Override
        public void readData(ByteBuf buf) {
            xCoord = buf.readInt();
            yCoord = buf.readInt();
            zCoord = buf.readInt();
            dimension = buf.readInt();
        }

        @Override
        public void handleMessage(Tile message, MessageContext ctx) {
            World world = FMLServerHandler.instance().getServer().worldServerForDimension(message.dimension);
            ITileSoundMuffler tileSoundMuffler = (ITileSoundMuffler) world.getTileEntity(message.xCoord, message.yCoord, message.zCoord);

            if (tileSoundMuffler != null) {
                if (message.type == Type.ADD)
                    tileSoundMuffler.addSoundEntry(message.soundEntry);
                else if (message.type == Type.REMOVE)
                    tileSoundMuffler.removeSoundEntry(message.soundEntry);

                world.markBlockForUpdate(tileSoundMuffler.getX(), tileSoundMuffler.getY(), tileSoundMuffler.getZ());
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
            ItemStack held = ctx.getServerHandler().playerEntity.getHeldItem();
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

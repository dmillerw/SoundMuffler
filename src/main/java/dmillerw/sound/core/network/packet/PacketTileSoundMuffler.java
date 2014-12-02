package dmillerw.sound.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.server.FMLServerHandler;
import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.api.SoundEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class PacketTileSoundMuffler extends CorePacket<PacketTileSoundMuffler> {

    public static void addSoundEntry(ITileSoundMuffler tileSoundMuffler, SoundEntry soundEntry) {
        PacketTileSoundMuffler packet = new PacketTileSoundMuffler();
        packet.xCoord = tileSoundMuffler.getX();
        packet.yCoord = tileSoundMuffler.getY();
        packet.zCoord = tileSoundMuffler.getZ();
        packet.dimension = tileSoundMuffler.getDimension();
        packet.soundEntry = soundEntry;
        packet.type = Type.ADD;
        packet.sendToServer();
    }

    public static void removeSoundEntry(ITileSoundMuffler tileSoundMuffler, SoundEntry soundEntry) {
        PacketTileSoundMuffler packet = new PacketTileSoundMuffler();
        packet.xCoord = tileSoundMuffler.getX();
        packet.yCoord = tileSoundMuffler.getY();
        packet.zCoord = tileSoundMuffler.getZ();
        packet.dimension = tileSoundMuffler.getDimension();
        packet.soundEntry = soundEntry;
        packet.type = Type.REMOVE;
        packet.sendToServer();
    }

    public int xCoord;
    public int yCoord;
    public int zCoord;
    public int dimension;

    public SoundEntry soundEntry;

    private Type type;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(xCoord);
        buf.writeInt(yCoord);
        buf.writeInt(zCoord);
        buf.writeInt(dimension);
        soundEntry.toBytes(buf);
        buf.writeByte(type.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        xCoord = buf.readInt();
        yCoord = buf.readInt();
        zCoord = buf.readInt();
        dimension = buf.readInt();
        soundEntry = SoundEntry.fromBytes(buf);
        type = Type.values()[buf.readByte()];
    }

    @Override
    public IMessage onMessage(PacketTileSoundMuffler message, MessageContext ctx) {
        World world = FMLServerHandler.instance().getServer().worldServerForDimension(message.dimension);
        ITileSoundMuffler tileSoundMuffler = (ITileSoundMuffler) world.getTileEntity(message.xCoord, message.yCoord, message.zCoord);

        if (tileSoundMuffler != null) {
            if (type == Type.ADD)
                tileSoundMuffler.addSoundEntry(message.soundEntry);
            else if (type == Type.REMOVE)
                tileSoundMuffler.removeSoundEntry(message.soundEntry);

            world.markBlockForUpdate(tileSoundMuffler.getX(), tileSoundMuffler.getY(), tileSoundMuffler.getZ());
        }

        return null;
    }

    private static enum Type {
        ADD,
        REMOVE
    }
}

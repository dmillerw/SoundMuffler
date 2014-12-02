package dmillerw.sound.api;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

/**
 * @author dmillerw
 */
public class SoundEntry {

    public static SoundEntry fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        int length = packetBuffer.readInt();
        String entry = "";
        try {
            entry = packetBuffer.readStringFromBuffer(length);
        } catch (IOException ex) {}
        int volumeModifier = packetBuffer.readInt();
        return new SoundEntry(entry, volumeModifier);
    }

    public static SoundEntry readFromNBT(NBTTagCompound nbtTagCompound) {
        return new SoundEntry(nbtTagCompound.getString("name"), nbtTagCompound.getInteger("volumeModifier"));
    }

    public String name;

    public int volumeModifier;

    public SoundEntry(String name, int volumeModifier) {
        this.name = name;
        this.volumeModifier = volumeModifier;
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setString("name", name);
        nbtTagCompound.setFloat("volumeModifier", volumeModifier);
    }

    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        try {
            packetBuffer.writeInt(name.length());
            packetBuffer.writeStringToBuffer(name);
        } catch (IOException ex) {}
        packetBuffer.writeInt(volumeModifier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoundEntry that = (SoundEntry) o;

        if (Integer.compare(that.volumeModifier, volumeModifier) != 0) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }
}

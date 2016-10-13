package dmillerw.sound.api;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

/**
 * @author dmillerw
 */
public class SoundEntry {

    public static SoundEntry fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        int length = packetBuffer.readInt();
        String entry = packetBuffer.readStringFromBuffer(length);
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
        packetBuffer.writeInt(name.length());
        packetBuffer.writeString(name);
        packetBuffer.writeInt(volumeModifier);
    }

    public boolean nameMatches(String name) {
        String[] soundSplit = name.replace(".", "/").split("/");
        String[] soundEntrySplit = this.name.replace(".", "/").split("/");
        boolean[] matchingArray = new boolean[soundEntrySplit.length];

        for (int i=0; i<soundEntrySplit.length; i++) {
            if (i >= soundSplit.length)
                break;

            String one = soundSplit[i];
            String two = soundEntrySplit[i];

            if (one.equals(two) || (one.equals("*") || two.equals("*"))) {
                matchingArray[i] = true;
            }
        }

        for (boolean bool : matchingArray) {
            if (!bool) return false;
        }
        return true;
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

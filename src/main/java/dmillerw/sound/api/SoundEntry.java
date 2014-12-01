package dmillerw.sound.api;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author dmillerw
 */
public class SoundEntry {

    public static SoundEntry readFromNBT(NBTTagCompound nbtTagCompound) {
        return new SoundEntry(nbtTagCompound.getString("name"), nbtTagCompound.getFloat("volumeModifier"));
    }

    public String name;

    public float volumeModifier;

    public SoundEntry(String name, float volumeModifier) {
        this.name = name;
        this.volumeModifier = volumeModifier;
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setString("name", name);
        nbtTagCompound.setFloat("volumeModifier", volumeModifier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoundEntry that = (SoundEntry) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

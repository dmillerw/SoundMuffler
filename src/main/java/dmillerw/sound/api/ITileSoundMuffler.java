package dmillerw.sound.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.ISound;

import java.util.List;

/**
 * @author dmillerw
 */
public interface ITileSoundMuffler {

    public int getRange();

    public int getDimension();

    public int getX();

    public int getY();

    public int getZ();

    @SideOnly(Side.CLIENT)
    public ISound getMuffledSound(String name, ISound sound);

    public List<SoundEntry> getSoundEntries();

    public void addSoundEntry(SoundEntry soundEntry);

    public void removeSoundEntry(SoundEntry soundEntry);
}

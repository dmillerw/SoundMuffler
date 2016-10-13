package dmillerw.sound.api;

import net.minecraft.client.audio.ISound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author dmillerw
 */
public interface ITileSoundMuffler {

    public int getRange();

    public int getDimension();

    public BlockPos getPosition();

    @SideOnly(Side.CLIENT)
    public ISound getMuffledSound(String name, ISound sound, SoundCategory soundCategory);

    public List<SoundEntry> getSoundEntries();

    public void addSoundEntry(SoundEntry soundEntry);

    public void removeSoundEntry(SoundEntry soundEntry);
}

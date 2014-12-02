package dmillerw.sound.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.ISound;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author dmillerw
 */
public interface IItemSoundMuffler {

    @SideOnly(Side.CLIENT)
    public ISound getMuffledSound(ItemStack itemStack, String name, ISound sound);

    public List<SoundEntry> getSoundEntries(ItemStack itemStack);

    public void addSoundEntry(ItemStack itemStack, SoundEntry soundEntry);

    public void removeSoundEntry(ItemStack itemStack, SoundEntry soundEntry);
}

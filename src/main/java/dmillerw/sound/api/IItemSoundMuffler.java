package dmillerw.sound.api;

import net.minecraft.client.audio.ISound;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author dmillerw
 */
public interface IItemSoundMuffler {

    @SideOnly(Side.CLIENT)
    public ISound getMuffledSound(ItemStack itemStack, String name, ISound sound, SoundCategory soundCategory);

    public List<SoundEntry> getSoundEntries(ItemStack itemStack);

    public void addSoundEntry(ItemStack itemStack, SoundEntry soundEntry);

    public void removeSoundEntry(ItemStack itemStack, SoundEntry soundEntry);
}

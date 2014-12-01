package dmillerw.sound.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.ISound;
import net.minecraft.item.ItemStack;

import java.util.Set;

/**
 * @author dmillerw
 */
public interface IMagicalEarmuffs {

    @SideOnly(Side.CLIENT)
    public ISound getMuffledSound(ItemStack itemStack, String name, ISound sound);

    public EnumListType getListType(ItemStack itemStack);

    public Set<SoundEntry> getSoundEntries(ItemStack itemStack);

    public void setListType(ItemStack itemStack, EnumListType listType);

    public void addSoundEntry(ItemStack itemStack, SoundEntry soundEntry);

    public void removeSoundEntry(ItemStack itemStack, String name);
}

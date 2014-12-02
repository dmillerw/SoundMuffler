package dmillerw.sound.core.item;

import dmillerw.sound.client.sound.SoundHelper;
import dmillerw.sound.client.sound.SoundReplaced;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class ItemBrokenEarmuffs extends ItemMagicalEarmuffs {

    public static int cooldown = 0;

    public ItemBrokenEarmuffs() {
        super();

        setUnlocalizedName("brokenEarmuffs");
    }

    @Override
    public boolean hasEffect(ItemStack itemStack, int pass) {
        return true;
    }

    @Override
    public ISound getMuffledSound(ItemStack itemStack, String name, ISound sound, SoundCategory soundCategory) {
        // Fire a random sound every other sound
        if (cooldown == 0) {
            cooldown = 1;
            return SoundHelper.getRandomSound(sound, soundCategory);
        } else {
            cooldown--;
            return new SoundReplaced(sound, sound.getPositionedSoundLocation());
        }
    }
}

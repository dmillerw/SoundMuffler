package dmillerw.sound.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dmillerw.sound.client.sound.SoundHelper;
import dmillerw.sound.client.sound.SoundReplaced;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * @author dmillerw
 */
public class ItemMysteriousEarplugs extends ItemMagicalEarplugs {

    public static int cooldown = 0;

    public ItemMysteriousEarplugs() {
        super();

        setUnlocalizedName("mysteriousEarplugs");
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean debug) {
        list.add(StatCollector.translateToLocal("item.mysteriousEarplugs.tooltip"));
    }

    @Override
    public boolean hasEffect(ItemStack itemStack, int pass) {
        return true;
    }

    @SideOnly(Side.CLIENT)
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

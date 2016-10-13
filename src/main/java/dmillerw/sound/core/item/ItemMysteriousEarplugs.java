package dmillerw.sound.core.item;

import dmillerw.sound.client.sound.SoundHelper;
import dmillerw.sound.client.sound.SoundReplaced;
import dmillerw.sound.core.lib.ModInfo;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author dmillerw
 */
public class ItemMysteriousEarplugs extends ItemMagicalEarplugs {

    public static int cooldown = 0;

    public ItemMysteriousEarplugs() {
        super();

        setUnlocalizedName(ModInfo.MOD_ID + "mysterious_earplugs");
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean debug) {
        list.add(I18n.translateToLocal("item.soundmuffler:mysterious_earplugs.tooltip"));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
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
            return new SoundReplaced(sound, sound.getSoundLocation());
        }
    }
}

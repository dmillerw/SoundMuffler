package dmillerw.sound.core.item;

import dmillerw.sound.client.sound.SoundHandler;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class ItemBrokenEarmuffs extends ItemMagicalEarmuffs {

    public ItemBrokenEarmuffs() {
        super();

        setUnlocalizedName("brokenEarmuffs");
    }

    @Override
    public boolean hasEffect(ItemStack itemStack, int pass) {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        return itemStack;
    }

    @Override
    public ISound getMuffledSound(ItemStack itemStack, String name, ISound sound, SoundCategory soundCategory) {
        return SoundHandler.getRandomSound(sound, soundCategory);
    }
}

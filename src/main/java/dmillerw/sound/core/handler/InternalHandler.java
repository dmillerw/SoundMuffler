package dmillerw.sound.core.handler;

import dmillerw.sound.SoundMuffler;
import dmillerw.sound.api.IMagicalEarmuffs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class InternalHandler {

    public static void openConfigurationGUI(EntityPlayer entityPlayer) {
        ItemStack held = entityPlayer.getHeldItem();
        if (held == null)
            return;

        if (!(held.getItem() instanceof IMagicalEarmuffs))
            return;

        entityPlayer.openGui(SoundMuffler.instance, 0, entityPlayer.worldObj, 0, 0 ,0);
    }
}

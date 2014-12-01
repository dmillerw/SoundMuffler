package dmillerw.sound.client.sound;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dmillerw.sound.api.IMagicalEarmuffs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;

/**
 * @author dmillerw
 */
@SideOnly(Side.CLIENT)
public class SoundHandler {

    @SideOnly(Side.CLIENT)
    public static ISound getMuffledSound(String name, ISound sound, ItemStack itemStack) {
        if (itemStack == null)
            return sound;

        if (itemStack.getItem() == null)
            return sound;

        if (!(itemStack.getItem() instanceof IMagicalEarmuffs))
            return sound;

        return ((IMagicalEarmuffs) itemStack.getItem()).getMuffledSound(itemStack, name, sound);
    }

    @SubscribeEvent
    public void soundPlay(PlaySoundEvent17 event) {
        ItemStack itemStack = Minecraft.getMinecraft().thePlayer.getCurrentArmor(0);
        if (itemStack == null)
            return;

        event.result = getMuffledSound(event.name, event.sound, itemStack);
    }
}

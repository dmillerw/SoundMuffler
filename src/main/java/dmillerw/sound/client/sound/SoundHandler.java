package dmillerw.sound.client.sound;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.core.handler.InternalHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;

/**
 * @author dmillerw
 */
@SideOnly(Side.CLIENT)
public class SoundHandler {

    @SubscribeEvent
    public void soundPlay(PlaySoundEvent17 event) {
        if (Minecraft.getMinecraft().thePlayer == null)
            return;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        ISound sound = null;

        // Worn items take priority, and any sound change done via an armor piece
        // will end the search/muffling action, instantly returning the modified sound
        // EXCEPT if a block muffles that sound further
        for (ItemStack itemStack : player.inventory.armorInventory) {
            if (itemStack != null && itemStack.getItem() instanceof IItemSoundMuffler) {
                ISound muffled = SoundHelper.getMuffledSound(event.name, event.sound, event.category, itemStack);
                if (sound == null && muffled != null && muffled != event.sound) {
                    sound = muffled;
                }
            }
        }

        // Blocks fire next, and will handle anything the armor didn't
        for (ITileSoundMuffler soundMuffler : InternalHandler.soundMufflerList) {
            ISound muffled = SoundHelper.getMuffledSound(event.name, event.sound, event.category, soundMuffler);

            // Use the tiles sound only if its quieter, or an armor piece hasn' already changed it
            if (muffled != null) {
                if (sound == null && muffled != event.sound) {
                    sound = muffled;
                    break;
                } else if (muffled.getPositionedSoundLocation() == sound.getPositionedSoundLocation()) {
                    if (muffled.getVolume() < sound.getVolume())
                        sound = muffled;
                }
            }
        }

        if (sound != null)
            event.result = sound;
    }
}

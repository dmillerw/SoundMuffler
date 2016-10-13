package dmillerw.sound.client.sound;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.core.handler.InternalHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * @author dmillerw
 */
@SideOnly(Side.CLIENT)
public class SoundHandler {

    private static final int MAX = 15;

    public static List<String> soundHistory = Lists.newArrayList();

    @SubscribeEvent
    public void soundPlay(PlaySoundEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null)
            return;

        if (soundHistory != null) {
            if (soundHistory.size() > MAX)
                soundHistory.remove(Iterables.get(soundHistory, 0));

            if (!soundHistory.contains(event.getName()))
                soundHistory.add(event.getName());
        }

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        ISound sound = null;

        // Worn items take priority, and any sound change done via an armor piece
        // will end the search/muffling action, instantly returning the modified sound
        // EXCEPT if a block muffles that sound further
        for (ItemStack itemStack : player.inventory.armorInventory) {
            if (itemStack != null && itemStack.getItem() instanceof IItemSoundMuffler) {
                ISound muffled = SoundHelper.getMuffledSound(event.getName(), event.getSound(), event.getSound().getCategory(), itemStack);
                if (sound == null && muffled != null) {
                    sound = muffled;
                }
            }
        }

        // Blocks fire next, and will handle anything the armor didn't
        for (ITileSoundMuffler soundMuffler : InternalHandler.soundMufflerList) {
            ISound muffled = SoundHelper.getMuffledSound(event.getName(), event.getSound(), event.getSound().getCategory(), soundMuffler);

            // Use the tiles sound only if its quieter, or an armor piece hasn't already changed it
            if (muffled != null) {
                if (sound != null) {
                    if (sound.getSoundLocation().equals(muffled.getSoundLocation())) {
                        if (muffled.getVolume() < sound.getVolume())
                            sound = muffled;
                    }
                } else {
                    sound = muffled;
                }
            }
        }

        if (sound != null && !(sound instanceof SoundReplaced)) {
            player.worldObj.spawnParticle(EnumParticleTypes.NOTE, (double) sound.getXPosF() + 0.5D, (double) sound.getYPosF() + 1.2D, (double) sound.getZPosF() + 0.5D, (double) (new Random().nextInt(12)) / 24.0D, 0.0D, 0.0D);
            event.setResultSound(sound);
        }
    }
}

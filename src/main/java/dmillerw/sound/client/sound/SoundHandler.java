package dmillerw.sound.client.sound;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.api.SoundEntry;
import dmillerw.sound.core.handler.InternalHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;

/**
 * @author dmillerw
 */
@SideOnly(Side.CLIENT)
public class SoundHandler {

    @SideOnly(Side.CLIENT)
    public static ISound getMuffledSound(String name, ISound sound, SoundCategory soundCategory,  ItemStack itemStack) {
        if (itemStack == null)
            return sound;

        if (itemStack.getItem() == null)
            return sound;

        if (!(itemStack.getItem() instanceof IItemSoundMuffler))
            return sound;

        return ((IItemSoundMuffler) itemStack.getItem()).getMuffledSound(itemStack, name, sound, soundCategory);
    }

    @SideOnly(Side.CLIENT)
    public static ISound getMuffledSound(String name, ISound sound, SoundCategory soundCategory, ITileSoundMuffler tileSoundMuffler) {
        if (tileSoundMuffler == null)
            return sound;

        if (Minecraft.getMinecraft().theWorld.provider.dimensionId != tileSoundMuffler.getDimension())
            return sound;

        double dx = sound.getXPosF() - tileSoundMuffler.getX();
        double dy = sound.getYPosF() - tileSoundMuffler.getY();
        double dz = sound.getZPosF() - tileSoundMuffler.getZ();
        double distance = dx * dx + dy * dy + dz * dz;

        if (distance > tileSoundMuffler.getRange())
            return sound;

        return tileSoundMuffler.getMuffledSound(name, sound, soundCategory);
    }

    public static boolean soundMatches(String sound, SoundEntry soundEntry) {
        sound = sound.replace(".", "/");
        String entry = soundEntry.name.replace(".", "/");
        String[] soundSplit = sound.split("/");
        String[] soundEntrySplit = entry.split("/");
        if (soundSplit.length != soundEntrySplit.length)
            return false;

        for (int i=0; i<soundSplit.length; i++) {
            String one = soundSplit[i];
            String two = soundEntrySplit[i];

            if (one.equals(two) || (one.equals("*") || two.equals("*")))
                return true;
        }

        return false;
    }

    public static ISound getRandomSound(ISound oldSound, SoundCategory soundCategory) {
        return new SoundReplaced(oldSound, Minecraft.getMinecraft().getSoundHandler().getRandomSoundFromCategories(soundCategory).getSoundEventLocation());
    }

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
                ISound muffled = getMuffledSound(event.name, event.sound, event.category, itemStack);
                if (sound == null && muffled != null && muffled != event.sound) {
                    sound = muffled;
                }
            }
        }

        // Blocks fire next, and will handle anything the armor didn't
        for (ITileSoundMuffler soundMuffler : InternalHandler.soundMufflerList) {
            ISound muffled = getMuffledSound(event.name, event.sound, event.category, soundMuffler);

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

        event.result = sound;
    }
}

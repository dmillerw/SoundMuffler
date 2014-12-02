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

        if (!(itemStack.getItem() instanceof IItemSoundMuffler))
            return sound;

        return ((IItemSoundMuffler) itemStack.getItem()).getMuffledSound(itemStack, name, sound);
    }

    @SideOnly(Side.CLIENT)
    public static ISound getMuffledSound(String name, ISound sound, ITileSoundMuffler tileSoundMuffler) {
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

        return tileSoundMuffler.getMuffledSound(name, sound);
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

    @SubscribeEvent
    public void soundPlay(PlaySoundEvent17 event) {
        if (Minecraft.getMinecraft().thePlayer == null)
            return;

        ItemStack itemStack = Minecraft.getMinecraft().thePlayer.getCurrentArmor(3);

        // First, check to see if they have a sound muffling item
        if (itemStack != null && itemStack.getItem() instanceof IItemSoundMuffler)
            event.result = getMuffledSound(event.name, event.sound, itemStack);

        // Then, go through all registered sound mufflers
        for (ITileSoundMuffler soundMuffler : InternalHandler.soundMufflerList) {
            ISound muffled = getMuffledSound(event.name, event.sound, soundMuffler);

            // Use the tiles sound only if its quieter
            if (muffled instanceof SoundMuffled && muffled.getVolume() < event.result.getVolume())
                event.result = muffled;
        }
    }
}

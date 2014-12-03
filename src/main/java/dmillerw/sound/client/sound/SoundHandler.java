package dmillerw.sound.client.sound;

import baubles.api.BaublesApi;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.core.handler.InternalHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;

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
    public void soundPlay(PlaySoundEvent17 event) {
        if (Minecraft.getMinecraft().thePlayer == null)
            return;

        if (soundHistory != null) {
            if (soundHistory.size() > MAX)
                soundHistory.remove(Iterables.get(soundHistory, 0));

            if (!soundHistory.contains(event.name))
                soundHistory.add(event.name);
        }

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        ISound sound = null;
        ITileSoundMuffler tileSoundMuffler = null;

        // Worn items take priority, and any sound change done via an armor piece
        // will end the search/muffling action, instantly returning the modified sound
        // EXCEPT if a block muffles that sound further
        for (ItemStack itemStack : player.inventory.armorInventory) {
            if (itemStack != null && itemStack.getItem() instanceof IItemSoundMuffler) {
                ISound muffled = SoundHelper.getMuffledSound(event.name, event.sound, event.category, itemStack);
                if (sound == null && muffled != null) {
                    sound = muffled;
                }
            }
        }

        if (Loader.isModLoaded("Baubles")) {
            // Baubles fire next
            IInventory baublesInventory = BaublesApi.getBaubles(player);
            for (int i = 0; i < baublesInventory.getSizeInventory(); i++) {
                ItemStack itemStack = baublesInventory.getStackInSlot(i);
                if (itemStack != null && itemStack.getItem() instanceof IItemSoundMuffler) {
                    ISound muffled = SoundHelper.getMuffledSound(event.name, event.sound, event.category, itemStack);
                    if (sound == null && muffled != null) {
                        sound = muffled;
                    }
                }
            }
        }

        // Blocks fire next, and will handle anything the armor didn't
        for (ITileSoundMuffler soundMuffler : InternalHandler.soundMufflerList) {
            ISound muffled = SoundHelper.getMuffledSound(event.name, event.sound, event.category, soundMuffler);

            // Use the tiles sound only if its quieter, or an armor piece hasn't already changed it
            if (muffled != null) {
                if (sound != null) {
                    if (sound.getPositionedSoundLocation().equals(muffled.getPositionedSoundLocation())) {
                        if (muffled.getVolume() < sound.getVolume())
                            tileSoundMuffler = soundMuffler;
                            sound = muffled;
                    }
                } else {
                    tileSoundMuffler = soundMuffler;
                    sound = muffled;
                }
            }
        }

        if (sound != null) {
            if (tileSoundMuffler != null) {
                double dx = (tileSoundMuffler.getX() + 0.5) - sound.getXPosF();
                double dy = (tileSoundMuffler.getY() + 0.5) - sound.getYPosF();
                double dz = (tileSoundMuffler.getZ() + 0.5) - sound.getZPosF();
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (distance != 0.0D) {
                    distance = 1.0D / distance / 8.0D;
                }

                player.worldObj.spawnParticle("note", (double) sound.getXPosF() + 0.5D, (double) sound.getYPosF() + 1.2D, (double) sound.getZPosF() + 0.5D, dx * distance, dy * distance, dz * distance);
            } else {
                player.worldObj.spawnParticle("note", (double) sound.getXPosF() + 0.5D, (double) sound.getYPosF() + 1.2D, (double) sound.getZPosF() + 0.5D, (double) (new Random().nextInt(12)) / 24.0D, 0.0D, 0.0D);
            }

            event.result = sound;
        }
    }
}

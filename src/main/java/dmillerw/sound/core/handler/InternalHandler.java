package dmillerw.sound.core.handler;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dmillerw.sound.SoundMuffler;
import dmillerw.sound.api.EventSoundMufflerTile;
import dmillerw.sound.api.ITileSoundMuffler;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * @author dmillerw
 */
public class InternalHandler {

    public static List<ITileSoundMuffler> soundMufflerList = Lists.newArrayList();

    public static void openConfigurationGUI(EntityPlayer entityPlayer, int x, int y, int z) {
        entityPlayer.openGui(SoundMuffler.instance, 0, entityPlayer.worldObj, x, y, z);
    }

    @SubscribeEvent
    public void registerTile(EventSoundMufflerTile.Register event) {
        soundMufflerList.add(event.tileSoundMuffler);
    }

    @SubscribeEvent
    public void unregisterTile(EventSoundMufflerTile.Unregister event) {
        soundMufflerList.remove(event.tileSoundMuffler);
    }
}

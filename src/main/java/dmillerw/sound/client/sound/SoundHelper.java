package dmillerw.sound.client.sound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.ITileSoundMuffler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
@SideOnly(Side.CLIENT)
public class SoundHelper {

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

    public static ISound getRandomSound(ISound oldSound, SoundCategory soundCategory) {
        return new SoundReplaced(oldSound, Minecraft.getMinecraft().getSoundHandler().getRandomSoundFromCategories(soundCategory).getSoundEventLocation());
    }
}

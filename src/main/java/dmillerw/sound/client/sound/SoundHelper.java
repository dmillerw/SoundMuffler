package dmillerw.sound.client.sound;

import com.google.common.collect.Lists;
import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.ITileSoundMuffler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author dmillerw
 */
public class SoundHelper {

    private static SoundRegistry soundRegistry;

    private static Method getKeysMethod;

    private static SoundRegistry getSoundRegistry() {
        if (soundRegistry == null) {
            try {
                Class clazz = Class.forName("net.minecraft.client.audio.SoundHandler");
                Field field = clazz.getDeclaredField("soundRegistry");
                field.setAccessible(true);
                SoundHelper.soundRegistry = (SoundRegistry) field.get(Minecraft.getMinecraft().getSoundHandler());
                field.setAccessible(false);
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        return soundRegistry;
    }

    public static Set<ResourceLocation> getSoundKeys() {
        return ForgeRegistries.SOUND_EVENTS.getKeys();
    }

    public static List<ResourceLocation> getSoundsForCategory(SoundCategory... categories) {
        List<ResourceLocation> list = Lists.newArrayList();
        Set<ResourceLocation> set = getSoundKeys();

        if (set != null) {
            Iterator<ResourceLocation> iterator = set.iterator();
            while (iterator.hasNext()) {
                ResourceLocation resourceLocation = iterator.next();
                SoundEventAccessor composite = (SoundEventAccessor) getSoundRegistry().getObject(resourceLocation);
//                if (ArrayUtils.contains(categories, composite.getCategory())) {
//                    set.add(resourceLocation);
//                }
            }
        }

        return list;
    }

    public static ISound getMuffledSound(String name, ISound sound, SoundCategory soundCategory,  ItemStack itemStack) {
        if (itemStack == null)
            return null;

        if (itemStack.getItem() == null)
            return null;

        if (!(itemStack.getItem() instanceof IItemSoundMuffler))
            return null;

        return ((IItemSoundMuffler) itemStack.getItem()).getMuffledSound(itemStack, name, sound, soundCategory);
    }

    public static ISound getMuffledSound(String name, ISound sound, SoundCategory soundCategory, ITileSoundMuffler tileSoundMuffler) {
        if (tileSoundMuffler == null)
            return null;

        if (Minecraft.getMinecraft().theWorld.provider.getDimension() != tileSoundMuffler.getDimension())
            return null;

        double dx = (tileSoundMuffler.getPosition().getX() + 0.5) - sound.getXPosF();
        double dy = (tileSoundMuffler.getPosition().getY() + 0.5) - sound.getYPosF();
        double dz = (tileSoundMuffler.getPosition().getZ() + 0.5) - sound.getZPosF();
        double distance = dx * dx + dy * dy + dz * dz;

        if (distance > tileSoundMuffler.getRange() || distance < 0.0D)
            return null;

        return tileSoundMuffler.getMuffledSound(name, sound, soundCategory);
    }

    public static ISound getRandomSound(ISound oldSound, SoundCategory soundCategory) {
        SoundEventAccessor random = getSoundRegistry().getRandomObject(new Random());
        if (random != null)
            return new SoundReplaced(oldSound, random.getLocation());
        else
            return oldSound;
    }
}

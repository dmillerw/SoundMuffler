package dmillerw.sound.client.sound;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.ITileSoundMuffler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistrySimple;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author dmillerw
 */
@SideOnly(Side.CLIENT)
public class SoundHelper {

    private static SoundRegistry sndRegistry;

    private static Method getKeysMethod;

    private static SoundRegistry getSoundRegistry() {
        if (sndRegistry == null) {
            try {
                Class clazz = Class.forName("net.minecraft.client.audio.SoundHandler");
                Field field = clazz.getDeclaredField("sndRegistry");
                field.setAccessible(true);
                SoundHelper.sndRegistry = (SoundRegistry) field.get(Minecraft.getMinecraft().getSoundHandler());
                field.setAccessible(false);
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        return sndRegistry;
    }

    private static Set<ResourceLocation> getSoundKeys() {
        try {
            if (getKeysMethod == null) {
                try {
                    Class clazz = RegistrySimple.class;
                    getKeysMethod = clazz.getMethod("getKeys");
                } catch (Exception ex) { ex.printStackTrace(); };
            }
            return (Set<ResourceLocation>) getKeysMethod.invoke(getSoundRegistry());
        } catch (Exception ex) { ex.printStackTrace(); }
        return null;
    }

    public static List<ResourceLocation> getSoundsForCategory(SoundCategory ... categories) {
        List<ResourceLocation> set = Lists.newArrayList();
        Iterator<ResourceLocation> iterator = getSoundKeys().iterator();
        while (iterator.hasNext()) {
            ResourceLocation resourceLocation = iterator.next();
            SoundEventAccessorComposite composite = (SoundEventAccessorComposite) getSoundRegistry().getObject(resourceLocation);
            if (ArrayUtils.contains(categories, composite.getSoundCategory())) {
                set.add(resourceLocation);
            }
        }
        return set;
    }

    public static ISound getMuffledSound(String name, ISound sound, SoundCategory soundCategory,  ItemStack itemStack) {
        if (itemStack == null)
            return sound;

        if (itemStack.getItem() == null)
            return sound;

        if (!(itemStack.getItem() instanceof IItemSoundMuffler))
            return sound;

        return ((IItemSoundMuffler) itemStack.getItem()).getMuffledSound(itemStack, name, sound, soundCategory);
    }

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

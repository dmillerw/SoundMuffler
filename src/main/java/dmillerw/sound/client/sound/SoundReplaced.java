package dmillerw.sound.client.sound;

import net.minecraft.client.audio.*;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import javax.annotation.Nullable;

/**
 * @author dmillerw
 */
public class SoundReplaced implements ISound {

    private ISound sound;
    private ResourceLocation resourceLocation;

    public SoundReplaced(ISound sound, ResourceLocation resourceLocation) {
        this.sound = sound;
        this.resourceLocation = resourceLocation;
    }


    @Override
    public ResourceLocation getSoundLocation() {
        return resourceLocation;
    }

    @Nullable
    @Override
    public SoundEventAccessor createAccessor(SoundHandler handler) {
        return handler.getAccessor(resourceLocation);
    }

    @Override
    public Sound getSound() {
        return sound.getSound();
    }

    @Override
    public SoundCategory getCategory() {
        return sound.getCategory();
    }

    @Override
    public boolean canRepeat() {
        return sound.canRepeat();
    }

    @Override
    public int getRepeatDelay() {
        return sound.getRepeatDelay();
    }

    @Override
    public float getVolume() {
        return sound.getVolume();
    }

    @Override
    public float getPitch() {
        return sound.getPitch();
    }

    @Override
    public float getXPosF() {
        return sound.getXPosF();
    }

    @Override
    public float getYPosF() {
        return sound.getYPosF();
    }

    @Override
    public float getZPosF() {
        return sound.getZPosF();
    }

    @Override
    public AttenuationType getAttenuationType() {
        return sound.getAttenuationType();
    }
}

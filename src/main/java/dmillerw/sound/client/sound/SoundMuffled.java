package dmillerw.sound.client.sound;

import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;

/**
 * @author dmillerw
 */
public class SoundMuffled implements ISound {

    private ISound sound;

    private float volumeModifier;

    public SoundMuffled(ISound sound, float volumeModifier) {
        this.sound = sound;
        this.volumeModifier = volumeModifier;
    }

    @Override
    public ResourceLocation getPositionedSoundLocation() {
        return sound.getPositionedSoundLocation();
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
        return sound.getVolume() * volumeModifier;
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

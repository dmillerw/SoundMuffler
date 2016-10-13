package dmillerw.sound.api;

import net.minecraftforge.fml.common.eventhandler.GenericEvent;

/**
 * @author dmillerw
 */
public class EventSoundMufflerTile extends GenericEvent<ITileSoundMuffler> {

    public ITileSoundMuffler tileSoundMuffler;

    public EventSoundMufflerTile(ITileSoundMuffler tileSoundMuffler) {
        super(ITileSoundMuffler.class);
        this.tileSoundMuffler = tileSoundMuffler;
    }

    public static class Register extends EventSoundMufflerTile {
        public Register(ITileSoundMuffler tileSoundMuffler) {
            super(tileSoundMuffler);
        }
    }

    public static class Unregister extends EventSoundMufflerTile {
        public Unregister(ITileSoundMuffler tileSoundMuffler) {
            super(tileSoundMuffler);
        }
    }
}

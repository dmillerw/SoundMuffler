package dmillerw.sound.api;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * @author dmillerw
 */
public class EventSoundMufflerTile extends Event {

    public ITileSoundMuffler tileSoundMuffler;

    public EventSoundMufflerTile(ITileSoundMuffler tileSoundMuffler) {
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

package dmillerw.sound.client.gui;

/**
 * @author dmillerw
 */
public class SoundEntryString {

    public final String string;
    public final String resourceDomain;
    public final String fullPath;

    public final boolean end;

    public SoundEntryString(String string, String resourceDomain, String fullPath, boolean end) {
        this.string = string;
        this.resourceDomain = resourceDomain;
        this.fullPath = fullPath;
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoundEntryString that = (SoundEntryString) o;

        if (!string.equals(that.string)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }
}

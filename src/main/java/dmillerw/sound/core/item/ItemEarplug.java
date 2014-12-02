package dmillerw.sound.core.item;

import dmillerw.sound.core.TabSoundMuffler;
import net.minecraft.item.Item;

/**
 * @author dmillerw
 */
public class ItemEarplug extends Item {

    public ItemEarplug() {
        super();

        setCreativeTab(TabSoundMuffler.TAB);
        setMaxStackSize(1);
        setMaxDamage(0);
        setUnlocalizedName("earplug");
        setTextureName("soundmuffler++:earplug");
    }
}

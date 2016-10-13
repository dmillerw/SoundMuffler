package dmillerw.sound.core.item;

import dmillerw.sound.core.TabSoundMuffler;
import dmillerw.sound.core.lib.ModInfo;
import net.minecraft.item.Item;

/**
 * @author dmillerw
 */
public class ItemEarplug extends Item {

    public ItemEarplug() {
        super();

        setMaxDamage(0);
        setMaxStackSize(1);
        setCreativeTab(TabSoundMuffler.TAB);

        setUnlocalizedName(ModInfo.MOD_ID + ":earplug");
    }
}

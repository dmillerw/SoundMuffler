package dmillerw.sound.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author dmillerw
 */
public class TabSoundMuffler {

    public static final CreativeTabs TAB = new CreativeTabs("SoundMuffler++") {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(CommonProxy.soundMuffler);
        }
    };
}

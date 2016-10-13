package dmillerw.sound.core;

import dmillerw.sound.core.block.ModBlocks;
import dmillerw.sound.core.lib.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author dmillerw
 */
public class TabSoundMuffler {

    public static final CreativeTabs TAB = new CreativeTabs(ModInfo.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ModBlocks.sound_muffler);
        }
    };
}

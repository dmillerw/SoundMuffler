package dmillerw.sound.core.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.client.gui.GuiTileSoundMuffler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class GuiHandler implements IGuiHandler {

    public static final int GUI_CONFIGURE = 0;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == GUI_CONFIGURE) {
//            if (y <= 0)
//                return new GuiItemSoundMuffler(player);
//            else
                return new GuiTileSoundMuffler(player, (ITileSoundMuffler) world.getTileEntity(x, y, z));

        }

        return null;
    }
}

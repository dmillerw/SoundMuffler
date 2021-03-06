package dmillerw.sound.core.handler;

import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.client.gui.GuiSoundHistory;
import dmillerw.sound.client.gui.GuiSoundMuffler;
import dmillerw.sound.client.gui.GuiSoundSearch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author dmillerw
 */
public class GuiHandler implements IGuiHandler {

    public static final int GUI_CONFIGURE = 0;
    public static final int GUI_SEARCH = 1;
    public static final int GUI_HISTORY = 2;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == GUI_CONFIGURE) {
            GuiSoundMuffler.cacheLastCoordinates(x, y, z);

            if (y <= 0)
                return new GuiSoundMuffler.Item(player.getHeldItemMainhand());
            else
                return new GuiSoundMuffler.Tile((ITileSoundMuffler) world.getTileEntity(new BlockPos(x, y, z)));

        } else if (id == GUI_SEARCH) {
            return new GuiSoundSearch();
        } else if (id == GUI_HISTORY) {
            return new GuiSoundHistory();
        }

        return null;
    }
}

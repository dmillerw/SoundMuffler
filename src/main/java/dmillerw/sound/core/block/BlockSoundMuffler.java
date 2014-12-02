package dmillerw.sound.core.block;

import dmillerw.sound.core.handler.InternalHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class BlockSoundMuffler extends BlockContainer {

    public BlockSoundMuffler() {
        super(Material.cloth);

        setHardness(1F);
        setResistance(0F);
        setCreativeTab(CreativeTabs.tabDecorations);
        setBlockName("soundMuffler");
        setBlockTextureName("soundmuffler++:soundMuffler");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float fx, float fy, float fz) {
        InternalHandler.openConfigurationGUI(entityPlayer, x, y, z);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileSoundMuffler();
    }
}

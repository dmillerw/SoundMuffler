package dmillerw.sound.core.block;

import dmillerw.sound.core.TabSoundMuffler;
import dmillerw.sound.core.handler.InternalHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author dmillerw
 */
public class BlockSoundMuffler extends BlockContainer {

    public BlockSoundMuffler() {
        super(Material.CLOTH);

        setHardness(1F);
        setResistance(0F);
        setCreativeTab(TabSoundMuffler.TAB);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        InternalHandler.openConfigurationGUI(playerIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileSoundMuffler();
    }
}

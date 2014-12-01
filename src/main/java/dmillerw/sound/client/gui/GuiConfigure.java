package dmillerw.sound.client.gui;

import dmillerw.sound.api.IMagicalEarmuffs;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class GuiConfigure extends GuiScreen {

    private EntityPlayer entityPlayer;

    private ItemStack itemStack;
    private IMagicalEarmuffs magicalEarmuffs;

    public GuiConfigure(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
        this.itemStack = entityPlayer.getHeldItem();
        this.magicalEarmuffs = (IMagicalEarmuffs) this.itemStack.getItem();
    }
}

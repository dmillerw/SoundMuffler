package dmillerw.sound.client.gui;

import dmillerw.sound.api.IMagicalEarmuffs;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author dmillerw
 */
public class GuiConfigure extends GuiScreen {

    private static final ResourceLocation GUI_BLANK = new ResourceLocation("soundmuffler++:textures/gui/blank.png");

    private static final int X_SIZE = 176;
    private static final int Y_SIZE = 166;

    private int guiLeft;
    private int guiTop;

    private EntityPlayer entityPlayer;

    private ItemStack itemStack;
    private IMagicalEarmuffs magicalEarmuffs;

    public GuiConfigure(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
        this.itemStack = entityPlayer.getHeldItem();
        this.magicalEarmuffs = (IMagicalEarmuffs) this.itemStack.getItem();
    }

    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - X_SIZE) / 2;
        this.guiTop = (this.height - Y_SIZE) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partial) {
        super.drawScreen(mouseX, mouseY, partial);

        mc.getTextureManager().bindTexture(GUI_BLANK);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, X_SIZE, Y_SIZE);
    }
}

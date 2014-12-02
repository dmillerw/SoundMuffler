package dmillerw.sound.client.gui;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * @author dmillerw
 */
public class GuiUVButton extends GuiButtonExt {

    private final ResourceLocation resourceLocation;

    private int u;
    private int v;
    private int w;
    private int h;

    public GuiUVButton(int id, int xPos, int yPos, int u, int v, int w, int h, final ResourceLocation resourceLocation) {
        super(id, xPos, yPos, 16, 16, "");

        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
        this.resourceLocation = resourceLocation;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            GuiUtils.drawContinuousTexturedBox(buttonTextures, this.xPosition, this.yPosition, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
            this.mouseDragged(mc, mouseX, mouseY);
            mc.getTextureManager().bindTexture(resourceLocation);
            drawTexturedModalRect(this.xPosition + 1, this.yPosition + 1, u, v, w, h);
        }
    }
}

package dmillerw.sound.client.gui;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author dmillerw
 */
public class GuiUVButton extends GuiButtonExt {

    private final ResourceLocation resourceLocation;

    private int u;
    private int v;
    private int w;
    private int h;

    private String tooltip;

    public GuiUVButton(int id, int xPos, int yPos, int u, int v, int w, int h, final ResourceLocation resourceLocation) {
        super(id, xPos, yPos, 16, 16, "");

        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
        this.resourceLocation = resourceLocation;
    }

    public GuiUVButton setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
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

    public void drawTooltip(Minecraft mc, int mouseX, int mouseY) {
        if (field_146123_n) {
            if (tooltip != null && !tooltip.isEmpty()) {
                drawHoveringText(mc, Arrays.asList(tooltip), mouseX, mouseY, mc.fontRenderer);
            }
        }
    }

    private void drawHoveringText(Minecraft mc, List<String> text, int x, int y, FontRenderer fontRenderer) {
        if (!text.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = text.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                int l = fontRenderer.getStringWidth(s);

                if (l > k) {
                    k = l;
                }
            }

            int j2 = x + 12;
            int k2 = y - 12;
            int i1 = 8;

            if (text.size() > 1) {
                i1 += 2 + (text.size() - 1) * 10;
            }

            if (j2 + k > mc.currentScreen.width) {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > mc.currentScreen.height) {
                k2 = mc.currentScreen.height - i1 - 6;
            }

            int j1 = -267386864;
            this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < text.size(); ++i2) {
                String s1 = text.get(i2);
                fontRenderer.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0) {
                    k2 += 2;
                }

                k2 += 10;
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
}

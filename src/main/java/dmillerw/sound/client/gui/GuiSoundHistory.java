package dmillerw.sound.client.gui;

import dmillerw.sound.client.sound.SoundHandler;
import dmillerw.sound.core.lib.ModInfo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * @author dmillerw
 */
public class GuiSoundHistory extends GuiScreen {

    private static final ResourceLocation GUI_BLANK = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/history.png");

    private static final int X_SIZE = 176;
    private static final int Y_SIZE = 166;

    private static final int LIST_X = 12;
    private static final int LIST_Y = 14;

    private static final int LIST_X_END = 144;

    private static final int MAX_LINE_COUNT = 15;

    private long lastClickTime;

    private int guiLeft;
    private int guiTop;
    private int selectedIndex = -1;

    private GuiUVButton buttonSelect;
    private GuiUVButton buttonBack;

    public GuiSoundHistory() {
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        Keyboard.enableRepeatEvents(false);
    }

    public void initGui() {
        super.initGui();

        this.guiLeft = (this.width - X_SIZE) / 2;
        this.guiTop = (this.height - Y_SIZE) / 2;

        this.buttonList.add(buttonSelect = new GuiUVButton(0, 153, 143, 176, 70, 14, 14, GUI_BLANK).setTooltip(I18n.translateToLocal(ModInfo.MOD_ID + ":tooltip.select")));
        this.buttonList.add(buttonBack = new GuiUVButton(1, 153, 123, 176, 56, 14, 14, GUI_BLANK).setTooltip(I18n.translateToLocal(ModInfo.MOD_ID + ":tooltip.back")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partial) {
        mc.getTextureManager().bindTexture(GUI_BLANK);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, X_SIZE, Y_SIZE);

        final int minX = guiLeft + LIST_X - 5;
        final int maxX = guiLeft + LIST_X_END + 5;

        for (int i = 0; i < Math.min(SoundHandler.soundHistory.size(), MAX_LINE_COUNT); i++) {
            final int minY = guiTop + LIST_Y + (mc.fontRendererObj.FONT_HEIGHT * i);
            final int maxY = minY + mc.fontRendererObj.FONT_HEIGHT;

            //TODO: Why no work!?
            /*if (selectedIndex == i) {
                GlStateManager.pushMatrix();
                GlStateManager.disableTexture2D();

                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer vertexBuffer = tessellator.getBuffer();

                vertexBuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);

                vertexBuffer.color(51, 51, 51, 255);
                vertexBuffer.pos(minX, maxY, zLevel);
                vertexBuffer.pos(maxX, maxY, zLevel);
                vertexBuffer.pos(maxX, minY, zLevel);
                vertexBuffer.pos(minX, minY, zLevel);

                tessellator.draw();

                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }*/

            String history = SoundHandler.soundHistory.get(i);
            if (selectedIndex == i)
                history = TextFormatting.UNDERLINE + "" + TextFormatting.YELLOW + history + TextFormatting.RESET;

            mc.fontRendererObj.drawString(history, guiLeft + LIST_X, guiTop + LIST_Y + (mc.fontRendererObj.FONT_HEIGHT * i), 0xFFFFFF);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 0);
        super.drawScreen(mouseX - guiLeft, mouseY - guiTop, partial);
        GlStateManager.popMatrix();

        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton guiButton = (GuiButton) this.buttonList.get(i);
            if (guiButton instanceof GuiUVButton)
                ((GuiUVButton) guiButton).drawTooltip(mc, mouseX, mouseY);
        }
    }

    @Override
    protected void keyTyped(char key, int keycode) {
        if (keycode == 1) {
            GuiSoundMuffler.reopen();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);

        if (mouseButton != 0)
            return;

        final int minX = guiLeft + LIST_X;
        final int maxX = guiLeft + LIST_X_END;

        for (int i = 0; i < Math.min(SoundHandler.soundHistory.size(), MAX_LINE_COUNT); i++) {
            final int minY = guiTop + LIST_Y + (mc.fontRendererObj.FONT_HEIGHT * i);
            final int maxY = minY + mc.fontRendererObj.FONT_HEIGHT;

            if (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY) {
                selectedIndex = i;
                long timeSinceLastClick = System.currentTimeMillis() - lastClickTime;
                if (timeSinceLastClick <= 250) {
                    if (selectedIndex != -1)
                        actionPerformed(buttonSelect);
                }
                lastClickTime = System.currentTimeMillis();
                return;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.id == 0) {
            if (selectedIndex >= 0) {
                GuiSoundMuffler.textFieldOverride = SoundHandler.soundHistory.get(selectedIndex);
                GuiSoundMuffler.reopen();
            }
        } else if (guiButton.id == 1) {
            GuiSoundMuffler.reopen();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

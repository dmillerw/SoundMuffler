package dmillerw.sound.client.gui;

import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.api.SoundEntry;
import dmillerw.sound.core.network.packet.PacketItemSoundMuffler;
import dmillerw.sound.core.network.packet.PacketTileSoundMuffler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * @author dmillerw
 */
public class GuiTileSoundMuffler extends GuiScreen {

    private static final ResourceLocation GUI_BLANK = new ResourceLocation("soundmuffler++:textures/gui/blank.png");

    private static final int X_SIZE = 176;
    private static final int Y_SIZE = 166;

    private static final int LIST_X = 12;
    private static final int LIST_Y = 31;

    private static final int LIST_X_END = 144;

    private static final int MAX_LINE_COUNT = 14;

    protected static String textFieldOverride;

    private int guiLeft;
    private int guiTop;

    private int listOffset = 0;
    private int selectedIndex = -1;

    private GuiTextField textFieldSound;
    private GuiTextField textFieldVolume;

    private GuiUVButton buttonUp;
    private GuiUVButton buttonDown;
    private GuiUVButton buttonDelete;
    private GuiUVButton buttonSearch;

    private EntityPlayer entityPlayer;

    private ITileSoundMuffler tileSoundMuffler;

    private List<SoundEntry> soundEntrySet;

    public GuiTileSoundMuffler(EntityPlayer entityPlayer, ITileSoundMuffler tileSoundMuffler) {
        this.entityPlayer = entityPlayer;
        this.tileSoundMuffler = tileSoundMuffler;
        this.soundEntrySet = this.tileSoundMuffler.getSoundEntries();

        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        textFieldOverride = "";
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        this.textFieldSound.updateCursorCounter();
    }

    public void initGui() {
        super.initGui();

        this.guiLeft = (this.width - X_SIZE) / 2;
        this.guiTop = (this.height - Y_SIZE) / 2;

        this.textFieldSound = new GuiTextField(mc.fontRenderer, guiLeft + 29, guiTop + 11, 101, 20);
        this.textFieldSound.setFocused(true);
        this.textFieldSound.setEnableBackgroundDrawing(false);
        if (textFieldOverride != null && !textFieldOverride.isEmpty())
            this.textFieldSound.setText(textFieldOverride);

        this.textFieldVolume = new GuiTextField(mc.fontRenderer, guiLeft + 145, guiTop + 11, 18, 20);
        this.textFieldVolume.setFocused(false);
        this.textFieldVolume.setEnableBackgroundDrawing(false);

        this.buttonList.add(buttonUp = new GuiUVButton(0, 153, 26, 176, 14, 14, 14, GUI_BLANK));
        this.buttonList.add(buttonDown = new GuiUVButton(1, 153, 46, 176, 28, 14, 14, GUI_BLANK));
        this.buttonList.add(buttonDelete = new GuiUVButton(2, 153, 143, 176, 0, 14, 14, GUI_BLANK));
        this.buttonList.add(buttonSearch = new GuiUVButton(3, 7, 7, 176, 41, 14, 14, GUI_BLANK));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partial) {
        mc.getTextureManager().bindTexture(GUI_BLANK);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, X_SIZE, Y_SIZE);

        this.textFieldSound.drawTextBox();
        this.textFieldVolume.drawTextBox();

        final int minX = guiLeft + LIST_X - 5;
        final int maxX = guiLeft + LIST_X_END + 5;

        for (int i=0; i<Math.min(soundEntrySet.size(), MAX_LINE_COUNT); i++) {
            final int minY = guiTop + LIST_Y + (mc.fontRenderer.FONT_HEIGHT * i);
            final int maxY = minY + mc.fontRenderer.FONT_HEIGHT;

            if (selectedIndex == listOffset + i) {
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.setColorOpaque_I(0x333333);
                tessellator.addVertex(minX, maxY, zLevel);
                tessellator.addVertex(maxX, maxY, zLevel);
                tessellator.addVertex(maxX, minY, zLevel);
                tessellator.addVertex(minX, minY, zLevel);
                tessellator.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glPopMatrix();
            }

            SoundEntry soundEntry = soundEntrySet.get(listOffset + i);
            String volume = String.valueOf(soundEntry.volumeModifier) + "%";
            mc.fontRenderer.drawString(soundEntry.name, guiLeft + LIST_X, guiTop + LIST_Y + (mc.fontRenderer.FONT_HEIGHT * i), 0xFFFFFF);

            // To offset the number display by half a pixel (woo, cheating)
            GL11.glPushMatrix();
            GL11.glTranslated(0, 0.75, 0);
            mc.fontRenderer.drawString(volume, guiLeft + LIST_X_END - (mc.fontRenderer.getStringWidth(volume)), guiTop + LIST_Y + (mc.fontRenderer.FONT_HEIGHT * i), 0xFFFFFF);
            GL11.glPopMatrix();
        }

        GL11.glPushMatrix();
        GL11.glTranslated(guiLeft, guiTop, 0);
        super.drawScreen(mouseX - guiLeft, mouseY - guiTop, partial);
        GL11.glPopMatrix();
    }

    @Override
    protected void keyTyped(char key, int keycode) {
        super.keyTyped(key, keycode);

        if (keycode == Keyboard.KEY_TAB) {
            if (this.textFieldSound.isFocused()) {
                this.textFieldSound.setFocused(false);
                this.textFieldVolume.setFocused(true);
                return;
            } else if (this.textFieldVolume.isFocused()) {
                this.textFieldSound.setFocused(true);
                this.textFieldVolume.setFocused(false);
                return;
            }
        }

        if (keycode == Keyboard.KEY_RETURN) {
            String sound = this.textFieldSound.getText();
            String volume = this.textFieldVolume.getText();

            if (!sound.isEmpty() && !volume.isEmpty()) {
                SoundEntry soundEntry = new SoundEntry(sound, Integer.parseInt(volume));

                soundEntrySet.add(soundEntry);
                PacketItemSoundMuffler.addSoundEntry(soundEntry);

                this.textFieldSound.setText("");
                this.textFieldVolume.setText("");

                return;
            }
        }

        if (this.textFieldSound.isFocused()) {
            this.textFieldSound.textboxKeyTyped(key, keycode);
        } else if (this.textFieldVolume.isFocused()) {
            if (keycode == Keyboard.KEY_BACK || Character.isDigit(key)) {
                String currentVolume = this.textFieldVolume.getText();
                if (Character.isDigit(key) && currentVolume != null && currentVolume.length() >= 3)
                    return;

                this.textFieldVolume.textboxKeyTyped(key, keycode);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);

        if (mouseButton != 0)
            return;

        final int minX = guiLeft + LIST_X;
        final int maxX = guiLeft + LIST_X_END;

        for (int i=0; i<Math.min(soundEntrySet.size(), MAX_LINE_COUNT); i++) {
            final int minY = guiTop + LIST_Y + (mc.fontRenderer.FONT_HEIGHT * i);
            final int maxY = minY + mc.fontRenderer.FONT_HEIGHT;

            if (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY) {
                selectedIndex = listOffset + i;
                return;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.id == 0) {
            listOffset -= 1;
            if (listOffset < 0) listOffset = 0;
        } else if (guiButton.id == 1) {
            final int max = Math.max(0, soundEntrySet.size() - MAX_LINE_COUNT);
            listOffset += 1;
            if (listOffset > max) listOffset = max;
        } else if (guiButton.id == 2) {
            if (selectedIndex != -1) {
                SoundEntry soundEntry = soundEntrySet.get(selectedIndex);
                soundEntrySet.remove(soundEntry);

                PacketTileSoundMuffler.removeSoundEntry(tileSoundMuffler, soundEntry);

                selectedIndex = -1;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

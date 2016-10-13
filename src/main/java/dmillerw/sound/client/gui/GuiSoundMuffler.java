package dmillerw.sound.client.gui;

import dmillerw.sound.SoundMuffler;
import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.api.SoundEntry;
import dmillerw.sound.core.handler.GuiHandler;
import dmillerw.sound.core.handler.InternalHandler;
import dmillerw.sound.core.lib.ModInfo;
import dmillerw.sound.core.network.PacketSoundMuffler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

/**
 * @author dmillerw
 */
public abstract class GuiSoundMuffler extends GuiScreen {

    private static final ResourceLocation GUI_BLANK = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/configure.png");

    private static final int X_SIZE = 176;
    private static final int Y_SIZE = 166;

    private static final int LIST_X = 12;
    private static final int LIST_Y = 31;

    private static final int LIST_X_END = 144;

    private static final int MAX_LINE_COUNT = 14;

    protected static String textFieldOverride;

    private static int lastX;
    private static int lastY;
    private static int lastZ;

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
    private GuiUVButton buttonHistory;

    private List<SoundEntry> soundEntryList;

    public GuiSoundMuffler(List<SoundEntry> soundEntryList) {
        this.soundEntryList = soundEntryList;

        Keyboard.enableRepeatEvents(true);
    }

    public static void cacheLastCoordinates(int x, int y, int z) {
        lastX = x;
        lastY = y;
        lastZ = z;
    }

    public static void reopen() {
        InternalHandler.openConfigurationGUI(Minecraft.getMinecraft().thePlayer, lastX, lastY, lastZ);
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

        this.textFieldSound = new GuiTextField(-1, mc.fontRendererObj, guiLeft + 29, guiTop + 11, 101, 20);
        this.textFieldSound.setFocused(true);
        this.textFieldSound.setEnableBackgroundDrawing(false);
        if (textFieldOverride != null && !textFieldOverride.isEmpty()) {
            if (textFieldOverride.charAt(0) == '.')
                textFieldOverride = textFieldOverride.substring(1, textFieldOverride.length());

            this.textFieldSound.setText(textFieldOverride);
        }

        this.textFieldVolume = new GuiTextField(-2, mc.fontRendererObj, guiLeft + 145, guiTop + 11, 18, 20);
        this.textFieldVolume.setFocused(false);
        this.textFieldVolume.setEnableBackgroundDrawing(false);

        this.buttonList.add(buttonUp = new GuiUVButton(0, 153, 26, 176, 14, 14, 14, GUI_BLANK).setTooltip(I18n.format("tooltip.scrollUp")));
        this.buttonList.add(buttonDown = new GuiUVButton(1, 153, 46, 176, 28, 14, 14, GUI_BLANK).setTooltip(I18n.format("tooltip.scrollDown")));
        this.buttonList.add(buttonDelete = new GuiUVButton(2, 153, 143, 176, 0, 14, 14, GUI_BLANK).setTooltip(I18n.format("tooltip.remove")));
        this.buttonList.add(buttonSearch = new GuiUVButton(3, 7, 7, 176, 41, 14, 14, GUI_BLANK).setTooltip(I18n.format("tooltip.search")));
        this.buttonList.add(buttonHistory = new GuiUVButton(4, 153, 66, 176, 56, 14, 14, GUI_BLANK).setTooltip(I18n.format("tooltip.history")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partial) {
        scrollMouse(Mouse.getDWheel());

        mc.getTextureManager().bindTexture(GUI_BLANK);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, X_SIZE, Y_SIZE);

        this.textFieldSound.drawTextBox();
        this.textFieldVolume.drawTextBox();

        final int minX = guiLeft + LIST_X - 5;
        final int maxX = guiLeft + LIST_X_END + 5;

        for (int i=0; i<Math.min(soundEntryList.size(), MAX_LINE_COUNT); i++) {
            final int minY = guiTop + LIST_Y + (mc.fontRendererObj.FONT_HEIGHT * i);
            final int maxY = minY + mc.fontRendererObj.FONT_HEIGHT;

            if (selectedIndex == listOffset + i) {
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
                vertexBuffer.finishDrawing();

                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }

            SoundEntry soundEntry = soundEntryList.get(listOffset + i);
            String volume = String.valueOf(soundEntry.volumeModifier) + "%";
            mc.fontRendererObj.drawString(soundEntry.name, guiLeft + LIST_X, guiTop + LIST_Y + (mc.fontRendererObj.FONT_HEIGHT * i), 0xFFFFFF);

            // To offset the number display by half a pixel (woo, cheating)
            GL11.glPushMatrix();
            GL11.glTranslated(0, 0.75, 0);
            mc.fontRendererObj.drawString(volume, guiLeft + LIST_X_END - (mc.fontRendererObj.getStringWidth(volume)), guiTop + LIST_Y + (mc.fontRendererObj.FONT_HEIGHT * i), 0xFFFFFF);
            GL11.glPopMatrix();
        }

        GL11.glPushMatrix();
        GL11.glTranslated(guiLeft, guiTop, 0);
        super.drawScreen(mouseX - guiLeft, mouseY - guiTop, partial);
        GL11.glPopMatrix();

        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton guiButton = (GuiButton) this.buttonList.get(i);
            if (guiButton instanceof GuiUVButton)
                ((GuiUVButton) guiButton).drawTooltip(mc, mouseX, mouseY);
        }
    }

    private void scrollMouse(int theta) {
        if (theta > 0) {
            listOffset -= 1;
            if (listOffset < 0) listOffset = 0;
        } else if (theta < 0) {
            final int max = Math.max(0, soundEntryList.size() - MAX_LINE_COUNT);
            listOffset += 1;
            if (listOffset > max) listOffset = max;
        }
    }

    @Override
    protected void keyTyped(char key, int keycode) throws IOException {
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

                soundEntryList.add(soundEntry);
                addSoundEntry(soundEntry);

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

                currentVolume = this.textFieldVolume.getText();
                if (currentVolume != null && !currentVolume.isEmpty() && Integer.parseInt(currentVolume) > 100)
                    this.textFieldVolume.setText("100");
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);

        if (mouseButton != 0)
            return;

        final int minX = guiLeft + LIST_X;
        final int maxX = guiLeft + LIST_X_END;

        for (int i=0; i<Math.min(soundEntryList.size(), MAX_LINE_COUNT); i++) {
            final int minY = guiTop + LIST_Y + (mc.fontRendererObj.FONT_HEIGHT * i);
            final int maxY = minY + mc.fontRendererObj.FONT_HEIGHT;

            if (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY) {
                selectedIndex = listOffset + i;
                buttonDelete.enabled = true;
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
            final int max = Math.max(0, soundEntryList.size() - MAX_LINE_COUNT);
            listOffset += 1;
            if (listOffset > max) listOffset = max;
        } else if (guiButton.id == 2) {
            if (selectedIndex != -1) {
                SoundEntry soundEntry = soundEntryList.get(selectedIndex);
                soundEntryList.remove(soundEntry);

                removeSoundEntry(soundEntry);

                selectedIndex = -1;
                buttonDelete.enabled = false;
            }
        } else if (guiButton.id == 3) {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
            entityPlayer.openGui(SoundMuffler.instance, GuiHandler.GUI_SEARCH, entityPlayer.worldObj, 0, 0, 0);
        } else if (guiButton.id == 4) {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
            entityPlayer.openGui(SoundMuffler.instance, GuiHandler.GUI_HISTORY, entityPlayer.worldObj, 0, 0, 0);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public abstract void addSoundEntry(SoundEntry soundEntry);
    public abstract void removeSoundEntry(SoundEntry soundEntry);

    public static class Tile extends GuiSoundMuffler {

        public ITileSoundMuffler tileSoundMuffler;

        public Tile(ITileSoundMuffler tileSoundMuffler) {
            super(tileSoundMuffler.getSoundEntries());

            this.tileSoundMuffler = tileSoundMuffler;
        }

        @Override
        public void addSoundEntry(SoundEntry soundEntry) {
            PacketSoundMuffler.sendPacket(tileSoundMuffler, soundEntry, PacketSoundMuffler.Type.ADD);
        }

        @Override
        public void removeSoundEntry(SoundEntry soundEntry) {
            PacketSoundMuffler.sendPacket(tileSoundMuffler, soundEntry, PacketSoundMuffler.Type.REMOVE);
        }
    }

    public static class Item extends GuiSoundMuffler {

        public ItemStack itemStack;

        public Item(ItemStack itemStack) {
            super(((IItemSoundMuffler)itemStack.getItem()).getSoundEntries(itemStack));

            this.itemStack = itemStack;
        }

        @Override
        public void addSoundEntry(SoundEntry soundEntry) {
            PacketSoundMuffler.sendPacket(soundEntry, PacketSoundMuffler.Type.ADD);
        }

        @Override
        public void removeSoundEntry(SoundEntry soundEntry) {
            PacketSoundMuffler.sendPacket(soundEntry, PacketSoundMuffler.Type.REMOVE);
        }
    }
}

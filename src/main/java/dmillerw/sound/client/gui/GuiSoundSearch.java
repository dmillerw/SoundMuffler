package dmillerw.sound.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dmillerw.sound.client.sound.SoundHelper;
import joptsimple.internal.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Set;

/**
 * @author dmillerw
 */
public class GuiSoundSearch extends GuiScreen {

    private static final ResourceLocation GUI_BLANK = new ResourceLocation("soundmuffler++:textures/gui/search.png");

    private static final int X_SIZE = 176;
    private static final int Y_SIZE = 166;

    private static final int LIST_X = 12;
    private static final int LIST_Y = 31;

    private static final int LIST_X_END = 144;

    private static final int MAX_LINE_COUNT = 14;

    private GuiTextField searchField;

    private GuiUVButton buttonUp;
    private GuiUVButton buttonDown;
    private GuiUVButton buttonBack;
    private GuiUVButton buttonAccept;
    private GuiUVButton buttonPlay;

    private int guiLeft;
    private int guiTop;

    private int listOffset = 0;
    private int selectedIndex = -1;

    private long lastClickTime;

    private List<SoundEntryString> listContents = Lists.newArrayList();

    public GuiSoundSearch() {
        Keyboard.enableRepeatEvents(true);
    }

    public void initGui() {
        super.initGui();

        this.guiLeft = (this.width - X_SIZE) / 2;
        this.guiTop = (this.height - Y_SIZE) / 2;

        this.searchField = new GuiTextField(mc.fontRenderer, guiLeft + LIST_X, guiTop + 11, LIST_X_END - LIST_X, 20);
        this.searchField.setFocused(true);
        this.searchField.setCanLoseFocus(false);
        this.searchField.setEnableBackgroundDrawing(false);

        this.buttonList.add(buttonUp = new GuiUVButton(0, 153, 26, 176, 14, 14, 14, GUI_BLANK).setTooltip(StatCollector.translateToLocal("tooltip.scrollUp")));
        this.buttonList.add(buttonDown = new GuiUVButton(1, 153, 46, 176, 28, 14, 14, GUI_BLANK).setTooltip(StatCollector.translateToLocal("tooltip.scrollDown")));
        this.buttonList.add(buttonBack = new GuiUVButton(2, 153, 123, 176, 56, 14, 14, GUI_BLANK).setTooltip(StatCollector.translateToLocal("tooltip.back")));
        this.buttonList.add(buttonAccept = new GuiUVButton(3, 153, 143, 176, 70, 14, 14, GUI_BLANK).setTooltip(StatCollector.translateToLocal("tooltip.select")));
        this.buttonList.add(buttonPlay = new GuiUVButton(4, 153, 103, 176, 84, 14, 14, GUI_BLANK).setTooltip(StatCollector.translateToLocal("tooltip.preview")));

        refresh();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        this.searchField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partial) {
        scrollMouse(Mouse.getDWheel());

        mc.getTextureManager().bindTexture(GUI_BLANK);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, X_SIZE, Y_SIZE);

        final int minX = guiLeft + LIST_X - 5;
        final int maxX = guiLeft + LIST_X_END + 5;

        for (int i=0; i<Math.min(listContents.size(), MAX_LINE_COUNT); i++) {
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

            SoundEntryString soundEntryString = listContents.get(listOffset + i);
            String string = soundEntryString.string;
            if (soundEntryString.end)
                string = EnumChatFormatting.UNDERLINE + string + EnumChatFormatting.RESET;
            mc.fontRenderer.drawString(string, guiLeft + LIST_X, guiTop + LIST_Y + (mc.fontRenderer.FONT_HEIGHT * i), soundEntryString.end ? 0xFF0000 : 0xFFFFFF);
        }

        this.searchField.drawTextBox();

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
            final int max = Math.max(0, listContents.size() - MAX_LINE_COUNT);
            listOffset += 1;
            if (listOffset > max) listOffset = max;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);

        if (mouseButton != 0)
            return;

        final int minX = guiLeft + LIST_X;
        final int maxX = guiLeft + LIST_X_END;

        for (int i=0; i<Math.min(listContents.size(), MAX_LINE_COUNT); i++) {
            final int minY = guiTop + LIST_Y + (mc.fontRenderer.FONT_HEIGHT * i);
            final int maxY = minY + mc.fontRenderer.FONT_HEIGHT;

            if (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY) {
                selectedIndex = listOffset + i;
                long timeSinceLastClick = System.currentTimeMillis() - lastClickTime;
                if (timeSinceLastClick <= 250) {
                    String text = searchField.getText();
                    SoundEntryString soundEntryString = listContents.get(selectedIndex);
                    if (text == null || text.isEmpty()) {
                        text = soundEntryString.string;
                    } else {
                        text = soundEntryString.fullPath;
                    }

                    if (!soundEntryString.end) {
                        searchField.setText(text);
                    } else {
                        actionPerformed(buttonAccept);
                    }

                    refresh();
                }
                lastClickTime = System.currentTimeMillis();
                return;
            }
        }
    }

    @Override
    protected void keyTyped(char key, int keycode) {
        if (keycode == 1) {
            GuiSoundMuffler.reopen();
            return;
        }

        if (this.searchField.isFocused()) {
            this.searchField.textboxKeyTyped(key, keycode);
            refresh();
        }
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.id == 0) {
            listOffset -= 1;
            if (listOffset < 0) listOffset = 0;
        } else if (guiButton.id == 1) {
            final int max = Math.max(0, listContents.size() - MAX_LINE_COUNT);
            listOffset += 1;
            if (listOffset > max) listOffset = max;
        } else if (guiButton.id == 2) {
            String text = searchField.getText();
            if (text != null && !text.isEmpty()) {
                if (text.contains(".")) {
                    searchField.setText(text.substring(0, text.lastIndexOf(".")));
                } else {
                    searchField.setText("");
                }
                refresh();
            }
        } else if (guiButton.id == 3) {
            if (selectedIndex != -1) {
                String text = searchField.getText() != null ? searchField.getText() + "." : "";
                SoundEntryString soundEntryString = listContents.get(selectedIndex);
                GuiSoundMuffler.textFieldOverride = text + soundEntryString.string + (!soundEntryString.end ? ".*" : "");
                GuiSoundMuffler.reopen();
            }
        } else if (guiButton.id == 4) {
            SoundEntryString soundEntryString = listContents.get(selectedIndex);
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147673_a(new ResourceLocation(soundEntryString.resourceDomain, soundEntryString.fullPath)));
        }
    }

    private void refresh() {
        listOffset = 0;

        listContents.clear();

        Set<SoundEntryString> temporarySet = Sets.newHashSet();

        String text = this.searchField.getText();
        List<ResourceLocation> sounds = SoundHelper.getSoundsForCategory(SoundCategory.values());
        for (ResourceLocation resourceLocation : sounds) {
            String path = resourceLocation.getResourcePath();

            if (!path.startsWith(text))
                continue;

            // Split search and path into readable segments
            String[] splitText = text.replace(".", "/").split("/");
            String[] splitPath = path.replace(".", "/").split("/");

            int displayIndex = -1;
            String display = "";

            // Loop through each segment of the FULL sound name
            for (int i=0; i<splitPath.length; i++) {
                // If the index of the path segment is beyond the length of the search text, simply display
                // the last segment, and break out
                if (i >= splitText.length) {
                    displayIndex = i;
                    display = splitPath[i];
                    break;
                }

                // Otherwise, see if the path segment and the search segment are equal.
                // If so, move on to the next one. Otherwise, display and break
                if (!splitPath[i].equals(splitText[i])) {
                    displayIndex = i;
                    display = splitPath[i];
                    break;
                }
            }

            // If we got through the loop without assigning display, simply set
            // it to the last segment of the full path
            if (display == null || display.isEmpty()) {
                display = splitPath[splitPath.length - 1];
            }

            // Full path, used for determining what to add to the search field
            // when double clicked
            String fullPath = Strings.join(splitPath, ".");

            // If last index is set, we broke out early, so the path is set to the segments
            // found before the breakout
            if (displayIndex != -1) {
                fullPath = Strings.join(ArrayUtils.subarray(splitPath, 0, displayIndex + 1), ".");
            }

            temporarySet.add(new SoundEntryString(display, resourceLocation.getResourceDomain(), fullPath, path.endsWith(display) && displayIndex == splitPath.length - 1));
        }

        listContents.addAll(temporarySet);
    }
}

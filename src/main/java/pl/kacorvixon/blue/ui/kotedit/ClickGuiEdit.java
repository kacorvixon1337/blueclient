package pl.kacorvixon.blue.ui.kotedit;

import pl.kacorvixon.blue.module.Category;

import pl.kacorvixon.blue.util.render.RenderUtil;
import pl.kacorvixon.blue.util.render.animation.Animation;
import pl.kacorvixon.blue.util.render.animation.Easings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;


public class ClickGuiEdit extends GuiScreen {
    private final List<ComponentEdit> components = new ArrayList<>();
    private static pl.kacorvixon.blue.ui.kotedit.ClickGuiEdit INSTANCE;
    private ComponentEdit selectedPanel;
    public final Animation animation = new Animation();
    private boolean sex = false;
    private double wasilukwas = 0;

    public ClickGuiEdit() {

        int panelX = 2;

        for (Category category : Category.values()) {
            CategoryPanelEdit panel = new CategoryPanelEdit(category, panelX, 4);
            this.components.add(panel);
            panelX += 150;
            selectedPanel = panel;
        }
    }

    @Override
    public void initGui() {
        wasilukwas = 1;
        sex = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        animation.update();
        animation.animate(wasilukwas, sex ? 0.3 : 0.8, sex ? Easings.BOUNCE_IN : Easings.BOUNCE_OUT, true);
        if (animation.getValue() < 0.01 && sex) {
            mc.displayGuiScreen(null);
            return;
        }
        for (ComponentEdit component : components) {
            final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            RenderUtil.startScale(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, (float) animation.getValue());
            component.drawComponent(sr, mouseX, mouseY);
            RenderUtil.endScale();
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        selectedPanel.onKeyPress(keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE) {
            wasilukwas = 0;
            sex = true;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (int i = components.size() - 1; i >= 0; i--) {
            ComponentEdit component = components.get(i);
            int x = component.getX();
            int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandComponentEdit) {
                ExpandComponentEdit expandableComponent = (ExpandComponentEdit) component;
                if (expandableComponent.expanded) cHeight = expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() + 6 && mouseY < y + cHeight) {
                selectedPanel = component;
                component.onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }

    public void display() {
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    public static pl.kacorvixon.blue.ui.kotedit.ClickGuiEdit getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new pl.kacorvixon.blue.ui.kotedit.ClickGuiEdit();
        }

        return INSTANCE;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        selectedPanel.onMouseRelease(state);
    }
}

package pl.kacorvixon.blue.ui.kot;

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

//MADE BY KTNTKOT

public class ClickGui extends GuiScreen {
    private final List<Component> components = new ArrayList<>();
    private static ClickGui INSTANCE;
    private Component selectedPanel;
    public final Animation animation = new Animation();
    private boolean sex = false;
    private double wasilukwas = 0;

    public ClickGui() {

        int panelX = 2;

        for (Category category : Category.values()) {
            CategoryPanel panel = new CategoryPanel(category, panelX, 2);
            this.components.add(panel);
            panelX += 2;
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
        animation.animate(wasilukwas, sex ? 0.4 : 0.8, sex ? Easings.BOUNCE_IN : Easings.BOUNCE_OUT, true);
        if (animation.getValue() < 0.01 && sex) {
            mc.displayGuiScreen(null);
            return;
        }
        for (Component component : components) {
            final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            RenderUtil.startScale(  sr.getScaledWidth() / 2,  sr.getScaledHeight() / 2, (float) animation.getValue());
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
            Component component = components.get(i);
            int x = component.getX();
            int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandComponent) {
                ExpandComponent expandableComponent = (ExpandComponent) component;
                if (expandableComponent.expanded) cHeight = expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
                selectedPanel = component;
                component.onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }

    public void display() {
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }

        return INSTANCE;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        selectedPanel.onMouseRelease(state);
    }
}

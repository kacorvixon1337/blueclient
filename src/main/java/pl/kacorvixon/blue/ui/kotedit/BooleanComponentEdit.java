package pl.kacorvixon.blue.ui.kotedit;

import pl.kacorvixon.blue.module.ModuleAdministration;
import pl.kacorvixon.blue.module.impl.render.Hud;
import pl.kacorvixon.blue.property.Property;

import pl.kacorvixon.blue.util.render.animation.ColorAnimation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;


public final class BooleanComponentEdit extends ComponentEdit implements PropertyComponentEdit {

    private final Property<Boolean> booleanProperty;
    private int buttonLeft;
    private int buttonTop;
    private int buttonRight;
    private int buttonBottom;
    private ColorAnimation colorAnimation = new ColorAnimation();

    public BooleanComponentEdit(ComponentEdit parent, Property<Boolean> booleanProperty, int x, int y, int width, int height) {
        super(parent, booleanProperty.name, x, y, width, height);
        this.booleanProperty = booleanProperty;
        colorAnimation.animate((booleanProperty.value ? new Color(239, 10, 10).getRGB() : new Color(40, 38, 38).getRGB()), 0.001);
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        colorAnimation.update();
        colorAnimation.animate(booleanProperty.value ? ModuleAdministration.getInstance(Hud.class).colorValue.value.getRGB() : new Color(40, 38, 38).getRGB(), 0.07);
        Hud.getFontRenderer().drawStringWithShadow(name, getX() - 23 + getHeight() / 2 + 3, getY() + getHeight() / 2 - 3, 0xFFFFFF);
        Gui.drawRect(buttonLeft = (getX() + 101), buttonTop = (getY() + getHeight() / 2 - (getHeight() / 2 / 2)), buttonRight = getX() + 104 + getHeight() / 2, buttonBottom = (getY() + getHeight() / 2 + (getHeight() / 2 / 2) + 3), colorAnimation.getColor().getRGB());
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && mouseX > buttonLeft && mouseY > buttonTop - 3&& mouseY < buttonBottom)
            booleanProperty.value = (!booleanProperty.value);
    }

    @Override
    public Property<?> getProperty() {
        return booleanProperty;
    }
}

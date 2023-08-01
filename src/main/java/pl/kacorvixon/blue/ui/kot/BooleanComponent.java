package pl.kacorvixon.blue.ui.kot;

import pl.kacorvixon.blue.module.impl.render.Hud;
import pl.kacorvixon.blue.property.Property;
import pl.kacorvixon.blue.util.render.animation.ColorAnimation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

//MADE BY KTNTKOT

public final class BooleanComponent extends pl.kacorvixon.blue.ui.kot.Component implements PropertyComponent {

    private final Property<Boolean> booleanProperty;
    private int buttonLeft;
    private int buttonTop;
    private int buttonRight;
    private int buttonBottom;
    private ColorAnimation colorAnimation = new ColorAnimation();

    public BooleanComponent(Component parent, Property<Boolean> booleanProperty, int x, int y, int width, int height) {
        super(parent, booleanProperty.name, x, y, width, height);
        this.booleanProperty = booleanProperty;
        colorAnimation.animate((booleanProperty.value ? new Color(239, 10, 10).getRGB() : new Color(40, 38, 38).getRGB()),0.001);
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        colorAnimation.update();
        colorAnimation.animate(booleanProperty.value ? new Color(239, 10, 10).getRGB() : new Color(40, 38, 38).getRGB(),0.07);
        Hud.getFontRenderer().drawStringWithShadow(name, getX() - 23 + getHeight() / 2 + 3, getY() + getHeight() / 2 - 3, 0xFFFFFF);
        Gui.drawRect(buttonLeft = (getX() + 89), buttonTop = (getY() + getHeight() / 2 - (getHeight() / 2 / 2) - 1), buttonRight = getX() + 92 + getHeight() / 2, buttonBottom = (getY() + getHeight() / 2 + (getHeight() / 2 / 2) + 3),colorAnimation.getColor().getRGB());
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && mouseX > buttonLeft && mouseY > buttonTop - 3 && mouseX < buttonRight && mouseY < buttonBottom + 3)
            booleanProperty.value = (!booleanProperty.value);
    }

    @Override
    public Property<?> getProperty() {
        return booleanProperty;
    }
}
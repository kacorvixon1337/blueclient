package pl.kacorvixon.blue.ui.kotedit;

//MADE BY KTNTKOT

import pl.kacorvixon.blue.module.impl.render.Hud;
import pl.kacorvixon.blue.property.Property;
import pl.kacorvixon.blue.property.impl.EnumProperty;

import net.minecraft.client.gui.ScaledResolution;


public final class EnumComponentEdit extends pl.kacorvixon.blue.ui.kotedit.ComponentEdit implements PropertyComponentEdit {


    private final EnumProperty<?> property;

    private int buttonLeft;
    private int buttonTop;
    private int buttonRight;
    private int buttonBottom;

    public EnumComponentEdit(ComponentEdit parent, EnumProperty<?> property, int x, int y, int width, int height) {
        super(parent, property.name, x, y, width, height);
        this.property = property;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        Hud.getFontRenderer().drawStringWithShadow(name, getX() - 23 + getHeight() / 2 + 3, getY() + getHeight() / 2 - 3, 0xFFFFFF);
        buttonLeft = (getX() + 97);
        buttonTop = (getY() + getHeight() / 2 - (getHeight() / 2 / 2) - 2);
        buttonRight = getX() + 92 + getHeight() / 2;
        buttonBottom = (getY() + getHeight() / 2 + (getHeight() / 2 / 2) + 2);
        Hud.getFontRenderer().drawStringWithShadow(property.value.name(), x + width - Hud.getFontRenderer().getWidth(property.value.name()) + 11, buttonBottom = (int) (y + getHeight() / 2 - 3), -1);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && mouseX > buttonLeft - Hud.getFontRenderer().getWidth(property.value.name()) && mouseY > buttonTop - 8 && mouseY < buttonBottom + 8)
            property.increment();
    }

    @Override
    public Property<?> getProperty() {
        return property;
    }
}



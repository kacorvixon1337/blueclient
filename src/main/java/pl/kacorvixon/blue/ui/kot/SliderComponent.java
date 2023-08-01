package pl.kacorvixon.blue.ui.kot;

import pl.kacorvixon.blue.module.impl.render.Hud;
import pl.kacorvixon.blue.property.Property;
import pl.kacorvixon.blue.property.impl.NumberProperty;

import pl.kacorvixon.blue.util.render.animation.Animation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

import pl.kacorvixon.blue.ui.kot.Component;
import java.awt.*;
import java.math.BigDecimal;

//MADE BY KTNTKOT

public final class SliderComponent extends pl.kacorvixon.blue.ui.kot.Component implements PropertyComponent {
    private final NumberProperty doubleProperty;

    private boolean sliding;
    private Animation animation = new Animation();

    public SliderComponent(Component parent, NumberProperty property, int x, int y, int width, int height) {
        super(parent, property.name, x, y, width, height);
        this.doubleProperty = property;
    }

    @Override
    public void drawComponent(final ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        double sliderPercentage = ((doubleProperty.value - doubleProperty.minimum) / (doubleProperty.maximum - doubleProperty.minimum));
        animation.update();
        animation.animate(sliderPercentage,0.06,false);
        if (sliding) {
            if (isHovered(mouseX, mouseY))
                doubleProperty.setValue(MathHelper.clamp_double(roundToFirstDecimalPlace((mouseX - x) * (doubleProperty.maximum - doubleProperty.minimum) / width + doubleProperty.minimum), doubleProperty.minimum, doubleProperty.maximum));
            else sliding = false;
        }

        Gui.drawRect(x, y + 15, x + width, y + height - 2, -1);
        Gui.drawRect(x, y + 15, (int) (x + width * animation.getValue()), y + height - 2, new Color(239, 10, 10).getRGB());
        Hud.getFontRenderer().drawStringWithShadow(doubleProperty.name, x - 23 + 10, y + getHeight() / 2 - 6, -1);
        Hud.getFontRenderer().drawStringWithShadow(String.valueOf(roundToFirstDecimalPlace(Double.parseDouble(doubleProperty.value.toString()))), x + width - Hud.getFontRenderer().getWidth(String.valueOf(roundToFirstDecimalPlace(Double.parseDouble(doubleProperty.value.toString())))) + 12, y + getHeight() / 2 - 6, -1);
    }

    private double roundToFirstDecimalPlace(double value) {
        double inc = doubleProperty.increment;
        double halfOfInc = inc / 2.0D;
        double floored = Math.floor(value / inc) * inc;
        if (value >= floored + halfOfInc)
            return new BigDecimal(Math.ceil(value / inc) * inc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        else return new BigDecimal(floored).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (!sliding && button == 0 && isHovered(mouseX, mouseY)) sliding = true;
    }

    @Override
    public void onMouseRelease(int button) {
        this.sliding = false;
    }

    @Override
    public Property<?> getProperty() {
        return doubleProperty;
    }
}

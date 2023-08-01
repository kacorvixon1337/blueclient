package pl.kacorvixon.blue.ui.kot;


import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.module.impl.render.Hud;
import pl.kacorvixon.blue.property.Property;
import pl.kacorvixon.blue.property.impl.ColorProperty;
import pl.kacorvixon.blue.property.impl.EnumProperty;
import pl.kacorvixon.blue.property.impl.NumberProperty;

import pl.kacorvixon.blue.util.render.animation.Animation;
import pl.kacorvixon.blue.util.render.animation.ColorAnimation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.List;

//MADE BY KTNTKOT

public final class ModuleComponent extends ExpandComponent {

    private final Module module;
    private boolean binding;
    private ColorAnimation colorAnimation = new ColorAnimation();
    private Animation animation = new Animation();

    public ModuleComponent(pl.kacorvixon.blue.ui.kot.Component parent,
                           Module module,
                           int x, int y,
                           int width,
                           int height) {
        super(parent, module.rendername, x, y, width, height);

        this.module = module;

        List<Property<?>> properties = module.propertyList;

        int propertyX = 15;
        int propertyY = height;

        for (Property<?> property : properties) {
            pl.kacorvixon.blue.ui.kot.Component component = null;
            if (property.value.getClass() == Boolean.class) {
                component = new BooleanComponent(this, (Property<Boolean>) property, propertyX, propertyY, width - (15 * 2), 15);
            } else if (property instanceof NumberProperty) {
                component = new SliderComponent(this, (NumberProperty) property, propertyX, propertyY, width - (15 * 2), 18);
            } else if (property instanceof EnumProperty) {
                component = new EnumComponent(this, (EnumProperty<?>) property, propertyX, propertyY, width - (15 * 2), 15);
            } else if (property instanceof ColorProperty) {
                component = new ColorComponent(this,(ColorProperty) property,propertyX,propertyY,width - (15 * 2), 68);
            }

            if (component != null && property.checkDependency()) {
                this.children.add(component);
                propertyY += component.height;
            }
        }
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        int x = getX();
        int y = getY();
        colorAnimation.update();
        colorAnimation.animate(module.enabled ? -1 : new Color(102, 96, 96).getRGB(),0.05);

        if (expanded) {
            int childY = 15;
            for (pl.kacorvixon.blue.ui.kot.Component child : children) {
                int cHeight = child.height;

                if (child instanceof PropertyComponent) {
                    PropertyComponent propertyComponent = (PropertyComponent) child;
                    if (!propertyComponent.getProperty().checkDependency())
                        continue;
                }
                if (child instanceof ExpandComponent) {
                    ExpandComponent expandableComponent = (ExpandComponent) child;
                    if (expandableComponent.expanded)
                        cHeight = expandableComponent.getHeightWithExpand();
                }
                child.y = (childY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                childY += cHeight;
            }
        }

        Gui.drawRect(x, y, x + width, y + height, new Color(45, 43, 43).getRGB());
        Hud.getFontRenderer().drawStringWithShadow(name, x + 2, y + height / 2.0F - 4,colorAnimation.getColor().getRGB());
    }

    @Override
    public boolean canExpand() {
        return !children.isEmpty();
    }

    @Override
    public void onPress(int mouseX,
                        int mouseY,
                        int button) {
        switch (button) {
            case 0:
                module.setEnabled(!module.enabled);
                break;
            case 2:
                binding = !binding;
                break;
        }
    }

    @Override
    public int getHeightWithExpand() {
        int height = getHeight();
        int cHeight = 0;
        if (expanded) {
            for (Component child : children) {
                cHeight = child.getHeight();
                if (child instanceof PropertyComponent) {
                    PropertyComponent propertyComponent = (PropertyComponent) child;
                    if (!propertyComponent.getProperty().checkDependency())
                        continue;
                }
                if (child instanceof ExpandComponent) {
                    ExpandComponent expandableComponent = (ExpandComponent) child;
                    if (expandableComponent.expanded)
                        cHeight = expandableComponent.getHeightWithExpand();
                }
                height += cHeight;
            }
        }
        return height;
    }
}

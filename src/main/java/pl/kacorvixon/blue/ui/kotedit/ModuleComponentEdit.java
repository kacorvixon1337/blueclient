package pl.kacorvixon.blue.ui.kotedit;

import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.module.ModuleAdministration;
import pl.kacorvixon.blue.module.impl.render.Hud;
import pl.kacorvixon.blue.property.Property;
import pl.kacorvixon.blue.property.impl.*;

import pl.kacorvixon.blue.util.render.ColorUtil;
import pl.kacorvixon.blue.util.render.RenderUtil;
import pl.kacorvixon.blue.util.render.animation.Animation;
import pl.kacorvixon.blue.util.render.animation.ColorAnimation;
import pl.kacorvixon.blue.util.render.animation.Easings;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.List;


public final class ModuleComponentEdit extends ExpandComponentEdit {

    private final Module module;
    private boolean binding;

    private final Animation arrow = new Animation();
    private ColorAnimation colorAnimation = new ColorAnimation();
    private Animation animation = new Animation();

    public ModuleComponentEdit(ComponentEdit parent,
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
            ComponentEdit component = null;
            if (property instanceof BooleanProperty) {
                component = new BooleanComponentEdit(this, (Property<Boolean>) property, propertyX, propertyY, width - (15 * 2), 15);
            } else if (property instanceof NumberProperty) {
                component = new SliderComponentEdit(this, (NumberProperty) property, propertyX, propertyY, width - (15 * 2), 20);
            } else if (property instanceof EnumProperty) {
                component = new EnumComponentEdit(this, (EnumProperty<?>) property, propertyX, propertyY, width - (15 * 2), 15);
            } else if (property instanceof ColorProperty) {
                component = new ColorComponentEdit(this, (ColorProperty) property, propertyX, propertyY, width - (15 * 2), 68);
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
        colorAnimation.animate(
                module.enabled ? ModuleAdministration.getInstance(Hud.class).colorEnumValue.value.equals(Hud.color.Rainbow) ?
                        ColorUtil.createRainbowFromOffset(2400, 11 * 15) : ModuleAdministration.getInstance(Hud.class).colorValue.value.getRGB() : new Color(45, 43, 43).getRGB(), 0.1
        );
                //new Color(45, 43, 43).getRed(), 0.1);




                /*
                module.enabled ? ModuleAdministration.getInstance(Hud.class).colorValue.value.getRGB() :
                        new Color(45, 43, 43).getRGB(), 0.1);*/

        if (expanded) {
            int childY = 15;
            for (ComponentEdit child : children) {
                int cHeight = child.height;

                if (child instanceof PropertyComponentEdit) {
                    PropertyComponentEdit propertyComponent = (PropertyComponentEdit) child;
                    if (!propertyComponent.getProperty().checkDependency())
                        continue;
                }
                if (child instanceof ExpandComponentEdit) {
                    ExpandComponentEdit expandableComponent = (ExpandComponentEdit) child;
                    if (expandableComponent.expanded)
                        cHeight = expandableComponent.getHeightWithExpand();
                }
                child.y = (childY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                childY += cHeight;
            }
        }
        Gui.drawRect(x, y, x + width - 2, y + height, colorAnimation.getColor().getRGB());
        Hud.getFontRenderer().drawStringWithShadow(name, x + 2, y + height / 2.0F - 3.5f, -1);
        arrow.update();
        arrow.animate(expanded ? -180 : 1, 0.1, Easings.NONE);
        if (canExpand()) {
            float arrowSize = 8;
            RenderUtil.drawPlus(x + width - (arrowSize + 1), y + height / 2f, arrowSize, arrow.getValue(), -1);
        }
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
            for (ComponentEdit child : children) {
                cHeight = child.getHeight();
                if (child instanceof PropertyComponentEdit) {
                    PropertyComponentEdit propertyComponent = (PropertyComponentEdit) child;
                    if (!propertyComponent.getProperty().checkDependency())
                        continue;
                }
                if (child instanceof ExpandComponentEdit) {
                    ExpandComponentEdit expandableComponent = (ExpandComponentEdit) child;
                    if (expandableComponent.expanded)
                        cHeight = expandableComponent.getHeightWithExpand();
                }
                height += cHeight;
            }
        }
        return height;
    }
}

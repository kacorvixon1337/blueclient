package pl.kacorvixon.blue.ui.kot;

import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.module.impl.render.Hud;

import net.minecraft.client.gui.ScaledResolution;
import pl.kacorvixon.blue.util.render.RenderUtil;

import java.awt.*;
import java.util.List;

//MADE BY KTNTKOT

public class CategoryPanel extends Panel {
    private final List<Module> modules;

    public CategoryPanel(Category category, int x, int y) {
        super(null, category.name(), x, y, 120, 15);
        int moduleY = 15;
        this.modules = Blue.getInstance().moduleAdministration.getModulesByCategory(category);
        for (Module module : modules) {
            this.children.add(new ModuleComponent(this, module, 1, moduleY, 118, 15));
            moduleY += 15;
        }
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        int headerHeight = height;
        int heightWithExpand = getHeightWithExpand();
        headerHeight = (expanded ? heightWithExpand + 1 : height);
        RenderUtil.glDrawRoundedRect(x, y, width, headerHeight, RenderUtil.RoundingMode.FULL, 3, 2.2f, new Color(21, 23, 23).getRGB());
        Hud.getFontRenderer().drawStringWithShadow(name.toUpperCase(), x + width / 2.0F - Hud.getFontRenderer().getWidth(name.toUpperCase()) / 2.0F - 1, y + 15 / 2.0f - 4, -1);
        if (expanded) {
            int moduleY = height;
            for (pl.kacorvixon.blue.ui.kot.Component child : children) {
                child.y = (moduleY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                int cHeight = child.getHeight();
                if (child instanceof ExpandComponent) {
                    ExpandComponent expandableComponent = (ExpandComponent) child;
                    if (expandableComponent.expanded) cHeight = expandableComponent.getHeightWithExpand();
                }
                moduleY += cHeight;
            }
        }
    }

    @Override
    public boolean canExpand() {
        return !modules.isEmpty();
    }

    @Override
    public int getHeightWithExpand() {
        int height = getHeight();

        if (expanded) {
            for (Component child : children) {
                int cHeight = child.getHeight();
                if (child instanceof ExpandComponent) {
                    ExpandComponent expandableComponent = (ExpandComponent) child;
                    if (expandableComponent.expanded) cHeight = expandableComponent.getHeightWithExpand();
                }
                height += cHeight;
            }
        }

        return height;
    }
}

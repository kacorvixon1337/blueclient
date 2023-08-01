package pl.kacorvixon.blue.ui.kotedit;

import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.font.FontManager;
import pl.kacorvixon.blue.module.Category;

import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.util.render.RenderUtil;
import pl.kacorvixon.blue.util.render.animation.Animation;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.List;


public class CategoryPanelEdit extends PanelEdit {

    private final Animation headerAnim = new Animation();
    private final List<Module> modules;

    public String icon;

    public CategoryPanelEdit(Category category, int x, int y) {
        super(null, category.name(), x, y, 120, 15);
        icon = category.icon;
        int moduleY = 15;
        this.modules = Blue.getInstance().moduleAdministration.getModulesByCategory(category);
        for (Module module : modules) {
            this.children.add(new ModuleComponentEdit(this, module, 1, moduleY, 130, 15));
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
        RenderUtil.drawRect(x, y - 3, 130, headerHeight + 3, new Color(15, 15, 15).getRGB(), true, 1);
        RenderUtil.drawRect(x, y - 3, 130,headerHeight, new Color(15, 15, 15).getRGB(), true, 1);
        FontManager.getFontBold().drawStringWithShadow(name, x + 3, y + 7 / 2.0f - 4, -1);
        FontManager.getIconFont().drawStringWithShadow(icon, x + width - FontManager.getIconFont().getWidth(icon) + 7, y + 10 / 2.0f - 3, -1);
        FontManager.getIconFont2().drawStringWithShadow(icon, x + width - FontManager.getIconFont2().getWidth(icon) + 7, y + 10 / 2.0f - 3, -1);
        if (expanded) {
            int moduleY = height;
            for (pl.kacorvixon.blue.ui.kotedit.ComponentEdit child : children) {
                child.y = (moduleY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                int cHeight = child.getHeight();
                if (child instanceof ExpandComponentEdit) {
                    ExpandComponentEdit expandableComponent = (ExpandComponentEdit) child;
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
            for (ComponentEdit child : children) {
                int cHeight = child.getHeight();
                if (child instanceof ExpandComponentEdit) {
                    ExpandComponentEdit expandableComponent = (ExpandComponentEdit) child;
                    if (expandableComponent.expanded) cHeight = expandableComponent.getHeightWithExpand();
                }
                height += cHeight;
            }
        }

        return height;
    }
}


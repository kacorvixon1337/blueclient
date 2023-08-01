package pl.kacorvixon.blue.ui.kotedit;


import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;


public class ComponentEdit {
    public final List<pl.kacorvixon.blue.ui.kotedit.ComponentEdit> children = new ArrayList<>();
    public final pl.kacorvixon.blue.ui.kotedit.ComponentEdit parent;
    public final String name;
    public int x;
    public int y;
    public int width;
    public int height;

    public ComponentEdit(pl.kacorvixon.blue.ui.kotedit.ComponentEdit parent, String name, int x, int y, int width, int height) {
        this.parent = parent;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public pl.kacorvixon.blue.ui.kotedit.ComponentEdit getParent() {
        return parent;
    }

    public void drawComponent(final ScaledResolution scaledResolution, int mouseX, int mouseY) {
        for (pl.kacorvixon.blue.ui.kotedit.ComponentEdit child : children) {
            child.drawComponent(scaledResolution, mouseX, mouseY);
        }
    }

    public void onMouseClick(int mouseX, int mouseY, int button) {
        for (pl.kacorvixon.blue.ui.kotedit.ComponentEdit child : children) {
            child.onMouseClick(mouseX, mouseY, button);
        }
    }

    public void onMouseRelease(int button) {
        for (pl.kacorvixon.blue.ui.kotedit.ComponentEdit child : children) {
            child.onMouseRelease(button);
        }
    }

    public void onKeyPress(int keyCode) {
        for (pl.kacorvixon.blue.ui.kotedit.ComponentEdit child : children) {
            child.onKeyPress(keyCode);
        }
    }

    public int getX() {
        pl.kacorvixon.blue.ui.kotedit.ComponentEdit familyMember = parent;
        int familyTreeX = x;

        while (familyMember != null) {
            familyTreeX += familyMember.x;
            familyMember = familyMember.parent;
        }

        return familyTreeX;
    }

    protected boolean isHovered(int mouseX, int mouseY) {
        int x, y;
        return mouseX >= (x = getX()) && mouseY >= (y = getY()) && mouseX < x + width && mouseY < y + height;
    }

    public int getY() {
        pl.kacorvixon.blue.ui.kotedit.ComponentEdit familyMember = parent;
        int familyTreeY = y;

        while (familyMember != null) {
            familyTreeY += familyMember.y;
            familyMember = familyMember.parent;
        }

        return familyTreeY;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}

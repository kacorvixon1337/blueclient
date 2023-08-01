package pl.kacorvixon.blue.ui.kotedit;

//MADE BY KTNTKOT


import net.minecraft.client.gui.ScaledResolution;


public abstract class PanelEdit extends ExpandComponentEdit {

    public boolean dragging;

    private int prevX;
    private int prevY;

    public PanelEdit(ComponentEdit parent, String name, int x, int y, int width, int height) {
        super(parent, name, x, y, width, height);

        prevX = x;
        prevY = y;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (dragging) {
            x = (mouseX - prevX);
            y = (mouseY - prevY);
        }
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        if (button == 0) {
            dragging = true;
            prevX = mouseX - getX();
            prevY = mouseY - getY();
        }
    }

    @Override
    public void onMouseRelease(int button) {
        super.onMouseRelease(button);

        dragging = false;
    }

}


package pl.kacorvixon.blue.ui.kotedit;

import pl.kacorvixon.blue.module.impl.render.Hud;
import pl.kacorvixon.blue.property.Property;
import pl.kacorvixon.blue.property.impl.ColorProperty;

import pl.kacorvixon.blue.util.render.ColorUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import pl.kacorvixon.blue.util.render.RenderUtil;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

//slosa

public class ColorComponentEdit extends ComponentEdit implements PropertyComponentEdit {

    private final ColorProperty colorProperty;
    private boolean nigger;
    private Color color;
    private Color sus;
    private int loca,locay;
    private int locay1;
    private Panel panel;


    public ColorComponentEdit(ComponentEdit parent, ColorProperty colorProperty, int x, int y, int width, int height) {
        super(parent, colorProperty.name, x, y, width, height);
        this.colorProperty = colorProperty;
        loca = - 100;
        locay = - 1000;
        locay1 = -500;
        sus = colorProperty.value;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        Hud.getFontRenderer().drawStringWithShadow(name, getX() - 13, getY() + 1, 0xFFFFFF);

        if (Mouse.isButtonDown(0) && mouseWithinBounds(mouseX, mouseY, getX() - 13 + width + 5, getY() + Hud.getFontRenderer().getHeight(name) + 2, 20, 56)) {
            final Point p = MouseInfo.getPointerInfo().getLocation();
            Robot robot = null;
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
            sus = robot.getPixelColor(p.x, p.y);
        }
        if (Mouse.isButtonDown(0) && mouseWithinBounds(mouseX, mouseY, getX() - 13, getY() + Hud.getFontRenderer().getHeight(name) + 2, width, 56)) {
            final Point p = MouseInfo.getPointerInfo().getLocation();
            Robot robot = null;
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
            color = robot.getPixelColor(p.x, p.y);
            colorProperty.setValue(color.getRed() + ":" + color.getGreen() + ":" + color.getBlue());

        }

        RenderUtil.drawColorPickerRect(getX() - 13, getY() + Hud.getFontRenderer().getHeight(name) + 2, width, 56, new int[]{Color.white.getRGB(), Color.black.getRGB(), Color.black.getRGB(), sus.getRGB()});
        drawHueSlider(getX() - 13 + width + 5, getY() + Hud.getFontRenderer().getHeight(name) + 2, 20, 56);
        if (Mouse.isButtonDown(0) && mouseWithinBounds(mouseX, mouseY, getX() - 13, getY() + Hud.getFontRenderer().getHeight(name) + 2, width, 56)) {
            locay = mouseY - getY();
            loca = mouseX - getX();
        }
        RenderUtil.drawRect( getX() + loca - 4.5, getY() + locay - 4.5, 9, 9, -1, false, 0.7f);
        if (Mouse.isButtonDown(0) && mouseWithinBounds(mouseX, mouseY, getX() - 13 + width + 5, getY() + Hud.getFontRenderer().getHeight(name) + 2, 20, 56)) {
            locay1 = mouseY - getY();
        }
        RenderUtil.drawRect(getX() - 13 + width + 5, locay1 + getY() - 2.8, 20, 1.5, -1, true, 0.5f);

    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {

    }


    @Override
    public Property<?> getProperty() {
        return colorProperty;
    }

    private boolean mouseWithinBounds(final int mouseX, final int mouseY, final double x, final double y, final double width, final double height) {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));
    }

    private static void drawHueSlider(final double x,
                                      final double y,
                                      final double width,
                                      final double height) {
        // Enable blending
        final boolean restore = RenderUtil.glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Translate matrix to top-left of rect
        glTranslated(x, y, 0);
        // Enable colour blending
        glShadeModel(GL_SMOOTH);

        final int[] colours = {
                0xFFFF0000, // red (255, 0, 0)
                0xFFFFFF00, // yellow (255, 255, 0)
                0xFF00FF00, // green (0, 255, 0)
                0xFF00FFFF, // aqua (0, 255, 255)
                0xFF0000FF, // blue (0, 0, 255)
                0xFFFF00FF, // purple (255, 0, 255)
                0xFFFF0000, // red (255, 0, 0)
        };

        final double segment = height / colours.length;

        // Begin rect
        glBegin(GL_QUADS);
        {
            for (int i = 0; i < colours.length; i++) {
                final int colour = colours[i];

                final int top = i != 0 ? ColorUtil.fadeBetween(colours[i - 1], colour, 0.5F) : colour;
                final int bottom = i + 1 < colours.length ? ColorUtil.fadeBetween(colour, colours[i + 1], 0.5F) : colour;

                final double start = segment * i;

                RenderUtil.glColor(top);
                glVertex2d(0, start);

                RenderUtil.glColor(bottom);
                glVertex2d(0, start + segment);

                glVertex2d(width, start + segment);

                RenderUtil.glColor(top);
                glVertex2d(width, start);
            }
        }
        // Draw the rect
        glEnd();

        // Disable colour blending
        glShadeModel(GL_FLAT);
        // Translate matrix back (instead of creating a new matrix with glPush/glPop)
        glTranslated(-x, -y, 0);
        // Disable blending
        RenderUtil.glRestoreBlend(restore);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

}


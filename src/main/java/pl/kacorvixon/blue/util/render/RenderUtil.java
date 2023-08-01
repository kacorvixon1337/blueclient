package pl.kacorvixon.blue.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import javax.vecmath.Vector3d;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

import static net.minecraft.client.renderer.GlStateManager.disableBlend;
import static net.minecraft.client.renderer.GlStateManager.enableTexture2D;

public class RenderUtil {
    private static final Frustum frustrum = new Frustum();

    private static IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private static FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);

    public static ScaledResolution getResolution() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }


    public static void drawGradientRect(final double x, final double y, final double width, final double height, final int startColor, final int endColor, final boolean reversed, final boolean filled, final float lineWidth) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glDisable(GL_TEXTURE_2D);
        glTranslated(x, y, 0);
        glShadeModel(GL_SMOOTH);
        if (!filled) {
            glLineWidth(lineWidth);
        }
        glBegin(filled ? GL_QUADS : GL_LINE_LOOP);
        {
            glColor(reversed ? endColor : startColor);
            glVertex2d(0, 0);
            glColor(startColor);
            glVertex2d(0, height);
            glColor(reversed ? startColor : endColor);
            glVertex2d(width, height);
            glColor(endColor);
            glVertex2d(width, 0);
        }
        glEnd();
        glShadeModel(GL_FLAT);
        glTranslated(-x, -y, 0);
        glEnable(GL_TEXTURE_2D);
        GlStateManager.enableAlpha();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
    public static void glColor(final int color) {
        final float red = (float) (color >> 16 & 255) / 255.0F;
        final float green = (float) (color >> 8 & 255) / 255.0F;
        final float blue = (float) (color & 255) / 255.0F;
        final float alpha = (float) (color >> 24 & 255) / 255.0F;
        glColor4f(red, green, blue, alpha);
    }
    public static void drawRect(final double x, final double y, final double width, final double height, final int color, final boolean filled, final float lineWidth) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glDisable(GL_TEXTURE_2D);
        glTranslated(x, y, 0);
        glShadeModel(GL_SMOOTH);
        if (!filled) {
            glLineWidth(lineWidth);
        }
        glBegin(!filled ? GL_LINE_LOOP : GL_QUADS);
        {
            glColor(color);
            glVertex2d(0, 0);
            glVertex2d(0, height);
            glVertex2d(width, height);
            glVertex2d(width, 0);
        }
        glEnd();
        glShadeModel(GL_FLAT);
        glTranslated(-x, -y, 0);
        glEnable(GL_TEXTURE_2D);
        GlStateManager.enableAlpha();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }
    public static float interpolate(final float old, final float now, final float partialTicks) {
        return old + (now - old) * partialTicks;
    }
    public static Vector3d project(double x, double y, double z) {
        FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(2982, modelview);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / getResolution().getScaleFactor(),
                    (Display.getHeight() - vector.get(1)) / getResolution().getScaleFactor(),
                    vector.get(2));
        }
        return null;
    }
    public static void glDrawRoundedRect(final double x, final double y, final double width, final double height, final RoundingMode roundingMode, final float rounding, final float scaleFactor, final int colour) {
        boolean bLeft = false;
        boolean tLeft = false;
        boolean bRight = false;
        boolean tRight = false;

        switch (roundingMode) {
            case TOP:
                tLeft = true;
                tRight = true;
                break;
            case BOTTOM:
                bLeft = true;
                bRight = true;
                break;
            case FULL:
                tLeft = true;
                tRight = true;
                bLeft = true;
                bRight = true;
                break;
            case LEFT:
                bLeft = true;
                tLeft = true;
                break;
            case RIGHT:
                bRight = true;
                tRight = true;
                break;
            case TOP_LEFT:
                tLeft = true;
                break;
            case TOP_RIGHT:
                tRight = true;
                break;
            case BOTTOM_LEFT:
                bLeft = true;
                break;
            case BOTTOM_RIGHT:
                bRight = true;
                break;
        }

        final float alpha = (colour >> 24 & 0xFF) / 255.f;

        final boolean restore = glEnableBlend();

        glColor(colour);

        glTranslated(x, y, 0);
        glDisable(GL_TEXTURE_2D);

        glBegin(GL_POLYGON);
        {
            if (tLeft) {
                glVertex2d(rounding, rounding);
                glVertex2d(0, rounding);
            } else {
                glVertex2d(0, 0);
            }

            if (bLeft) {
                glVertex2d(0, height - rounding);
                glVertex2d(rounding, height - rounding);
                glVertex2d(rounding, height);
            } else {
                glVertex2d(0, height);
            }

            if (bRight) {
                glVertex2d(width - rounding, height);
                glVertex2d(width - rounding, height - rounding);
                glVertex2d(width, height - rounding);
            } else {
                glVertex2d(width, height);
            }

            if (tRight) {
                glVertex2d(width, rounding);
                glVertex2d(width - rounding, rounding);
                glVertex2d(width - rounding, 0);
            } else {
                glVertex2d(width, 0);
            }

            if (tLeft) {
                glVertex2d(rounding, 0);
            }
        }
        glEnd();

        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);

        glPointSize(rounding * 2.f * glGetFloat(GL_MODELVIEW_MATRIX) * scaleFactor);

        glBegin(GL_POINTS);
        {
            if (tLeft) {
                glVertex2d(rounding, rounding);
            }

            if (tRight) {
                glVertex2d(width - rounding, rounding);
            }

            if (bLeft) {
                glVertex2d(rounding, height - rounding);
            }

            if (bRight) {
                glVertex2d(width - rounding, height - rounding);
            }
        }
        glEnd();

        glDisable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_DONT_CARE);
        glRestoreBlend(restore);
        glTranslated(-x, -y, 0);
        glEnable(GL_TEXTURE_2D);
    }
    public static boolean glEnableBlend() {
        final boolean wasEnabled = glIsEnabled(GL_BLEND);

        if (!wasEnabled) {
            glEnable(GL_BLEND);
            glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        }

        return wasEnabled;
    }

    public static void glRestoreBlend(final boolean wasEnabled) {
        if (!wasEnabled) {
            glDisable(GL_BLEND);
        }
    }
    public static void startScale(float x, float y, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(-x, -y, 0);
    }
    public static void endScale() {
        GlStateManager.popMatrix();
    }
    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != Minecraft.getMinecraft().displayWidth || framebuffer.framebufferHeight != Minecraft.getMinecraft().displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
        }
        return framebuffer;
    }
    public static void drawColorPickerRect(final double x, final double y, final double width, final double height, final int[] color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glDisable(GL_TEXTURE_2D);
        glTranslated(x, y, 0);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_QUADS);
        {
            glColor(color[0]);
            glVertex2d(0, 0);
            glColor(color[1]);
            glVertex2d(0, height);
            glColor(color[2]);
            glVertex2d(width, height);
            glColor(color[3]);
            glVertex2d(width, 0);
        }
        glEnd();
        glShadeModel(GL_FLAT);
        glTranslated(-x, -y, 0);
        glEnable(GL_TEXTURE_2D);
        GlStateManager.enableAlpha();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
    public static void drawPlus(final double x, final double y, final double size, final double rotation, final int colour) {
        glDisable(GL_TEXTURE_2D);
        final boolean restore = glEnableBlend();
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth(1.f);
        glPushMatrix();
        glTranslated(x, y, 0);
        glRotated(rotation, 0, 1, 1);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glColor(colour);
        glBegin(GL_LINES);
        {
            glVertex2d(-(size / 2.0), 0);
            glVertex2d(size / 2.0, 0);
            glVertex2d(0, -(size / 2.0));
            glVertex2d(0, size / 2.0);
        }
        glEnd();
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glRestoreBlend(restore);
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
    }
    public static void bindTexture(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }
    public enum RoundingMode {
        TOP_LEFT,
        BOTTOM_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,

        LEFT,
        RIGHT,

        TOP,
        BOTTOM,

        FULL
    }
}

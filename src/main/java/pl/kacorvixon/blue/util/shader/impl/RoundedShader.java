package pl.kacorvixon.blue.util.shader.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import pl.kacorvixon.blue.util.shader.Shader;

import java.awt.*;

import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform4f;

public class RoundedShader {
    private static final String SHADER = "#version 120\n" +
            "\n" +
            "uniform float width;\n" +
            "uniform float height;\n" +
            "uniform vec4 color;\n" +
            "uniform float radius;\n" +
            "\n" +
            "float dstfn(vec2 p, vec2 b, float r) {\n" +
            "    return length(max(abs(p) - b, 0.0)) - r;\n" +
            "}\n" +
            "\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 size = vec2(width, height);\n" +
            "    vec2 pixel = gl_TexCoord[0].st * size;\n" +
            "    vec2 centre = 0.5 * size;\n" +
            "    float sa = smoothstep(0.0, 1, dstfn(centre - pixel, centre - radius - 1, radius));\n" +
            "    vec4 c = mix(vec4(color.rgb, 1), vec4(color.rgb, 0), sa);\n" +
            "    gl_FragColor = vec4(c.rgb, color.a * c.a);\n" +
            "}";

    private static final String SHADER_OUTLINE = "#version 120\n" +
            "\n" +
            "uniform float width;\n" +
            "uniform float height;\n" +
            "uniform float radius;\n" +
            "uniform float thickness;\n" +
            "uniform vec4 color;\n" +
            "\n" +
            "float dstfn(vec2 p, vec2 b, float r) {\n" +
            "    return length(max(abs(p),b) - b) - r;\n" +
            "}\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 size = vec2(width, height);\n" +
            "    vec2 pixel = gl_TexCoord[0].st * size;\n" +
            "    vec2 centre = 0.5 * size;\n" +
            "    float d = dstfn(centre - pixel, centre - radius - thickness, radius);\n" +
            "    float sa = smoothstep(0., 2., abs(d) / thickness);\n" +
            "    vec4 c = mix(vec4(color.rgb, 1), vec4(color.rgb, 0), sa);\n" +
            "    gl_FragColor = vec4(c.rgb, color.a * c.a);\n" +
            "}";

    public static final Shader round = new Shader(SHADER);

    public static final Shader roundOutline = new Shader(SHADER_OUTLINE);

    public static void drawRound(final float x, final float y, final float width, final float height, final float radius, final Color color) {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        round.startProgram();
        glUniform1f(round.getUniformLoc("width"), width * sr.getScaleFactor());
        glUniform1f(round.getUniformLoc("height"), height * sr.getScaleFactor());
        glUniform1f(round.getUniformLoc("radius"), radius * sr.getScaleFactor());
        glUniform4f(round.getUniformLoc("color"), color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        BlurUtil.drawCanvas(x, y, width, height);
        round.stopProgram();
        GlStateManager.disableBlend();
    }

    public static void drawRoundOutline(final float x, final float y, final float width, final float height, final float radius, final float thickness, final Color color) {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        roundOutline.startProgram();
        glUniform1f(roundOutline.getUniformLoc("width"), width * sr.getScaleFactor());
        glUniform1f(roundOutline.getUniformLoc("height"), height * sr.getScaleFactor());
        glUniform1f(roundOutline.getUniformLoc("thickness"), thickness * sr.getScaleFactor());
        glUniform1f(roundOutline.getUniformLoc("radius"), radius * sr.getScaleFactor());
        glUniform4f(roundOutline.getUniformLoc("color"), color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        BlurUtil.drawCanvas(x, y, width, height);
        roundOutline.stopProgram();
        GlStateManager.disableBlend();
    }
}

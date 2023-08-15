package pl.kacorvixon.blue.module.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.Display;
import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.property.impl.NumberProperty;
import pl.kacorvixon.blue.util.shader.impl.BlurUtil;

import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;

public class Shader extends Module {
    public final NumberProperty radius = new NumberProperty("Blur Radius", 20, 1, 50, 1);
    public final BooleanProperty glow = new BooleanProperty("Shadow", false);
    public final NumberProperty glowradius = new NumberProperty("Shadow Radius", 4, 1, 50, 1);
    public final NumberProperty glowskipping = new NumberProperty("Shadow Skipping", 110, 1, 600, 1);

    public Shader() {
        super("Shader", "Shader", Category.Render, 0);
        addProperties(radius, glow, glowradius, glowskipping);
    }

    private Framebuffer frameBuffer = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
    private static final String based = "#version 120\n" +
            "\n" +
            "uniform sampler2D texture;\n" +
            "uniform vec2 texelSize;\n" +
            "uniform int skipping;\n" +
            "uniform int radius;\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 uv = gl_TexCoord[0].st;\n" +
            "    vec4 color = texture2D(texture, uv);\n" +
            "    if (color.a != 0.0) return;\n" +
            "\n" +
            "    float alpha = 0.0;\n" +
            "    for (int x = -radius; x <= radius; ++x) {\n" +
            "        for (int y = -radius; y <= radius; ++y) {\n" +
            "            vec4 next_color = texture2D(texture, uv + vec2(x, y) * texelSize);\n" +
            "            if (next_color.a == 0.0) continue;\n" +
            "\n" +
            "\t\t\tcolor = next_color;\n" +
            "\n" +
            "            alpha += max(0.0, radius - sqrt(x * x + y * y));\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    gl_FragColor = vec4(color.rgb, alpha * color.a / skipping);\n" +
            "}";
    public static final pl.kacorvixon.blue.util.shader.Shader shaderProgram = new pl.kacorvixon.blue.util.shader.Shader(based);

    @Override
    public void onRender2D(net.weavemc.loader.api.event.RenderGameOverlayEvent event) {
        if (Display.isActive() && glow.value) {
            onResize();
            final ScaledResolution scaledResolution = new ScaledResolution(mc);
            frameBuffer.bindFramebuffer(true);
            mc.getFramebuffer().bindFramebuffer(true);
            shaderProgram.startProgram();
            glUniform1i(shaderProgram.getUniformLoc("texture"), 0);
            glUniform2f(shaderProgram.getUniformLoc("texelSize"), 1.0F / scaledResolution.getScaledWidth(), 1.0F / scaledResolution.getScaledHeight());
            glUniform1i(shaderProgram.getUniformLoc("radius"), glowradius.value.intValue());
            glUniform1i(shaderProgram.getUniformLoc("skipping"), glowskipping.value.intValue());
            GlStateManager.alphaFunc(GL_GREATER, 0.0F);
            GlStateManager.enableBlend();
            frameBuffer.bindFramebufferTexture();
            BlurUtil.drawCanvas();
            shaderProgram.stopProgram();
            GlStateManager.disableBlend();
        }
    }

    private void onResize() {
        try {
            frameBuffer.framebufferClear();
            if (hasResized()) {
                frameBuffer.deleteFramebuffer();
                frameBuffer = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasResized() {
        return Minecraft.getMinecraft().displayWidth != frameBuffer.framebufferWidth || Minecraft.getMinecraft().displayHeight != frameBuffer.framebufferHeight;
    }
}

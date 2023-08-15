package pl.kacorvixon.blue.module.impl.render;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.font.FontManager;
import pl.kacorvixon.blue.font.RenderInterface;
import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.module.ModuleAdministration;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.property.impl.ColorProperty;
import pl.kacorvixon.blue.property.impl.EnumProperty;
import pl.kacorvixon.blue.property.impl.NumberProperty;
import pl.kacorvixon.blue.util.render.ColorUtil;
import pl.kacorvixon.blue.util.render.RenderUtil;
import pl.kacorvixon.blue.util.render.animation.Easings;
import pl.kacorvixon.blue.util.render.animation.V2;
import pl.kacorvixon.blue.util.shader.StencilUtil;
import pl.kacorvixon.blue.util.shader.impl.BlurUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Hud extends Module {

    public Hud() {
        super("HUD", "HUD", Category.Render, Keyboard.KEY_O);
        addProperties(fpsBooleanProperty, potionBooleanProperty, bpsBooleanProperty, clientInfoProperty, hueInterpolation, font, watermarkEnumProperty, colorValue, colorValue2, colorEnumValue, FadeColorEnumValue, backgoundBooleanProperty, backgroundOpacity, outlineBoolean, outline, animationEnumProperty, safe);
    }
    public static final ColorProperty colorValue = new ColorProperty("Color", Color.WHITE);
    public static final EnumProperty<color> colorEnumValue = new EnumProperty<>("Color", color.Fade);
    private static final EnumProperty<FadeColor> FadeColorEnumValue = new EnumProperty<>("Fade Color", FadeColor.TwoColor, () -> colorEnumValue.value == color.Fade);
    public static final ColorProperty colorValue2 = new ColorProperty("Second Color", () -> FadeColorEnumValue.value == FadeColor.TwoColor, Color.CYAN);
    public static final BooleanProperty hueInterpolation = new BooleanProperty("Hue Interpolation", true);
    public static final BooleanProperty font = new BooleanProperty("Font", true);
    private final BooleanProperty watermarkBooleanProperty = new BooleanProperty("Watermark", true);
    private final EnumProperty<Watermark> watermarkEnumProperty = new EnumProperty<>("Watermark", Watermark.Rect);
    private final BooleanProperty backgoundBooleanProperty = new BooleanProperty("Background", true);
    private final BooleanProperty outlineBoolean = new BooleanProperty("Outline", true);
    private final EnumProperty<Outline> outline = new EnumProperty("Outline Mode", Outline.Wrapped, () -> outlineBoolean.value);
    private final NumberProperty backgroundOpacity = new NumberProperty("Opacity", () -> backgoundBooleanProperty.value, 120, 1, 255, 1);
    private final BooleanProperty fpsBooleanProperty = new BooleanProperty("FPS", true);
    private final BooleanProperty potionBooleanProperty = new BooleanProperty("Potions", true);
    private final BooleanProperty bpsBooleanProperty = new BooleanProperty("BPS", true);
    private final BooleanProperty clientInfoProperty = new BooleanProperty("Client Info", true);
    private final EnumProperty<Animation> animationEnumProperty = new EnumProperty<>("Animation", Animation.Normal);

    private final BooleanProperty safe = new BooleanProperty("Safe Anim", false);

    public int colorr = -1;
    public Module lastMod;
    double lastDistance;

    pl.kacorvixon.blue.util.render.animation.Animation animation = new pl.kacorvixon.blue.util.render.animation.Animation();

    @Override
    public void onRender2D(net.weavemc.loader.api.event.RenderGameOverlayEvent e) {
        int lastColor = -1;
        final ScaledResolution sr = new ScaledResolution(mc);
        animation.update();
        animation.animate(mc.currentScreen instanceof GuiChat ? 25 : 11, 0.1, false);
        int potionY = (int) (sr.getScaledHeight() - 11);
        int secondY = (int) (sr.getScaledHeight() - 11);
        if (fpsBooleanProperty.value) {
            final String fpsText = "FPS: " + Minecraft.getDebugFPS();
            getFontRenderer().drawStringWithShadow(fpsText, 3, potionY, -1);
            secondY -= 11;
        }

        if (clientInfoProperty.value) {
            getFontRenderer().drawStringWithShadow(Blue.getInstance().version + " - RELEASE - " + Blue.getInstance().build, sr.getScaledWidth() - 3 - getFontRenderer().getWidth("1.2 - DEVELOPMENT - 62122"), potionY, -1);
            potionY -= 11;
        }

        if (potionBooleanProperty.value) {
            if (!mc.thePlayer.getActivePotionEffects().isEmpty()) {
                for (PotionEffect o : mc.thePlayer.getActivePotionEffects()) {
                    Potion potion = Potion.potionTypes[o.getPotionID()];
                    String effectName = I18n.format(o.getEffectName(), new Object[0]) + " " + (o.getAmplifier() + 1) + EnumChatFormatting.WHITE + " " + Potion.getDurationString(o);
                    getFontRenderer().drawStringWithShadow(effectName, sr.getScaledWidth() - 3 - getFontRenderer().getWidth(effectName), potionY, potion.getLiquidColor());
                    potionY -= 11;
                }
            }
        }
        if (bpsBooleanProperty.value) {
            getFontRenderer().drawStringWithShadow("BPS: " + String.format("%.2f", lastDistance * 20.0 * mc.timer.timerSpeed), 2.0f, secondY, -1);
            secondY -= 11;
        }
        switch (colorEnumValue.value) {
            case Static:
                colorr = colorValue.value.getRGB();
                break;
            case Rainbow:
                colorr = ColorUtil.createRainbowFromOffset(2400, 11 * 15);
                break;
            case Astolfo:
                colorr = ColorUtil.astolfoColors(11, 11 + 900);
                break;
            case Fade:
                switch (FadeColorEnumValue.value) {
                    case TwoColor:
                        colorr = ColorUtil.fadeBetween(colorValue.value.getRGB(), colorValue2.value.getRGB(), 11 * 17L);
                        break;
                    case OneColor:
                        colorr = ColorUtil.fadeBetween(colorValue.value.getRGB(), ColorUtil.darker(colorValue.value.getRGB(), 0.38f), 11 * 17L);
                        break;
                }
                break;
        }
        if(watermarkBooleanProperty.getValue()) {
            switch (watermarkEnumProperty.value) {
                case Normal:
                    if (font.value) {
                        FontManager.bigBold.drawStringWithShadow("B" + EnumChatFormatting.WHITE + "lue", 3, 3, colorr);
                    } else getFontRenderer().drawStringWithShadow("B" + EnumChatFormatting.WHITE + "lue", 3, 3, colorr);
                    break;
                case Rect:
                    if (ModuleAdministration.getInstance(Glow.class).enabled) {

                        GlStateManager.pushMatrix();
                        GlStateManager.translate(3, 3, 1);

                        String serverip = mc.isSingleplayer() ? "localhost" : !mc.getCurrentServerData().serverIP.contains(":") ? mc.getCurrentServerData().serverIP + ":25565" : mc.getCurrentServerData().serverIP;
                        String text = "Blue | " + mc.thePlayer.getName() + " | " + "blue.lol";
                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glEnable(GL11.GL_BLEND);
                        RenderUtil.renderShadow(4, 5, getFontRenderer().getWidth(text) + 8, 18, colorr, 7);
                        int width = (int) getFontRenderer().getWidth(text) + 4;
                        if (ModuleAdministration.getInstance(Shader.class).enabled) {
                            StencilUtil.INSTANCE.initStencilToWrite();
                            RenderUtil.drawRect(4, 5, width, 13, new Color(1, 1, 1, 120).getRGB(), true, 1);
                            StencilUtil.INSTANCE.readStencilBuffer(1);
                            BlurUtil.drawBlur2();
                            StencilUtil.INSTANCE.uninitStencilBuffer();
                        }
                        RenderUtil.drawRect(4, 5, width, 13, new Color(1, 1, 1, 120).getRGB(), true, 1);
                        RenderUtil.drawRect(4, 5, width, 1, colorr, true, 1);
                        getFontRenderer().drawStringWithShadow(text, 6.5f, 8.5f, colorr);
                        GL11.glDisable(GL11.GL_ALPHA_TEST);
                        GL11.glDisable(GL11.GL_BLEND);
                        GlStateManager.popMatrix();

                    } else {
                    String text = "Blue | " + mc.thePlayer.getName() + " | " + "blue.lol";
                    int width = (int) getFontRenderer().getWidth(text) + 3;
                    RenderUtil.drawRect(5, 5, width, 13, new Color(1, 1, 1, 120).getRGB(), true, 1);
                    RenderUtil.drawRect(5, 4, width, 1, colorr, true, 1);
                    getFontRenderer().drawStringWithShadow(text, 6, 8, -1);
                    Gui.drawRect(0, 0, 0, 0, 0);
                    }








                    break;
            }
        }
        final java.util.List<Module> modules = new ArrayList<>(Blue.getInstance().moduleAdministration.moduleList);
        modules.sort(Comparator.comparingDouble(m -> -getFontRenderer().getWidth(m.getDisplayName())));
        int count = 2;
        Module lastEnabledModule = null;
        for (final Module m : modules) {
            switch (colorEnumValue.value) {
                case Static:
                    colorr = colorValue.value.getRGB();
                    break;
                case Rainbow:
                    colorr = ColorUtil.createRainbowFromOffset(2400, count * 15);
                    break;
                case Astolfo:
                    colorr = ColorUtil.astolfoColors(count, m.name.length() + 900);
                    break;
                case Fade:
                    switch (FadeColorEnumValue.value) {
                        case TwoColor:
                            colorr = ColorUtil.fadeBetween(colorValue.value.getRGB(), colorValue2.value.getRGB(), count * 17L);
                            break;
                        case OneColor:
                            colorr = ColorUtil.fadeBetween(colorValue.value.getRGB(), ColorUtil.darker(colorValue.value.getRGB(), 0.38f), count * 17L);
                            break;
                    }
                    break;
            }
            if (lastEnabledModule == null) lastColor = colorr;
            m.animation.update();
            m.animation.animate(new V2(!m.enabled || (mc.gameSettings.showDebugInfo) ? sr.getScaledWidth() + 1 : sr.getScaledWidth() - getFontRenderer().getWidth(m.getDisplayName()) - 1 - (dothgi() ? 1 : 0), m.enabled && animationEnumProperty.value == Animation.Normal ? count : !m.enabled && animationEnumProperty.value == Animation.Exhi ? -20 : count), safe.value ? 0.20 : 0.1, Easings.NONE, safe.value);
            if (m.animation.getValue().getX() < sr.getScaledWidth() && !m.hidden) {
                if (backgoundBooleanProperty.value) {
                    Gui.drawRect((int) (m.animation.getValue().getX() - 1.0), (int) (m.animation.getValue().getY() - 2), sr.getScaledWidth(), (int) (m.animation.getValue().getY() + 9.0), new Color(1, 1, 1, backgroundOpacity.value.intValue()).getRGB());
                }
                if (outlineBoolean.value) {
                    switch (outline.value) {
                        case Right:
                            RenderUtil.drawGradientRect(sr.getScaledWidth() - 1, m.animation.getValue().getY() - 2, 1, 11.0, colorr, lastColor, true, true, 1f);
                            break;
                        case Left:
                            RenderUtil.drawGradientRect(m.animation.getValue().getX() - 2, m.animation.getValue().getY() - 2, 1, 11.0, colorr, lastColor, true, true, 1f);
                            break;
                        case Wrapped:
                            RenderUtil.drawGradientRect(sr.getScaledWidth() - 1, m.animation.getValue().getY() - 2, 1, 11.0, colorr, lastColor, true, true, 1f);
                            RenderUtil.drawGradientRect(m.animation.getValue().getX() - 2, m.animation.getValue().getY() - 2, 1, 11.0, colorr, lastColor, true, true, 1f);
                            if (lastEnabledModule != null) {
                                if (m.enabled)
                                    RenderUtil.drawRect(lastEnabledModule.animation.getValue().getX() - 2, m.animation.getValue().getY() - 2, m.animation.getValue().getX() - lastEnabledModule.animation.getValue().getX(), 1, colorr, true, 1f);
                                if (modules.indexOf(m) == modules.size() - 1) {
                                    RenderUtil.drawRect(m.animation.getValue().getX() - 2, m.animation.getValue().getY() + getFontRenderer().getHeight(m.name) - (font.value ? 0 : 2), getFontRenderer().getWidth(m.getDisplayName()) + 5, 1, colorr, true, 1f);
                                }
                            }
                            break;
                    }
                } else {
                    RenderUtil.drawGradientRect(sr.getScaledWidth(), m.animation.getValue().getY() - 2, 1, 11.0, colorr, lastColor, true, true, 1f);
                }
                getFontRenderer().drawStringWithShadow(m.getDisplayName(), (float) m.animation.getValue().getX(), (float) m.animation.getValue().getY(), colorr);
            }
            lastMod = modules.get(modules.size() - 1);
            if (m.enabled && !m.hidden) {
                count += 11;
                lastColor = colorr;
                lastEnabledModule = m;
            }
        }
    }

    public Color[] getClientColors() {
        return new Color[]{colorValue.getValue(), colorValue2.getValue()};
    }













    /*@Handler
    public void onGlow(GlowEvent event) {
        final List<Module> modules = new ArrayList<>(Saber.getInstance().moduleAdministration.moduleList);
        modules.sort(Comparator.comparingDouble(m -> -getFontRenderer().getWidth(m.getDisplayName())));
        final ScaledResolution sr = new ScaledResolution(mc);
        for (final Module m : modules) {
            switch (colorEnumValue.value) {
                case Static:
                    colorr = colorValue.value.getRGB();
                    break;
                case Rainbow:
                    colorr = ColorUtil.createRainbowFromOffset(2400, (int) (m.animation.getValue().getY() * 15));
                    break;
                case Astolfo:
                    colorr = ColorUtil.astolfoColors((int) (m.animation.getValue().getY()), m.name.length() + 900);
                    break;
                case Fade:
                    switch (FadeColorEnumValue.value) {
                        case TwoColor:
                            colorr = ColorUtil.fadeBetween(colorValue.value.getRGB(), colorValue2.value.getRGB(), (int) (m.animation.getValue().getY()) * 17L);
                            break;
                        case OneColor:
                            colorr = ColorUtil.fadeBetween(colorValue.value.getRGB(), ColorUtil.darker(colorValue.value.getRGB(), 0.38f), (int) (m.animation.getValue().getY()) * 17L);
                            break;
                    }
                    break;
            }
            /*if (m.animation.getValue().getX() < sr.getScaledWidth() && ModuleAdministration.getInstance(Shader.class).enabled) {
                if (outlineBoolean.value && outline.value == Outline.Wrapped || outline.value == Outline.Left) return;
                Gui.drawRect((int) (m.animation.getValue().getX() - 1.0), (int) (m.animation.getValue().getY() - 2), sr.getScaledWidth(), (int) (m.animation.getValue().getY() + 9.0), colorr);
            }
        }
    }*/

    public String getIP() {
        return mc.getCurrentServerData().serverIP;
    }

    @Override
    public void onEnable() {

    }

    public boolean dothgi() {
        return outlineBoolean.value && outline.value == Outline.Wrapped || outlineBoolean.value && outline.value == Outline.Right;
    }

    public static RenderInterface getFontRenderer() {
        return (font.value) ? FontManager.getFont() : (RenderInterface) FontManager.getMinecraftFontRenderer();
    }

    public enum color {
        Astolfo, Static, Rainbow, Fade
    }

    private enum FadeColor {
        OneColor, TwoColor
    }

    private enum Outline {
        Wrapped, Right, Left
    }

    private enum Watermark {
        Normal, Rect
    }

    private enum Animation {
        Normal, Exhi
    }
}

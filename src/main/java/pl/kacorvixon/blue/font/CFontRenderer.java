package pl.kacorvixon.blue.font;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class CFontRenderer extends CFont implements RenderInterface {
    protected CFont.CharData[] boldChars = new CFont.CharData[256];
    protected CFont.CharData[] italicChars = new CFont.CharData[256];
    protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];

    private final int[] colorCode = new int[32];
    private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        setupMinecraftColorcodes();
        setupBoldItalicIDs();
    }

    @Override
    public int drawStringWithShadow(final String text, final float x, final float y, final int color) {

        float shadowWidth = drawString(text, x + 0.5, y + 0.5, color, true);
        return (int) Math.max(shadowWidth,

                drawString(text, x, y, color, false));
    }

    public int drawString(String text, float x, float y, int color) {
        return drawString(text, x, y, color, false);
    }


    public float drawCenteredString(String text, float x, float y, int color) {
        return drawString(text, x - getWidth(text) / 2, y, color);
    }

    public int drawString(String text, double x, double y, int color, boolean shadow) {
        x -= 1.0D;
        if (text == null)
            return (int) 0.0F;
        if (color == 553648127) {
            color = 16777215;
        }
        if ((color & 0xFC000000) == 0) {
            color |= -16777216;
        }

        if (shadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }

        CFont.CharData[] currentData = this.charData;
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x *= 2.0D;
        y = (y - 3.0D) * 2.0D;
        if (render) {
            GL11.glPushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);

            GL11.glEnable(GL11.GL_BLEND);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
            int size = text.length();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(this.tex.getGlTextureId());
            GL11.glBindTexture(3553, this.tex.getGlTextureId());
            for (int i = 0; i < size; i++) {
                char character = text.charAt(i);
                if ((character == '\u00a7') && (i < size)) {
                    int colorIndex = 21;
                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (colorIndex < 16) {
                        bold = false;
                        italic = false;
                        randomCase = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.bindTexture(this.tex.getGlTextureId());

                        currentData = this.charData;
                        if ((colorIndex < 0) || (colorIndex > 15))
                            colorIndex = 15;
                        if (shadow)
                            colorIndex += 16;
                        int colorcode = this.colorCode[colorIndex];
                        GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
                    } else if (colorIndex == 16) {
                        randomCase = true;
                    } else if (colorIndex == 17) {
                        bold = true;
                        if (italic) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());

                            currentData = this.boldItalicChars;
                        } else {
                            GlStateManager.bindTexture(this.texBold.getGlTextureId());

                            currentData = this.boldChars;
                        }
                    } else if (colorIndex == 18) {
                        strikethrough = true;
                    } else if (colorIndex == 19) {
                        underline = true;
                    } else if (colorIndex == 20) {
                        italic = true;
                        if (bold) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());

                            currentData = this.boldItalicChars;
                        } else {
                            GlStateManager.bindTexture(this.texItalic.getGlTextureId());

                            currentData = this.italicChars;
                        }
                    } else if (colorIndex == 21) {
                        bold = false;
                        italic = false;
                        randomCase = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
                        GlStateManager.bindTexture(this.tex.getGlTextureId());

                        currentData = this.charData;
                    }
                    i++;
                } else if ((character < currentData.length) && (character >= 0)) {
                    GL11.glBegin(4);
                    drawChar(currentData, character, (float) x, (float) y);
                    GL11.glEnd();
                    if (strikethrough)
                        drawLine(x, y + currentData[character].height / 2, x + currentData[character].width - 8.0D, y + currentData[character].height / 2, 1.0F);
                    if (underline)
                        drawLine(x, y + currentData[character].height - 2.0D, x + currentData[character].width - 8.0D, y + currentData[character].height - 2.0D, 1.0F);
                    x += currentData[character].width - 8 + this.charOffset;
                }
            }
            GL11.glHint(3155, 4352);
            GL11.glPopMatrix();
        }
        Gui.drawRect(0, 0, 0, 0, 0);
        return (int) x * 2;
    }

    @Override
    public float getHeight(String text) {
        return getHeight();
    }
    @Override
    public float getMiddleOfBox(float boxHeight) {
        return boxHeight / 2.0f - (float)getHeight() / 2.0f;
    }

    public void drawTotalCenteredString(final String text, final double x, final double y, final int color) {
        drawString(text, (int) x - getWidth(text) / 2, (float) (y - getHeight() / 2), color);
    }

    public void drawTotalCenteredStringWithShadow(final String text, final double x, final double y, final int color) {
        drawStringWithShadow(text, (float) (x - getWidth(text) / 2), (float) (y - getHeight() / 2.0f), color);
    }

    public void drawString(final String text, final double x, final double y, final int color) {
        drawString(text, (int) x, (int) y, color);
    }

    public void drawCenteredString(final String text, final double x, final double y, final int color) {
        drawString(text, x - getWidth(text) / 2, y, color);
    }

    public void drawCenteredStringWithShadow(final String text, final double x, final double y, final int color) {
        drawStringWithShadow(text, (float) (x - getWidth(text) / 2), (float) y, color);
    }

    public float getWidth(String text) {
        if (text == null)
            return 0;
        int width = 0;
        CFont.CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();

        for (int i = 0; i < size; i++) {
            char character = text.charAt(i);
            if ((character == '\u00a7') && (i < size)) {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic)
                        currentData = this.boldItalicChars;
                    else
                        currentData = this.boldChars;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold)
                        currentData = this.boldItalicChars;
                    else
                        currentData = this.italicChars;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                i++;
            } else if ((character < currentData.length) && (character >= 0)) {
                width += currentData[character].width - 8 + this.charOffset;
            }
        }

        return width / 2;
    }


    public void setFont(Font font) {
        super.setFont(font);
        setupBoldItalicIDs();
    }

    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        setupBoldItalicIDs();
    }

    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = setupTexture(this.font.deriveFont(Font.BOLD), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = setupTexture(this.font.deriveFont(Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.italicChars);

    }

    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public java.util.List<String> wrapWords(String text, double width) {
        java.util.List finalWords = new ArrayList();
        if (getWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            char lastColorCode = 65535;

            for (String word : words) {
                for (int i = 0; i < word.toCharArray().length; i++) {
                    char c = word.toCharArray()[i];

                    if ((c == '\u00a7') && (i < word.toCharArray().length - 1)) {
                        lastColorCode = word.toCharArray()[(i + 1)];
                    }
                }
                if (getWidth(currentWord + word + " ") < width) {
                    currentWord = currentWord + word + " ";
                } else {
                    finalWords.add(currentWord);
                    currentWord = '\u00a7' + lastColorCode + word + " ";
                }
            }
            if (currentWord.length() > 0)
                if (getWidth(currentWord) < width) {
                    finalWords.add('\u00a7' + lastColorCode + currentWord + " ");
                    currentWord = "";
                } else {
                    for (String s : formatString(currentWord, width))
                        finalWords.add(s);
                }
        } else {
            finalWords.add(text);
        }

        return finalWords;
    }

    public java.util.List<String> formatString(String string, double width) {
        java.util.List finalWords = new ArrayList();
        String currentWord = "";
        char lastColorCode = 65535;
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if ((c == '\u00a7') && (i < chars.length - 1)) {
                lastColorCode = chars[(i + 1)];
            }

            if (getWidth(currentWord + c) < width) {
                currentWord = currentWord + c;
            } else {
                finalWords.add(currentWord);
                currentWord = '\u00a7' + lastColorCode + String.valueOf(c);
            }
        }

        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }

        return finalWords;
    }

    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; index++) {
            int noClue = (index >> 3 & 0x1) * 85;
            int red = (index >> 2 & 0x1) * 170 + noClue;
            int green = (index >> 1 & 0x1) * 170 + noClue;
            int blue = (index >> 0 & 0x1) * 170 + noClue;

            if (index == 6) {
                red += 85;
            }

            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            this.colorCode[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
        }
    }
}
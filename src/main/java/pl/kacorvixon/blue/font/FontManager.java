package pl.kacorvixon.blue.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FontManager {
    public static CFontRenderer font = null;
    public static CFontRenderer fontUI = null;
    public static CFontRenderer bigBold = null;
    public static CFontRenderer iconFont = null;
    public static CFontRenderer iconFont2 = null;

    public final static String
            COMBAT = "a",
            MOVEMENT = "b",
            MISC = "e",
            RENDER = "D";

    public void init() {
        font = new CFontRenderer(getFont2("https://mirror.racisz.in/.heliodor/font.ttf", 21), true, true);
        fontUI = new CFontRenderer(getFont2("https://mirror.racisz.in/.heliodor/bold.ttf", 26), true, true);
        iconFont = new CFontRenderer(getFont2("https://mirror.racisz.in/.heliodor/icon.ttf", 26), true, true);
        iconFont2 = new CFontRenderer(getFont2("https://mirror.racisz.in/.heliodor/icon2.ttf", 26), true, true);
        bigBold = new CFontRenderer(getFont2("https://mirror.racisz.in/.heliodor/bold.ttf", 30), true, true);
    }

    private Font getFont2(String urlr, int size) {
        Font font;
        try {
            URL url = null;
            url = new URL(urlr);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "marcinmichaltocwel");
            con.connect();
            final InputStream is = con.getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            font = new Font("default", 0, size);
        }
        return font;
    }

    public static FontRenderer getMinecraftFontRenderer() {
        return Minecraft.getMinecraft().fontRendererObj;
    }

    public static CFontRenderer getFont() {
        return font;
    }

    public static CFontRenderer getFontBold() {
        return fontUI;
    }

    public static CFontRenderer getIconFont() {
        return iconFont;
    }

    public static CFontRenderer getIconFont2() {
        return iconFont2;
    }
}
package pl.kacorvixon.blue.module;


import pl.kacorvixon.blue.font.FontManager;

public enum Category {
    Combat(FontManager.COMBAT),
    Misc(FontManager.MISC),
    Movement(FontManager.MOVEMENT),
    Render(FontManager.RENDER);
    public final String icon;
    Category(String icon) {
        this.icon = icon;
    }
}

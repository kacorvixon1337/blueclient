package pl.kacorvixon.blue.module.impl.render;


import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;

public class Animations extends Module {
    public Animations() {
        super("Animations","Animations", Category.Render,10);
        addProperties(f3,tab,hotbar,chat,titles);
    }
    public final BooleanProperty f3 = new BooleanProperty("Animate F3", true, null);
    public final BooleanProperty tab = new BooleanProperty("Animate Tab", true, null);
    public final BooleanProperty hotbar = new BooleanProperty("Animate HotBar", true, null);
    public final BooleanProperty chat = new BooleanProperty("Animate Chat", true, null);
    public final BooleanProperty titles = new BooleanProperty("Animate Titles", true, null);
}

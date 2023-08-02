package pl.kacorvixon.blue.module.impl.render;

import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.property.impl.ColorProperty;

import java.awt.*;

public class ChestESP extends Module {
    public BooleanProperty visible = new BooleanProperty("Visible Chams",true);
    public BooleanProperty oddcluded = new BooleanProperty("Oddcluded Chams",true);
    public ColorProperty visiblecolor = new ColorProperty("Visible Color", Color.RED);
    public ColorProperty oddcludedcolor = new ColorProperty("Oddcluded Color", Color.MAGENTA);

    public BooleanProperty flat = new BooleanProperty("Visible Flat", true);

    public ChestESP() {
        super("ChestESP","Chest ESP", Category.Render,0);
        addProperties(visible,oddcluded,visiblecolor,flat,oddcludedcolor);
    }

}

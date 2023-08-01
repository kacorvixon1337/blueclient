package pl.kacorvixon.blue.property.impl;

import pl.kacorvixon.blue.property.Property;
import java.awt.*;

public class ColorProperty extends Property<Color> {

    public ColorProperty(String label, Dependency dependency, Color value) {
        super(label, value, dependency);
    }

    public ColorProperty(String label, Color value) {
        super(label, value);
    }

    public void setValue(String value) {
        String[] rgb = value.split(":");
        this.value = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
    }

}
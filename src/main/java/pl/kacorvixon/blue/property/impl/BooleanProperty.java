package pl.kacorvixon.blue.property.impl;

import pl.kacorvixon.blue.property.Property;

public class BooleanProperty extends Property<Boolean> {
    public BooleanProperty(final String name, final boolean value) {
        super(name, value, null);
    }

    public BooleanProperty(final String name, final boolean value, final Dependency dependency) {
        super(name, value, dependency);
    }

}

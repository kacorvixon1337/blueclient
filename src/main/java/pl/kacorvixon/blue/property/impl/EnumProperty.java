package pl.kacorvixon.blue.property.impl;

import pl.kacorvixon.blue.property.Property;
public final class EnumProperty<T extends Enum<T>> extends Property<T> {
    private final T[] values;

    public EnumProperty(final String name, final T value) {
        this(name, value, null);
    }

    public EnumProperty(final String name, final T value, final Dependency dependency) {
        super(name, value, dependency);

        this.values = this.getEnumConstants();
    }

    private T[] getEnumConstants() {
        return (T[]) this.value.getClass().getEnumConstants();
    }

    public void increment() {
        T currentValue = value;

        for (T constant : values) {
            if (constant != currentValue) {
                continue;
            }

            T newValue;

            int ordinal = constant.ordinal();
            if (ordinal == values.length - 1) {
                newValue = values[0];
            } else {
                newValue = values[ordinal + 1];
            }

            value = (newValue);
            return;
        }
    }

    public T[] getValues() {
        return this.values;
    }

    public void setValue(final int index) {
        this.value = (this.values[index]);
    }
}
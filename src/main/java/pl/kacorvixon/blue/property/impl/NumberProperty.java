package pl.kacorvixon.blue.property.impl;

import pl.kacorvixon.blue.property.Property;
import pl.kacorvixon.blue.util.math.MathUtil;

public class NumberProperty extends Property<Double> {

    public final double minimum, maximum, increment;

    public NumberProperty(final String label, final Dependency dependency, final double value, final double minimum, final double maximum, final double increment) {
        super(label, value, dependency);
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public NumberProperty(final String label, final double value, final double minimum, final double maximum, final double increment) {
        super(label, value, null);
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public void setValue(final double value) {
        this.value = value > this.maximum ? this.maximum : value < this.minimum ? this.minimum : MathUtil.round(value, this.increment);
    }
}
package pl.kacorvixon.blue.module.impl.combat;

import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.NumberProperty;

public class KeepSprint extends Module {

    public KeepSprint() {
        super("KeepSprint", "KeepSprint", Category.Combat, 0);
    }
//
//    private double roundToFirstDecimalPlace(double value) {
//        double inc = 0.1;
//        double halfOfInc = inc / 2.0D;
//        double floored = Math.floor(value / inc) * inc;
//        if (value >= floored + halfOfInc)
//            return new BigDecimal(Math.ceil(value / inc) * inc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//        else return new BigDecimal(floored).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//    }
}


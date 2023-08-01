package pl.kacorvixon.blue.module.impl.combat;

import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.property.impl.NumberProperty;

public class RightClicker extends Module {


    private NumberProperty min_cps = new NumberProperty("Min CPS",6,1,20,1);
    private NumberProperty max_cps = new NumberProperty("Max CPS",6,1,20,1);
    private BooleanProperty random = new BooleanProperty("Randomization",true);
    private NumberProperty rand_min = new NumberProperty("Randomization min",1,1,5,1);
    private NumberProperty rand_max = new NumberProperty("Randomization max",1,1,5,1);





    private BooleanProperty inInventory = new BooleanProperty("Click in inventory", false);



    public RightClicker() {
        super("RightClicker", "RightClicker", Category.Combat, 0);
        addProperties(min_cps);
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


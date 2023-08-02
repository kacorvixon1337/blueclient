package pl.kacorvixon.blue.module.impl.combat;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;
import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.property.impl.NumberProperty;
import pl.kacorvixon.blue.util.PlayerUtil;
import pl.kacorvixon.blue.util.math.MathUtil;
import pl.kacorvixon.blue.util.math.TimeUtil;

public class RightClicker extends Module {


    private BooleanProperty left_click = new BooleanProperty("Left Clicker",true);

    private NumberProperty min_cps = new NumberProperty("Min CPS",13,1,20,1);
    private NumberProperty max_cps = new NumberProperty("Max CPS",20,1,20,1);

    private BooleanProperty random = new BooleanProperty("Randomization",true);

    private NumberProperty rand_min = new NumberProperty("Randomization min",1,1,5,1);
    private NumberProperty rand_max = new NumberProperty("Randomization max",1,1,5,1);

    private BooleanProperty block_check = new BooleanProperty("Blocks only", false);




    public RightClicker() {
        super("RightClicker", "RightClicker", Category.Combat, 0);
        addProperties(min_cps,max_cps,random,rand_min,rand_max,block_check);
    }

    private TimeUtil timer = new TimeUtil();

    @Override
    public void onTick(net.weavemc.loader.api.event.TickEvent e){
        if(/*mc.gameSettings.keyBindAttack.isKeyDown()*/Mouse.isButtonDown(1)){

            if(block_check.getValue() && !PlayerUtil.isHoldingWeapon())
                return;

                int cps = (int) MathUtil.getRandom(min_cps.getValue(), max_cps.getValue());
                int rand = random.getValue() ? (int) MathUtil.getRandom(rand_min.getValue(), rand_max.getValue()) : 0;
                long del = 850 / cps + rand;
                if (timer.hasReached(del)) {
                    PlayerUtil.sendLegitClick(1, true);
                    timer.reset();
                }
        }
    }
}


package pl.kacorvixon.blue.module.impl.combat;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;
import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.property.impl.NumberProperty;
import pl.kacorvixon.blue.util.math.MathUtil;
import pl.kacorvixon.blue.util.math.TimerUtil;

public class LeftClicker extends Module {



    private BooleanProperty left_click = new BooleanProperty("Left Clicker",true);

    private NumberProperty min_cps = new NumberProperty("Min CPS",6,1,20,1);
    private NumberProperty max_cps = new NumberProperty("Max CPS",6,1,20,1);

    private BooleanProperty random = new BooleanProperty("Randomization",true);

    private NumberProperty rand_min = new NumberProperty("Randomization min",1,1,5,1);
    private NumberProperty rand_max = new NumberProperty("Randomization max",1,1,5,1);

//    private BooleanProperty spike = new BooleanProperty("Spikes",true);
//
//    private NumberProperty spike_chance_up = new NumberProperty("Spike chance",1,1,100,1);
//    private NumberProperty drop_chance_down = new NumberProperty("Drop chance",1,1,100,1);
//
//
//    private NumberProperty spike_amount = new NumberProperty("Spike amount",1,1,5,1);
//    private NumberProperty drop_chance = new NumberProperty("Drop amount",1,1,5,1);

    private BooleanProperty block_check = new BooleanProperty("Block break", false);
    private BooleanProperty weapon_check = new BooleanProperty("Weapon only", false);
    private BooleanProperty hit_select = new BooleanProperty("Hit select", false);




    public LeftClicker() {
        super("LeftClicker", "LeftClicker", Category.Combat, 0);
        addProperties(min_cps, max_cps, random, rand_min, rand_max, /*spike, spike_chance_up, drop_chance_down, spike_amount, drop_chance,*/block_check, weapon_check, hit_select);
    }

    private TimerUtil timer = new TimerUtil();
    private double leftDelay;

    @Override
    public void onTick(net.weavemc.loader.api.event.TickEvent e){
        if(/*mc.gameSettings.keyBindAttack.isKeyDown()*/Mouse.isButtonDown(0)){

            mc.gameSettings.keyBindAttack.pressed = false;

            if(block_check.getValue()){
                BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
                Block bruhnig = Minecraft.getMinecraft().theWorld.getBlockState(blockpos).getBlock();
                if(bruhnig != Blocks.air || bruhnig != Blocks.water || bruhnig != Blocks.flowing_water || bruhnig != Blocks.lava || bruhnig != Blocks.flowing_lava)
                    return;
            }

            if(weapon_check.getValue() && !isHoldingWeapon())
                return;

            if (timer.hasReached((long) leftDelay)) {
                if (mc.currentScreen == null) {
                    mc.clickMouse();
                }
                double cpsRange = Math.abs(max_cps.getValue() - min_cps.getValue());
                leftDelay = (1 / (min_cps.getValue() + Math.random() * cpsRange)) * 1000;
                timer.reset();
            }
        }
    }
    public static boolean isHoldingWeapon() {
        Item item = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem();
        if(item instanceof ItemSword)
            return true;
        else return false;
    }
}


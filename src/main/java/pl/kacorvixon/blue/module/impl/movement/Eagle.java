package pl.kacorvixon.blue.module.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.property.impl.NumberProperty;
import pl.kacorvixon.blue.util.log.Logger;
import pl.kacorvixon.blue.util.math.TimerUtil;

public class Eagle extends Module {



    private NumberProperty delay = new NumberProperty("Place delay", 1, 0, 5, 0.1);
    private BooleanProperty blocks_only = new BooleanProperty("Blocks only", true);
    private BooleanProperty backwards_only = new BooleanProperty("Backwards only", false);

    public Eagle() {
        super("Eagle", "Eagle", Category.Combat, 0);
        addProperties(delay, blocks_only, backwards_only);
    }
    private TimerUtil timer = new TimerUtil();
    long delayxd = (long) (delay.getValue() * 100);
    @Override
    public void onTick(net.weavemc.loader.api.event.TickEvent event){
            if(blocks_only.getValue() && !holdingBlock()) return;
            if(backwards_only.getValue() && mc.gameSettings.keyBindForward.isKeyDown()) return;

            ItemStack i = mc.thePlayer.getCurrentEquippedItem();
            BlockPos Bp = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1D, mc.thePlayer.posZ);
            if(i != null) {
                if(i.getItem() instanceof ItemBlock) {
                    if(timer.hasTimeElapsed(delayxd, true))
                        mc.gameSettings.keyBindSneak.pressed = false;
                    if(blockRelativeToPlayer(0, -1, 0) instanceof BlockAir && !mc.gameSettings.keyBindForward.isKeyDown()) {
                        mc.gameSettings.keyBindSneak.pressed = true;
                    }
                }
            }
    }
    @Override
    public void onDisable(){
        mc.gameSettings.keyBindSneak.pressed = false;
    }
    public static ItemStack getCurrentItem() {
        return mc.thePlayer.getCurrentEquippedItem() == null ? new ItemStack(Blocks.air) : mc.thePlayer.getCurrentEquippedItem();
    }
    public static boolean holdingBlock() {
        if (getCurrentItem() == null) {
            return false;
        } else {
            return getCurrentItem().getItem() instanceof ItemBlock && !getCurrentItem().getDisplayName().equalsIgnoreCase("sand");
        }
    }
    public Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).add(offsetX, offsetY, offsetZ)).getBlock();
    }
}


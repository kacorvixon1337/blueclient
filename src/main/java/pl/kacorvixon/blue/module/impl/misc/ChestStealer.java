package pl.kacorvixon.blue.module.impl.misc;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.property.impl.NumberProperty;
import pl.kacorvixon.blue.util.ItemUtil;
import pl.kacorvixon.blue.util.math.TimeUtil;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ChestStealer extends Module {
    public ChestStealer() {
        super("ChestStealer","Chest Stealer", Category.Misc, 0);
        addProperties(min, max, KEEP, AutoClose);
    }

    private final NumberProperty min = new NumberProperty("Min Delay", 40.0, 5.0, 200.0, 1.0);

    private final NumberProperty max = new NumberProperty("Max Delay", 40.0, 5.0, 200.0, 1.0);

    private final BooleanProperty KEEP = new BooleanProperty("Keep Mouse", false);

    private final BooleanProperty AutoClose = new BooleanProperty("Auto Close", true);

    //public static BooleanProperty Silent = new BooleanProperty("Silent", false);

    private final TimeUtil timer = new TimeUtil();

    double delay;

    public static boolean stealing;

    @Override
    public void onTick(net.weavemc.loader.api.event.TickEvent event) {
        int index = 0;
        if (mc.currentScreen instanceof GuiChest) {
            final GuiChest chest = (GuiChest) mc.currentScreen;
            stealing = true;
            if (KEEP.value) {
                mc.inGameHasFocus = true;
                mc.mouseHelper.grabMouseCursor();
            }
            if (!KEEP.value) {
                mc.inGameHasFocus = false;
            }

            if (AutoClose.value) {
                if (ItemUtil.isChestEmpty(chest)) {
                    mc.thePlayer.closeScreen();
                    stealing = false;
                    return;
                }
            }

            for (index = 0; index < chest.lowerChestInventory.getSizeInventory(); ++index) {
                final ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
                if (stack != null && this.timer.hasReached((long) this.delay) && ItemUtil.isValid(stack)) {
                    stealing = true;
                    mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
                    this.setDelay();
                    this.timer.reset();
                    break;
                }
            }
        }
    }

    @Override
    public void onRender2D(net.weavemc.loader.api.event.RenderGameOverlayEvent eventRender) {
        if (mc.currentScreen instanceof GuiChest) {
            mc.inGameHasFocus = true;
            mc.mouseHelper.grabMouseCursor();
        }
    }

    public void setDelay() {
        if (Objects.equals(min.value, max.value)) {
            max.setValue(min.value * 1.1);
        }
        this.delay = ThreadLocalRandom.current().nextDouble(Math.min(min.value, max.value), Math.max(min.value, max.value));
    }
    public static boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); ++index) {
            final ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
            if (stack != null) {
                if (ItemUtil.isValid(stack)) {
                    return false;
                }
            }
        }
        return true;
    }

}

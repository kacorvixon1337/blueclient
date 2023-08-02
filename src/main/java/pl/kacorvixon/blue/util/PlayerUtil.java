package pl.kacorvixon.blue.util;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

import java.util.Iterator;
import java.util.List;

public class PlayerUtil {
    static Minecraft mc = Minecraft.getMinecraft();
    public static boolean isHoldingWeapon() {
        Item item = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem();
        if(item instanceof ItemSword)
            return true;
        else return false;
    }
    public static void sendLegitClick(final int button, final boolean state) {
        final int keyBind = button == 0 ? mc.gameSettings.keyBindAttack.getKeyCode() : mc.gameSettings.keyBindUseItem.getKeyCode();

        KeyBinding.setKeyBindState(keyBind, state);

        if (state) {
            KeyBinding.onTick(keyBind);
        }
    }
    public static boolean isBreakingBlock() {
        BlockPos blockpos = mc.objectMouseOver.getBlockPos();
        if (blockpos != null) {
            Block bruhnig = Minecraft.getMinecraft().theWorld.getBlockState(blockpos).getBlock();
            if (bruhnig != Blocks.air || bruhnig != Blocks.water || bruhnig != Blocks.flowing_water || bruhnig != Blocks.lava || bruhnig != Blocks.flowing_lava)
                return true;
        }
        return false;
    }
}

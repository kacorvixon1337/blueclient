package pl.kacorvixon.blue.module.impl.combat;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.property.impl.NumberProperty;
import pl.kacorvixon.blue.util.render.RenderUtil;
import pl.kacorvixon.blue.util.render.animation.Animation;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Reach extends Module {

    public static NumberProperty range = new NumberProperty("Range",3,3,5,0.1);

    public Reach() {
        super("Reach", "Reach", Category.Combat, 0);
        addProperties(range);
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


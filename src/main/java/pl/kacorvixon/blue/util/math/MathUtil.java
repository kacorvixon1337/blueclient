package pl.kacorvixon.blue.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static net.minecraft.util.MathHelper.*;

public class MathUtil {

    public static final Random RANDOM = new Random();
    public static Double interpolate(double oldValue, double newValue, double interpolationValue){
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }
    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }
    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }
    public static double round(final double value, final double inc) {
        if (inc == 0.0) return value;
        else if (inc == 1.0) return Math.round(value);
        else {
            final double halfOfInc = inc / 2.0;
            final double floored = Math.floor(value / inc) * inc;

            if (value >= floored + halfOfInc)
                return new BigDecimal(Math.ceil(value / inc) * inc)
                        .doubleValue();
            else return new BigDecimal(floored)
                    .doubleValue();
        }
    }
    public static double getRandom(double min, double max) {
        if (min == max) {
            return min;
        } else if (min > max) {
            final double d = min;
            min = max;
            max = d;
        }
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double round(final double value, final int scale, final double inc) {
        final double halfOfInc = inc / 2.0;
        final double floored = Math.floor(value / inc) * inc;

        if (value >= floored + halfOfInc)
            return new BigDecimal(Math.ceil(value / inc) * inc)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .doubleValue();
        else return new BigDecimal(floored)
                .setScale(scale, RoundingMode.HALF_UP)
                .doubleValue();
    }
    public static double func_181159_b(double p_181159_0_, double p_181159_2_) {
        double d0 = p_181159_2_ * p_181159_2_ + p_181159_0_ * p_181159_0_;
        if (Double.isNaN(d0)) {
            return Double.NaN;
        } else {
            boolean flag = p_181159_0_ < 0.0D;
            if (flag) {
                p_181159_0_ = -p_181159_0_;
            }

            boolean flag1 = p_181159_2_ < 0.0D;
            if (flag1) {
                p_181159_2_ = -p_181159_2_;
            }

            boolean flag2 = p_181159_0_ > p_181159_2_;
            double d9;
            if (flag2) {
                d9 = p_181159_2_;
                p_181159_2_ = p_181159_0_;
                p_181159_0_ = d9;
            }

            d9 = func_181161_i(d0);
            p_181159_2_ *= d9;
            p_181159_0_ *= d9;
            double d2 = field_181163_d + p_181159_0_;
            int i = (int)Double.doubleToRawLongBits(d2);
            double d3 = field_181164_e[i];
            double d4 = field_181165_f[i];
            double d5 = d2 - field_181163_d;
            double d6 = p_181159_0_ * d4 - p_181159_2_ * d5;
            double d7 = (6.0D + d6 * d6) * d6 * 0.16666666666666666D;
            double d8 = d3 + d7;
            if (flag2) {
                d8 = 1.5707963267948966D - d8;
            }

            if (flag1) {
                d8 = 3.141592653589793D - d8;
            }

            if (flag) {
                d8 = -d8;
            }

            return d8;
        }
    }
    public static float wrapAngleToCustom_float(float var0, float var1) {
        var0 %= 360.0F;
        if (var0 >= var1) {
            var0 -= 360.0F;
        }

        if (var0 < -var1) {
            var0 += 360.0F;
        }

        return var0;
    }
}

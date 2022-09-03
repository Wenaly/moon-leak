package com.moon.api.utils.math;

import net.minecraft.util.math.Vec3d;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    public static double roundToPlaces(double i, int places) {
        BigDecimal decimal = new BigDecimal(i);
        decimal = decimal.setScale(places, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

    public static Vec3d roundVector(final Vec3d vec3d, final int places) {
        return new Vec3d(roundToPlaces(vec3d.x, places), roundToPlaces(vec3d.y, places), roundToPlaces(vec3d.z, places));
    }

    public static double round(double value, int places) throws IllegalArgumentException {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(places, RoundingMode.FLOOR);
        return decimal.doubleValue();
    }

}

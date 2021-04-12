package io.infinitestrike.flatpixel.math;

public class FPMath {
    public static final float EPSILON = 0.00000000000000000000000000000000000000000001f;

    public static boolean isEven(int v){
        return v%2 == 0;
    }

    public static boolean isOdd(int v){
        return v%2 != 0;
    }
}

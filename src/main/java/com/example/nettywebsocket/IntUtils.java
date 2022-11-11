package com.example.nettywebsocket;

import java.util.Random;

public class IntUtils {
    public static    Random r = new Random();
    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return r.nextInt((max - min) + 1) + min;
    }
}

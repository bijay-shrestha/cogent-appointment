package com.cogent.cogentappointment.utils.commons;

import java.util.Random;
import java.util.UUID;

public class RandomNumberGenerator {

    private static int[] sizeTable = {0, 9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};

    private static Random random = new Random();

    public static String generateRandomNumber(int digit) {
        int randomNumber = random.nextInt(sizeTable[digit] + 1);
        return String.valueOf(randomNumber);
    }

    public static String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

}

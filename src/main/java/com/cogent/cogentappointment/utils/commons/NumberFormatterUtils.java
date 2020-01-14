package com.cogent.cogentappointment.utils.commons;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;

public class NumberFormatterUtils {

    private static int[] sizeTable = {0, 9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};

    private static Random random = new Random();

    public static String generateRandomNumber(int digit) {
        int randomNumber = random.nextInt(sizeTable[digit] + 1);
        return String.valueOf(randomNumber);
    }

    public static Double formatDoubleTo2DecimalPlaces(Double amount) {
        DecimalFormat format = new DecimalFormat("##.00");
        return Double.parseDouble(format.format(amount));
    }

    public static String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

}

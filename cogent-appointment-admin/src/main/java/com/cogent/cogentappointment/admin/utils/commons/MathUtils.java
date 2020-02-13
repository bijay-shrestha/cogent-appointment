package com.cogent.cogentappointment.admin.utils.commons;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.cogent.cogentappointment.admin.utils.commons.NumberFormatterUtils.formatDoubleTo2DecimalPlaces;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public class MathUtils {

    private static final int PRECISION_VALUE = 2;
    private static final int HUNDRED_PERCENT = 100;

    public static BigDecimal calculatePercenatge(BigDecimal current, BigDecimal previous) {
        if (previous.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return current.subtract(previous).divide(previous, PRECISION_VALUE, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(HUNDRED_PERCENT));
    }

    public static Double calculatePercenatge(Double current, Double previous) {

        if(current==0 && previous==0 || previous==0){
            return 0D;
        }
        return formatDoubleTo2DecimalPlaces((current - previous)/previous * 100 );
    }
}




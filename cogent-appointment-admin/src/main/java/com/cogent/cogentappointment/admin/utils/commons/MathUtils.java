package com.cogent.cogentappointment.admin.utils.commons;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.cogent.cogentappointment.admin.utils.commons.NumberFormatterUtils.formatDoubleTo2DecimalPlaces;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public class MathUtils {

    public static Double calculatePercenatge(Double current, Double previous) {

        if(current==0 && previous==0 || previous==0){
            return 0D;
        }
        return formatDoubleTo2DecimalPlaces((current - previous)/previous * 100 );
    }
}




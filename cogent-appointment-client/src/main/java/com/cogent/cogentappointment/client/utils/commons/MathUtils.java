package com.cogent.cogentappointment.client.utils.commons;

import static com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils.formatDoubleTo2DecimalPlaces;

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

    public static Double calculateTotalTransactionAmount(Double currentTransaction, Double refund) {

        return formatDoubleTo2DecimalPlaces(currentTransaction-refund);
    }


}

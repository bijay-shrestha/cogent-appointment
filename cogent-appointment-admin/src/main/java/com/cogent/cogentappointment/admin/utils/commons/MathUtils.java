package com.cogent.cogentappointment.admin.utils.commons;

import static com.cogent.cogentappointment.admin.utils.commons.NumberFormatterUtils.formatDoubleTo2DecimalPlaces;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public class MathUtils {

    public static Double calculatePercenatge(Double current, Double previous) {
        return formatDoubleTo2DecimalPlaces((current - previous) * 100 / current);
    }


}

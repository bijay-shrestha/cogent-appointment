package com.cogent.cogentappointment.logging.utils;

import java.util.Date;
import java.util.Objects;

import static com.cogent.cogentappointment.logging.utils.common.DateUtils.*;

/**
 * @author Rupak
 */
public class AdminLogUtils {

    private static Date parseAdminLogTime(Date logDate, String logTime) {
        return datePlusTime(utilDateToSqlDate(logDate), Objects.requireNonNull(parseTime(logTime)));
    }

}

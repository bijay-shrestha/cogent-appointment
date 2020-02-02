package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 25/11/2019
 */
public class WeekDaysQuery {
    public static final String QUERY_TO_FETCH_ACTIVE_WEEK_DAYS =
            "SELECT" +
                    " w.id as weekDaysId," +
                    " w.name as weekDaysName" +
                    " FROM WeekDays w" +
                    " WHERE w.status = 'Y'";
}

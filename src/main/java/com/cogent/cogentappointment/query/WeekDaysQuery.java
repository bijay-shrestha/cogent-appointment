package com.cogent.cogentappointment.query;

/**
 * @author smriti on 25/11/2019
 */
public class WeekDaysQuery {
    public static final String QUERY_TO_FETCH_ACTIVE_WEEK_DAYS =
            "SELECT" +
                    " w.id as value," +
                    " w.name as label" +
                    " FROM WeekDays w" +
                    " WHERE w.status = 'Y'";
}

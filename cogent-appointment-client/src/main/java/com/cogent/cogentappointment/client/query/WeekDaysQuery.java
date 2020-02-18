package com.cogent.cogentappointment.client.query;

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
    public static final String QUERY_TO_PREPARE_ACTIVE_WEEK_DAYS =
            "SELECT" +
                    " w.id as weekDaysId," +
                    " w.name as weekDaysName" +
                    " FROM WeekDays w" +
                    " WHERE w.status = 'Y'";

}

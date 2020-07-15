package com.cogent.cogentappointment.client.query;

/**
 * @author smriti on 14/07/20
 */
public class AppointmentModeQuery {

    public static final String QUERY_TO_FETCH_ACTIVE_APPOINTMENT_MODE =
            "SELECT" +
                    " am.id as value," +
                    " am.name as label" +
                    " FROM AppointmentMode am " +
                    " WHERE am.status = 'Y'";
}

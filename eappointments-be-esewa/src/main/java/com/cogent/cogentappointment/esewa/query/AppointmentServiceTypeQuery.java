package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 26/05/20
 */
public class AppointmentServiceTypeQuery {

    public static final String QUERY_TO_FETCH_MIN_APPOINTMENT_SERVICE_TYPE =
            "SELECT" +
                    " a.id as value," +
                    " a.name as label" +
                    " FROM AppointmentServiceType a" +
                    " WHERE a.status = 'Y'";
}

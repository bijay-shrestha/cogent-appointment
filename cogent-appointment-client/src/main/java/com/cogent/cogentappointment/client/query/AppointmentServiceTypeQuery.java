package com.cogent.cogentappointment.client.query;

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

    public static final String QUERY_TO_FETCH_APPOINTMENT_SERVICE_TYPE_NAME_AND_CODE =
            "SELECT" +
                    " a.name as name," +
                    " a.code as code" +
                    " FROM AppointmentServiceType a" +
                    " WHERE a.status = 'Y'";

    public static final String QUERY_TO_FETCH_PRIMARY_APPOINTMENT_SERVICE_TYPE_BY_HOSPIAL_ID =
            "SELECT" +
                    " hast.id as value," +
                    " ast.name as name" +
                    " FROM HospitalAppointmentServiceType hast" +
                    " LEFT JOIN AppointmentServiceType ast ON ast.id=hast.appointmentServiceType.id" +
                    " LEFT JOIN Hospital h ON h.id=hast.hospital.id" +
                    " WHERE hast.status = 'Y'" +
                    " AND ast.status='Y'" +
                    " AND h.id=:hospitalId";
}

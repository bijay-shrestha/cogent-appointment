package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 27/05/20
 */
public class HospitalAppointmentServiceTypeQuery {

    public static final String QUERY_TO_FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE =
            "SELECT" +
                    " h" +
                    " FROM HospitalAppointmentServiceType h " +
                    " WHERE" +
                    " h.hospital.id =:hospitalId" +
                    " AND h.appointmentServiceType.code =:appointmentServiceTypeCode" +
                    " AND h.status = 'Y'" +
                    " AND h.appointmentServiceType.status = 'Y'";
}

package com.cogent.cogentappointment.client.query;

/**
 * @author Sauravi Thapa ON 6/18/20
 */
public class AppointmentDoctorInfoQuery {

    public static String QUERY_TO_GET_CURRENT_APPOINTMENT_DOCTOR_INFO=
            "SELECT" +
                    " a.id as id," +
                    " a.appointment as appointment," +
                    " a.specialization as specialization," +
                    " a.doctor as doctor" +
                    " FROM AppointmentDoctorInfo a" +
                    " WHERE a.appointment.id=:appointmentId";

}

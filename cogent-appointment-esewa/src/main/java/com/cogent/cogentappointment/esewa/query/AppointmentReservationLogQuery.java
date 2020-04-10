package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 19/02/20
 */
public class AppointmentReservationLogQuery {

    public static String QUERY_TO_FETCH_APPOINTMENT_RESERVATION_LOG =
            "SELECT DATE_FORMAT(a.appointmentTime, '%H:%i') as appointmentTime" +               //[0]
                    " FROM AppointmentReservationLog a" +
                    " WHERE" +
                    " a.appointmentDate = :date" +
                    " AND a.doctorId = :doctorId" +
                    " AND a.specializationId= :specializationId";

    public static String QUERY_TO_VALIDATE_APPOINTMENT_RESERVATION_DUPLICITY_EXCEPT_CURRENT_ID =
            "SELECT COUNT(a.id)" +
                    " FROM AppointmentReservationLog a" +
                    " WHERE" +
                    " a.id !=:appointmentId" +
                    " AND a.appointmentDate =:appointmentDate" +
                    " AND a.doctorId =:doctorId" +
                    " AND a.specializationId =:specializationId" +
                    " AND DATE_FORMAT(a.appointmentTime,'%H:%i') =:appointmentTime";

    public static String QUERY_TO_FETCH_APPOINTMENT_RESERVATION_LOG_ID =
            "SELECT a.id" +
                    " FROM AppointmentReservationLog a" +
                    " WHERE" +
                    " a.appointmentDate =:appointmentDate" +
                    " AND a.doctorId =:doctorId" +
                    " AND a.specializationId =:specializationId" +
                    " AND DATE_FORMAT(a.appointmentTime,'%H:%i') =:appointmentTime";

    public static String QUERY_TO_VALIDATE_APPOINTMENT_RESERVATION_DUPLICITY =
            "SELECT COUNT(a.id)" +
                    " FROM AppointmentReservationLog a" +
                    " WHERE" +
                    " a.appointmentDate =:appointmentDate" +
                    " AND a.doctorId =:doctorId" +
                    " AND a.specializationId =:specializationId" +
                    " AND DATE_FORMAT(a.appointmentTime,'%H:%i') =:appointmentTime";
}

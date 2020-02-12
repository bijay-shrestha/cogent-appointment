package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 18/11/2019
 */
public class AppointmentFollowUpTrackerQuery {

    public static final String QUERY_TO_FETCH_MINIMAL_FOLLOW_UP_TRACKER =
            " SELECT" +
                    " f.parentAppointmentNumber as parentAppointmentNumber," +             //[0]
                    " f.remainingNumberOfFollowUps as remainingNumberOfFollowUps," +        //[1]
                    " f.patientTypeId.name as patientType" +                               //[1]
                    " FROM FollowUpTracker f " +
                    " WHERE" +
                    " f.active = 'Y'" +
                    " AND f.appointmentStatus= 'S'" +
                    " AND f.patientId.id = :patientId" +
                    " AND f.doctorId.id = :doctorId";

    public static final String QUERY_TO_FETCH_APPOINTMENT_FOLLOW_UP_TRACKER =
            " SELECT f FROM FollowUpTracker f" +
                    " WHERE " +
                    " f.active = 'Y'" +
                    " AND f.appointmentStatus= 'S'" +
                    " AND f.parentAppointmentNumber =:parentAppointmentNumber" +
                    " AND f.patientId.id= :patientId" +
                    " AND f.doctorId.id = :doctorId";
}

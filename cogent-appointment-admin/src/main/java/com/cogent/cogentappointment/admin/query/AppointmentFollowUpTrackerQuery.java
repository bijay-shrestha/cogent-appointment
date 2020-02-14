package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 18/11/2019
 */
public class AppointmentFollowUpTrackerQuery {

    public static final String QUERY_TO_VALIDATE_FOLLOW_UP_TRACKER_EXISTS =
            " SELECT" +
                    " f.parentAppointmentId as parentAppointmentId" +
                    " FROM AppointmentFollowUpTracker f " +
                    " WHERE" +
                    " f.status = 'Y'" +
                    " AND f.patientId.id = :patientId" +
                    " AND f.doctorId.id = :doctorId" +
                    " AND f.specializationId.id=:specializationId" +
                    " AND f.hospitalId.id =:hospitalId";

    public static final String QUERY_TO_FETCH_LATEST_APPOINTMENT_FOLLOW_UP_TRACKER =
            " SELECT f FROM appointment_follow_up_tracker f" +
                    " WHERE f.status ='Y'" +
                    " AND f.parent_appointment_id=:parentAppointmentId" +
                    " ORDER BY f.id DESC LIMIT 1";

    public static final String QUERY_TO_FETCH_APPOINTMENT_FOLLOW_UP_TRACKER =
            " SELECT f FROM FollowUpTracker f" +
                    " WHERE " +
                    " f.active = 'Y'" +
                    " AND f.appointmentStatus= 'S'" +
                    " AND f.parentAppointmentNumber =:parentAppointmentNumber" +
                    " AND f.patientId.id= :patientId" +
                    " AND f.doctorId.id = :doctorId";
}

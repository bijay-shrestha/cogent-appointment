package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 18/11/2019
 */
public class AppointmentFollowUpTrackerQuery {

    public static final String QUERY_TO_FETCH_FOLLOW_UP_DETAILS =
            " SELECT" +
                    " f.parentAppointmentId as parentAppointmentId," +              //[0]
                    " f.appointmentApprovedDate as appointmentApprovedDate" +       //[1]
                    " FROM AppointmentFollowUpTracker f " +
                    " WHERE" +
                    " f.status = 'Y'" +
                    " AND f.patientId.id = :patientId" +
                    " AND f.doctorId.id = :doctorId" +
                    " AND f.specializationId.id=:specializationId" +
                    " AND f.hospitalId.id =:hospitalId";


    public static final String QUERY_TO_FETCH_LATEST_APPOINTMENT_FOLLOW_UP_TRACKER =
            " SELECT * FROM appointment_follow_up_tracker f" +
                    " WHERE f.status ='Y'" +
                    " AND f.parent_appointment_id=:parentAppointmentId" +
                    " ORDER BY f.id DESC LIMIT 1";

}

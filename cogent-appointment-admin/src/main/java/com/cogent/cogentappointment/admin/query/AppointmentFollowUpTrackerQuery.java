package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 18/11/2019
 */
public class AppointmentFollowUpTrackerQuery {

    public static final String QUERY_TO_FETCH_LATEST_APPOINTMENT_FOLLOW_UP_TRACKER =
            " SELECT * FROM appointment_follow_up_tracker f" +
                    " WHERE f.status ='Y'" +
                    " AND f.parent_appointment_id=:parentAppointmentId" +
                    " ORDER BY f.id DESC LIMIT 1";
}

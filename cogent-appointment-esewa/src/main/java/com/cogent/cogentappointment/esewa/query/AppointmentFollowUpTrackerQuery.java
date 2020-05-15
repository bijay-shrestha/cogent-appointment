package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 18/11/2019
 */
public class AppointmentFollowUpTrackerQuery {

    public static final String QUERY_TO_FETCH_APPOINTMENT_FOLLOW_UP_TRACKER =
            " SELECT" +
                    " f" +
                    " FROM AppointmentFollowUpTracker f " +
                    " WHERE" +
                    " f.status = 'Y'" +
                    " AND f.patientId.id = :patientId" +
                    " AND f.doctorId.id = :doctorId" +
                    " AND f.specializationId.id=:specializationId" +
                    " AND f.hospitalId.id =:hospitalId";

}

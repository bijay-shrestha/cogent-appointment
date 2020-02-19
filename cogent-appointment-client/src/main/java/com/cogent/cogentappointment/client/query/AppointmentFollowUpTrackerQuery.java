package com.cogent.cogentappointment.client.query;

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

}

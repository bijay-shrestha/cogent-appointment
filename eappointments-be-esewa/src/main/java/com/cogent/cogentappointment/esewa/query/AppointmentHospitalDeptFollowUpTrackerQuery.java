package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 04/06/20
 */
public class AppointmentHospitalDeptFollowUpTrackerQuery {

    public static final String QUERY_TO_FETCH_APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER =

            " SELECT" +
                    " f" +
                    " FROM AppointmentHospitalDepartmentFollowUpTracker f " +
                    " WHERE" +
                    " f.status = 'Y'" +
                    " AND f.patient.id = :patientId" +
                    " AND f.hospitalDepartment.id = :hospitalDepartmentId" +
                    " AND f.hospital.id =:hospitalId";
}

package com.cogent.cogentappointment.client.query;

/**
 * @author smriti on 18/11/2019
 */
public class AppointmentHospitalDepartmentFollowUpTrackerQuery {

    public static final String QUERY_TO_FETCH_LATEST_APPOINTMENT_HOSPITAL_DEPT_FOLLOW_UP_TRACKER =
            " SELECT * FROM appointment_hospital_department_follow_up_tracker f" +
                    " WHERE f.status ='Y'" +
                    " AND f.parent_appointment_id=:parentAppointmentId" +
                    " ORDER BY f.id DESC LIMIT 1";

    public static final String QUERY_TO_UPDATE_HOSPITAL_DEPT_APPOINTMENT_FOLLOW_UP_TRACKER =
            "UPDATE AppointmentHospitalDepartmentFollowUpTracker f " +
                    " SET remainingNumberOfFollowUps = 0, status = 'N'" +
                    " WHERE" +
                    " f.status = 'Y'" +
                    " AND f.patient.id = :patientId" +
                    " AND f.hospitalDepartment.id = :hospitalDepartmentId" +
                    " AND f.hospital.id =:hospitalId";

}

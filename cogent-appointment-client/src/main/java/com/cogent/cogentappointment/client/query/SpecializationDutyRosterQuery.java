package com.cogent.cogentappointment.client.query;

/**
 * @author Sauravi Thapa ON 5/18/20
 */
public class SpecializationDutyRosterQuery {
    public static final String VALIDATE_SPECIALIZATION_DUTY_ROSTER_COUNT =
            " SELECT COUNT(sdr.id)" +
                    " FROM HospitalDepartmentDutyRoster sdr" +
                    " WHERE sdr.status != 'D'" +
                    " AND sdr.specialization.id= :specializationId" +
                    " AND sdr.toDate >=:fromDate" +
                    " AND sdr.fromDate <=:toDate";
}

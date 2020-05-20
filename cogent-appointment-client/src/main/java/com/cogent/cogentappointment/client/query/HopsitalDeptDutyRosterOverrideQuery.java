package com.cogent.cogentappointment.client.query;

/**
 * @author smriti on 20/05/20
 */
public class HopsitalDeptDutyRosterOverrideQuery {

    public static final String VALIDATE_SPECIALIZATION_DUTY_ROSTER_OVERRIDE_COUNT =
            " SELECT COUNT(sdro.id)" +
                    " FROM HospitalDepartmentDutyRosterOverride sdro" +
                    " LEFT JOIN HospitalDepartmentDutyRoster sdr ON sdr.id = sdro.hospitalDepartmentDutyRoster.id" +
                    " WHERE sdr.status != 'D'" +
                    " AND sdro.status = 'Y'" +
                    " AND sdr.specializationId.id= :specializationId" +
                    " AND sdro.toDate >=:fromDate" +
                    " AND sdro.fromDate <=:toDate";
}

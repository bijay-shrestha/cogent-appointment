package com.cogent.cogentappointment.client.query;

/**
 * @author Sauravi Thapa ON 5/18/20
 */
public class SpecializationDutyRosterOverrideQuery {
    public static final String VALIDATE_SPECIALIZATION_DUTY_ROSTER_OVERRIDE_COUNT =
            " SELECT COUNT(sdro.id)" +
                    " FROM SpecializationDutyRosterOverride sdro" +
                    " LEFT JOIN SpecializationDutyRoster sdr ON sdr.id = sdro.specializationDutyRoster.id" +
                    " WHERE sdr.status != 'D'" +
                    " AND sdro.status = 'Y'" +
                    " AND sdr.specializationId.id= :specializationId" +
                    " AND sdro.toDate >=:fromDate" +
                    " AND sdro.fromDate <=:toDate";
}

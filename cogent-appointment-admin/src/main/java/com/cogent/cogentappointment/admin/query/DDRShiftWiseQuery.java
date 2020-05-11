package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 08/05/20
 */
public class DDRShiftWiseQuery {

    public static final String VALIDATE_DDR_SHIFT_WISE_COUNT =
            " SELECT COUNT(d.id)" +
                    " FROM DoctorDutyRosterShiftWise d" +
                    " WHERE d.status != 'D'" +
                    " AND d.doctor.id=:doctorId" +
                    " AND d.specialization.id= :specializationId" +
                    " AND d.toDate >=:fromDate" +
                    " AND d.fromDate <=:toDate";

}

package com.cogent.cogentappointment.admin.query.ddrShiftWise;

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

    public static final String QUERY_TO_FETCH_EXISTING_DDR =
            " SELECT" +
                    " dd.id as ddrId," +                                      //[0]
                    " dd.fromDate as fromDate," +                            //[1]
                    " dd.toDate as toDate," +                                //[2]
                    " dd.rosterGapDuration as rosterGapDuration" +           //[3]
                    " FROM DoctorDutyRosterShiftWise dd" +
                    " WHERE dd.status != 'D'" +
                    " AND dd.doctor.id=:doctorId" +
                    " AND dd.specialization.id= :specializationId" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";



}

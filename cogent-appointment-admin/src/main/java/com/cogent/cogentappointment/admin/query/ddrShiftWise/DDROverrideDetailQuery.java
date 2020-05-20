package com.cogent.cogentappointment.admin.query.ddrShiftWise;

import java.util.Objects;

/**
 * @author smriti on 13/05/20
 */
public class DDROverrideDetailQuery {

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_DETAIL =
            " SELECT" +
                    " dd.id as ddrOverrideId," +                                        //[0]
                    " dd.date as date," +                                               //[1]
                    " dd.startTime as startTime," +                                     //[2]
                    " dd.endTime as endTime," +                                         //[3]
                    " dd.shift.id as shiftId," +                                         //[4]
                    " dd.shift.name as shiftName," +                                    //[5]
                    " dd.offStatus as offStatus," +                                     //[6]
                    " dd.rosterGapDuration as rosterGapDuration," +                     //[7]
                    " dd.hasBreak as hasBreak," +                                       //[8]
                    " dd.remarks as remarks" +                                          //[9]
                    " FROM DDROverrideDetail dd" +
                    " WHERE dd.status = 'Y'" +
                    " AND dd.ddrShiftWise.status != 'D'" +
                    " AND dd.ddrShiftWise.id = :ddrId";

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_TIME_DETAIL =
            " SELECT" +
                    " dd.startTime as startTime," +                                     //[0]
                    " dd.endTime as endTime," +                                         //[1]
                    " dd.shift.name as shiftName" +                                     //[2]
                    " FROM DoctorDutyRosterShiftWise ddr" +
                    " LEFT JOIN DDROverrideDetail dd ON ddr.id = dd.ddrShiftWise.id" +
                    " WHERE dd.status = 'Y'" +
                    " AND ddr.status != 'D'" +
                    " AND ddr.doctor.id=:doctorId" +
                    " AND ddr.specialization.id= :specializationId" +
                    " AND dd.date =:date";

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_TIME_DETAIL_EXCEPT_CURRENT_ID =
            " SELECT" +
                    " dd.startTime as startTime," +                                     //[0]
                    " dd.endTime as endTime," +                                        //[1]
                    " dd.shift.name as shiftName" +                                    //[2]
                    " FROM DoctorDutyRosterShiftWise ddr" +
                    " LEFT JOIN DDROverrideDetail dd ON ddr.id = dd.ddrShiftWise.id" +
                    " WHERE dd.status = 'Y'" +
                    " AND ddr.status != 'D'" +
                    " AND dd.id !=:ddrOverrideId" +
                    " AND ddr.doctor.id=:doctorId" +
                    " AND ddr.specialization.id= :specializationId" +
                    " AND dd.date =:date";

    public static String QUERY_TO_FETCH_DDR_OVERRIDE_COUNT(Long ddrOverrideId) {

        String query = " SELECT" +
                " COUNT(dd.id) " +
                " FROM DDROverrideDetail dd" +
                " WHERE" +
                " dd.status = 'Y'" +
                " AND dd.date =:date" +
                " AND dd.ddrShiftWise.id=:ddrId" +
                " AND (DATE_FORMAT(dd.endTime,'%H:%i') >:startTime" +
                " AND DATE_FORMAT(dd.startTime,'%H:%i')<:endTime)";

        /*IF UPDATING EXISTING OVERRIDE DETAIL, VALIDATE EXCEPT CURRENT ID*/
        if (!Objects.isNull(ddrOverrideId))
            query += " AND dd.id !=" + ddrOverrideId;

        return query;
    }
}

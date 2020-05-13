package com.cogent.cogentappointment.admin.query.ddrShiftWise;

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
                    " dd.shift.id as shiftId,"+                                         //[4]
                    " dd.shift.name as shiftName," +                                    //[5]
                    " dd.offStatus as offStatus," +                                     //[6]
                    " dd.rosterGapDuration as rosterGapDuration," +                     //[7]
                    " dd.hasBreak as hasBreak," +                                       //[8]
                    " dd.remarks as remarks" +                                          //[9]
                    " FROM DDROverrideDetail dd" +
                    " WHERE dd.status = 'Y'" +
                    " AND dd.ddrShiftWise.status != 'D'" +
                    " AND dd.ddrShiftWise.id = :ddrId";
}

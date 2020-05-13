package com.cogent.cogentappointment.admin.query.ddrShiftWise;

/**
 * @author smriti on 13/05/20
 */
public class DDRWeekDaysDetailQuery {

    public static final String QUERY_TO_FETCH_DDR_WEEK_DAYS_DETAIL =
            " SELECT" +
                    " dw.id as ddrWeekDaysId," +                    //[0]
                    " dw.startTime as startTime," +                 //[1]
                    " dw.endTime as endTime," +                     //[2]
                    " dw.offStatus as offStatus," +                 //[3]
                    " dw.hasBreak as hasBreak," +                   //[4]
                    " dw.weekDays.id as weekDaysId," +              //[5]
                    " dw.weekDays.name as weekDaysName" +           //[6]
                    " FROM DoctorDutyRosterShiftWise ddr" +
                    " LEFT JOIN DDRShiftDetail dsd ON ddr.id = dsd.ddrShiftWise.id" +
                    " LEFT JOIN DDRWeekDaysDetail dw ON dsd.id = dw.ddrShiftDetail.id" +
                    " WHERE ddr.status !='D'" +
                    " AND dsd.status = 'Y'" +
                    " AND ddr.id = :ddrId" +
                    " AND dsd.shift.id = :shiftId";
}

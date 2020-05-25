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

    /*USED FOR COMPARISON WHILE UPDATING DDR WEEKDAYS INFO*/
    public static String QUERY_TO_FETCH_WEEK_DAYS_TIME_DETAIL(String weekDaysIds, String shiftDetailIds) {
        return "SELECT" +
                " dw.weekDays.id as weekDaysId," +                  //[0]
                " dw.weekDays.name as weekDaysName," +              //[1]
                " dw.startTime as startTime," +                     //[2]
                " dw.endTime as endTime," +                         //[3]
                " ds.shift.name as shiftName" +                     //[4]
                " FROM DDRWeekDaysDetail dw" +
                " LEFT JOIN DDRShiftDetail ds ON ds.id = dw.ddrShiftDetail.id" +
                " LEFT JOIN WeekDays w ON w.id = dw.weekDays.id" +
                " WHERE" +
                " dw.offStatus = 'N'" +
                " AND w.id IN (" + weekDaysIds + ")" +
                " AND ds.id IN (" + shiftDetailIds + ")" +
                " AND ds.status = 'Y'";
    }
}

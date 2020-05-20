package com.cogent.cogentappointment.admin.query.ddrShiftWise;

import java.util.List;

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
    public static String QUERY_TO_FETCH_WEEK_DAYS_TIME_DETAIL(List<Long> weekDaysIds, List<Long> shiftDetailIds) {

        return "SELECT" +
                " dw.weekDays.id as weekDaysId," +                  //[0]
                " dw.weekDays.name as weekDaysName," +               //[1]
                " dw.startTime as startTime," +                     //[2]
                " dw.endTime as endTime" +                          //[3]
                " FROM DDRWeekDaysDetail dw" +
                " WHERE" +
                " dw.offStatus = 'N'" +
                " dw.weekDays.id IN (" + weekDaysIds + ")" +
                " AND dw.ddrShiftDetail.id IN (" + shiftDetailIds + ")";
    }
}

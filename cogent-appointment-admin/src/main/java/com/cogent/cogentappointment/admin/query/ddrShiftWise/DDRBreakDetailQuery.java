package com.cogent.cogentappointment.admin.query.ddrShiftWise;

/**
 * @author smriti on 14/05/20
 */
public class DDRBreakDetailQuery {

    public static String QUERY_TO_FETCH_BREAK_DETAIL =
            " SELECT" +
                    " dd.startTime as startTime," +                 //[0]
                    " dd.endTime as endTime," +                     //[1]
                    " dd.breakType.id as breakTypeId," +            //[2]
                    " dd.breakType.name as breakTypeName," +        //[3]
                    " dd.remarks as remarks" +                      //[4]
                    " FROM DDRBreakDetail dd" +
                    " WHERE dd.status = 'Y'" +
                    " AND dd.ddrWeekDaysDetail.id = :ddrWeekDaysId";
}

package com.cogent.cogentappointment.admin.query.ddrShiftWise;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.override.DDROverrideBreakUpdateRequestDTO;

import java.util.List;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeFromDateIn24HrFormat;

/**
 * @author smriti on 14/05/20
 */
public class DDROverrideBreakDetailQuery {

    public static String QUERY_TO_FETCH_EXISTING_OVERRIDE_BREAK_DETAIL =
            " SELECT" +
                    " dd.startTime as startTime," +                 //[0]
                    " dd.endTime as endTime," +                     //[1]
                    " dd.breakType.id as breakTypeId," +            //[2]
                    " dd.breakType.name as breakTypeName," +        //[3]
                    " dd.remarks as remarks" +                      //[4]
                    " FROM DDROverrideBreakDetail dd" +
                    " WHERE dd.status = 'Y'" +
                    " AND dd.ddrOverrideDetail.id = :ddrOverrideId";

    public static String QUERY_TO_FETCH_OVERRIDE_BREAK_DETAIL =
            " SELECT" +
                    " dd.id as ddrOverrideBreakId," +               //[0]
                    " dd.startTime as startTime," +                 //[1]
                    " dd.endTime as endTime," +                     //[2]
                    " dd.breakType.id as breakTypeId," +            //[3]
                    " dd.breakType.name as breakTypeName," +        //[4]
                    " dd.remarks as remarks" +                      //[5]
                    " FROM DDROverrideBreakDetail dd" +
                    " WHERE dd.status = 'Y'" +
                    " AND dd.ddrOverrideDetail.id = :ddrOverrideId";

    public static String QUERY_TO_FETCH_DDR_OVERRIDE_BREAK_COUNT(
            List<DDROverrideBreakUpdateRequestDTO> breakUpdateRequestDTOS) {

        String query = " SELECT" +
                " COUNT(dd.id) " +
                " FROM DDROverrideBreakDetail dd" +
                " WHERE" +
                " dd.status = 'Y'" +
                " AND dd.ddrOverrideDetail.id=:ddrOverrideId";

        boolean isFirstRequestAdded = false;

        for (DDROverrideBreakUpdateRequestDTO breakDetails : breakUpdateRequestDTOS) {

            if (!isFirstRequestAdded) {
                query += " AND (DATE_FORMAT(dd.endTime,'%H:%i') >'" + getTimeFromDateIn24HrFormat(breakDetails.getStartTime()) + "'" +
                        " AND DATE_FORMAT(dd.startTime,'%H:%i')<'" + getTimeFromDateIn24HrFormat(breakDetails.getEndTime()) + "'";

                isFirstRequestAdded = true;
            } else {
                query += " OR (DATE_FORMAT(dd.endTime,'%H:%i') >'" + getTimeFromDateIn24HrFormat(breakDetails.getStartTime()) + "'" +
                        " AND DATE_FORMAT(dd.startTime,'%H:%i')<'" + getTimeFromDateIn24HrFormat(breakDetails.getEndTime()) + "'";
            }
        }

        return query;
    }

}

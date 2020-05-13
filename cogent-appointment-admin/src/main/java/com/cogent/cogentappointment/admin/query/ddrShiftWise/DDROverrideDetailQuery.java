package com.cogent.cogentappointment.admin.query.ddrShiftWise;

/**
 * @author smriti on 13/05/20
 */
public class DDROverrideDetailQuery {

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_DETAIL =
            " SELECT" +
                    " dd.id as ddrOverrideId," +
                    " dd.date as date," +
                    " DATE_FORMAT(dd.startTime, '%h:%i %p') as startTime," +
                    " DATE_FORMAT(dd.endTime, '%h:%i %p') as endTime," +
                    " dd.shift.name as shiftName," +
                    " dd.offStatus as offStatus," +
                    " dd.rosterGapDuration as rosterGapDuration," +
                    " dd.remarks as remarks" +
                    " FROM DDROverrideDetail dd" +
                    " WHERE dd.status = 'Y'" +
                    " AND dd.ddrShiftWise.status != 'D'" +
                    " AND dd.ddrShiftWise.id = :ddrId";
}

package com.cogent.cogentappointment.admin.query.ddrShiftWise;

/**
 * @author smriti on 13/05/20
 */
public class DDRShiftDetailQuery {

    public static final String QUERY_TO_FETCH_EXISTING_SHIFT_DETAIL =
            " SELECT" +
                    " ds.shift.id as shiftId," +
                    " ds.shift.name as shiftName," +
                    " ds.rosterGapDuration as rosterGapDuration" +
                    " FROM DDRShiftDetail ds " +
                    " WHERE ds.status = 'Y'" +
                    " AND ds.ddrShiftWise.status != 'D'" +
                    " AND ds.ddrShiftWise.id =:ddrId";

}

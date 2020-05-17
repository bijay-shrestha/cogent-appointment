package com.cogent.cogentappointment.admin.query.ddrShiftWise;

/**
 * @author smriti on 13/05/20
 */
public class DDRShiftDetailQuery {

    public static final String QUERY_TO_FETCH_MIN_SHIFT_INFO =
            " SELECT" +
                    " ds.shift.id as shiftId," +                            //[0]
                    " ds.shift.name as shiftName," +                        //[1]
                    " ds.rosterGapDuration as rosterGapDuration" +          //[2]
                    " FROM DDRShiftDetail ds " +
                    " WHERE ds.status = 'Y'" +
                    " AND ds.ddrShiftWise.status != 'D'" +
                    " AND ds.ddrShiftWise.id =:ddrId";

    public static String QUERY_TO_FETCH_DDR_SHIFT_COUNT(String shiftIds,
                                                        Long ddrId) {

        return "SELECT" +
                " COUNT(ds.id)" +
                " FROM DDRShiftDetail ds" +
                " WHERE ds.ddrShiftWise.id = " + ddrId +
                " AND ds.shift.id IN (" + shiftIds + ")" +
                " AND ds.status = 'Y'" +
                " AND ds.ddrShiftWise.status != 'D'";
    }

    public static final String QUERY_TO_FETCH_SHIFT_DETAILS =
            " SELECT" +
                    " ds.id as ddrShiftDetailId," +                             //[0]
                    " ds.shift.id as shiftId," +                                //[1]
                    " ds.shift.name as shiftName," +                            //[2]
                    " ds.rosterGapDuration as rosterGapDuration" +              //[3]
                    " FROM DDRShiftDetail ds " +
                    " WHERE ds.status = 'Y'" +
                    " AND ds.ddrShiftWise.status != 'D'" +
                    " AND ds.ddrShiftWise.id =:ddrId";

}

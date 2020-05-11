package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 06/05/20
 */
public class ShiftQuery {

    public static final String QUERY_TO_FETCH_SHIFT_BY_HOSPITAL_ID =
            "SELECT" +
                    " s.id as value," +
                    " s.name as label" +
                    " FROM Shift s" +
                    " LEFT JOIN Hospital h ON h.id =s.hospital.id" +
                    " WHERE s.status = 'Y'" +
                    " AND h.status = 'Y'" +
                    " AND h.id=:hospitalId" +
                    " ORDER BY label ASC";

    public static String QUERY_TO_FETCH_NAME_BY_IDS(String shiftIds) {
        return " SELECT" +
                " GROUP_CONCAT(s.name)" +
                " FROM " +
                " shift s" +
                " WHERE s.id IN (" + shiftIds + ")" +
                " AND s.status = 'Y'";
    }
}

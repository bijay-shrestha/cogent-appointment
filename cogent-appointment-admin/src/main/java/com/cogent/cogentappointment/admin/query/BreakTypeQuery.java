package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 06/05/20
 */
public class BreakTypeQuery {

    public static final String QUERY_TO_FETCH_BREAK_TYPE_BY_HOSPITAL_ID =
            "SELECT" +
                    " b.id as value," +
                    " b.name as label" +
                    " FROM BreakType b" +
                    " LEFT JOIN Hospital h ON h.id =b.hospital.id" +
                    " WHERE b.status = 'Y'" +
                    " AND h.status = 'Y'" +
                    " AND h.id=:hospitalId" +
                    " ORDER BY label ASC";
}

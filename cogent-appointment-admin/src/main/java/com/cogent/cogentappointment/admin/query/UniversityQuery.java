package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 08/11/2019
 */
public class UniversityQuery {
    public static final String QUERY_TO_FETCH_ACTIVE_UNIVERSITY =
            "SELECT" +
                    " c.id as value," +
                    " c.name as label" +
                    " FROM University c" +
                    " WHERE c.status = 'Y'";
}

package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 08/11/2019
 */
public class UniversityQuery {
    public static final String QUERY_TO_FETCH_ACTIVE_UNIVERSITY =
            "SELECT" +
                    " u.id as value," +
                    " u.name as label" +
                    " FROM University u" +
                    " WHERE u.status = 'Y'";
}

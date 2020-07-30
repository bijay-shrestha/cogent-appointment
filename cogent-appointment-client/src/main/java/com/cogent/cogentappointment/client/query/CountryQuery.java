package com.cogent.cogentappointment.client.query;

/**
 * @author smriti on 08/11/2019
 */
public class CountryQuery {

    public static final String QUERY_TO_FETCH_ACTIVE_COUNTRY =
            "SELECT" +
                    " c.id as value," +
                    " c.name as label" +
                    " FROM Country c" +
                    " WHERE c.status = 'Y'";

    public static final String QUERY_TO_FETCH_COUNTRY =
            "SELECT" +
                    " c.id as value," +
                    " c.name as label" +
                    " FROM Country c" +
                    " WHERE c.status != 'D'";
}

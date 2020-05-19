package com.cogent.cogentappointment.admin.query;

/**
 * @author rupak on 2020-05-19
 */
public class IntegrationQuery {


    public static final String QUERY_TO_FETCH_MIN_FEATURES =
                    "SELECT" +
                    " f.id as value," +
                    " f.name as label" +
                    " FROM" +
                    " Feature f" +
                    " WHERE f.status ='Y'" +
                    " ORDER by f.name ASC";

    public static final String QUERY_TO_FETCH_MIN_REQUEST_METHODS =
            "SELECT" +
                    " hrm.id as value," +
                    " hrm.name as label" +
                    " FROM" +
                    " HttpRequestMethod hrm" +
                    " WHERE hrm.status ='Y'" +
                    " ORDER by hrm.name ASC";
}

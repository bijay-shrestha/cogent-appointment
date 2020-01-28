package com.cogent.cogentappointment.client.query;

/**
 * @author smriti on 11/11/2019
 */
public class QualificationAliasQuery {

    public static final String QUERY_TO_FETCH_ACTIVE_QUALIFICATION_ALIAS =
            "SELECT" +
                    " q.id as value," +
                    " q.name as label" +
                    " FROM QualificationAlias q" +
                    " WHERE q.status = 'Y'";
}

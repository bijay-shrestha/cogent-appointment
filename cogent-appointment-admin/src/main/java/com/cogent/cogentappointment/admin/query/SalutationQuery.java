package com.cogent.cogentappointment.admin.query;

public class SalutationQuery {

    public static final String QUERY_TO_FETCH_ACTIVE_SALUTATION_FOR_DROPDOWN =
            " SELECT" +
                    " sa.id as value," +
                    " sa.code as label" +
                    " FROM" +
                    " Salutation sa " +
                    " WHERE sa.status='Y'";
}

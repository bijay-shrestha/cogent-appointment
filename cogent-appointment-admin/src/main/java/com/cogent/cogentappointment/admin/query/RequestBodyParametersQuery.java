package com.cogent.cogentappointment.admin.query;

/**
 * @author rupak ON 2020/05/29-11:00 AM
 */
public class RequestBodyParametersQuery {
    public static final String QUERY_TO_FETCH_MIN_REQUEST_BODY_PARAMTERS =
            "SELECT" +
                    " airbp.id as value," +
                    " airbp.name as label" +
                    " FROM" +
                    " ApiIntegrationRequestBodyParameters airbp" +
                    " WHERE airbp.status ='Y'" +
                    " ORDER by airbp.name ASC";

    public static final String FETCH_REQUEST_BODY_ATTRIBUTE_BY_FEATURE_ID =

            "SELECT" +
                    " airbp.id as id," +
                    " airbp.name as name" +
                    " FROM" +
                    " ApiIntegrationRequestBodyParameters airbp" +
                    " LEFT JOIN ApiFeatureIntegrationRequestBodyParameters afirbp ON afirbp.requestBodyParametersId=airbp.id" +
                    " WHERE airbp.status ='Y'" +
                    " AND afirbp.status ='Y'" +
                    " AND afirbp.featureId.id=:featureId" +
                    " ORDER by airbp.name ASC";

    public static String FETCH_REQUEST_BODY_ATTRIBUTE_BY_ID(String ids) {

        String query = "SELECT" +
                " airbp"+
                " FROM" +
                " ApiIntegrationRequestBodyParameters airbp" +
                " WHERE airbp.status ='Y'" +
                " AND airbp.id IN ("+ids+")" +
                " ORDER by airbp.name ASC";

        return query;

    }
}

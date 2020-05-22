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

    public static final String ADMIN_MODE_FEAUTRES_INTEGRATION_API_QUERY =
            "SELECT" +
                    " aif.id as apiIntegrationFormatId," +
                    " f.code as featureCode," +
                    " hrm.name as requestMethod,"+
                    " aif.url as url," +
                    " aif.httpRequestBodyAttributes as requestBody" +
                    " from AdminModeFeatureIntegration amfi"+
                    " LEFT JOIN ApiFeatureIntegration afi ON afi.clientFeatureIntegrationId=amfi.id" +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=afi.apiIntegrationFormatId" +
                    " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId" +
                    " WHERE amfi.appointmentModeId= :appointmentModeId"+
                    " AND aif.status='Y'"+
                    " AND hrm.status='Y'"+
                    " AND afi.status='Y'"+
                    " AND f.status='Y'"+
                    " AND amfi.status='Y'";


    public static final String CLIENT_API_FEAUTRES_HEADERS_QUERY =
            " SELECT " +
                    " arh.keyName as keyName," +
                    " arh.value as keyValue" +
                    " FROM ClientFeatureIntegration cfi" +
                    " LEFT JOIN Feature f ON f.id=cfi.featureId" +
                    " LEFT JOIN ApiFeatureIntegration afi ON afi.clientFeatureIntegrationId=cfi.id" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=afi.apiIntegrationFormatId" +
                    " LEFT JOIN ApiRequestHeader arh ON arh.apiIntegrationFormatId=aif.id" +
                    " WHERE aif.id=:apiIntegrationFormatId"+
                    " AND aif.status='Y'"+
                    " AND arh.status='Y'"+
                    " AND afi.status='Y'"+
                    " AND cfi.status='Y'";

    public static final String CLIENT_API_PARAMETERS_QUERY =
            " SELECT " +
                    " aqp.param as param," +
                    " aqp.value as value" +
                    " FROM ClientFeatureIntegration cfi" +
                    " LEFT JOIN ApiFeatureIntegration afi ON afi.clientFeatureIntegrationId=cfi.id" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=afi.apiIntegrationFormatId" +
                    " LEFT JOIN ApiQueryParameters aqp ON aqp.apiIntegrationFormatId =aif.id"+
                    " WHERE aif.id=:apiIntegrationFormatId" +
                    " AND aif.status='Y'"+
                    " AND afi.status='Y'"+
                    " AND aqp.status='Y'"+
                    " AND cfi.status='Y'";

}

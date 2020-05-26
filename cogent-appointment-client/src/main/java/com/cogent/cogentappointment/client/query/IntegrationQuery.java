package com.cogent.cogentappointment.client.query;

/**
 * @author rupak on 2020-05-19
 */
public class IntegrationQuery {

    public static final String CLIENT_FEAUTRES_INTEGRATION_API_QUERY =
            "SELECT" +
                    " aif.id as apiIntegrationFormatId," +
                    " f.code as featureCode," +
                    " hrm.name as requestMethod,"+
                    " aif.url as url," +
                    " aif.httpRequestBodyAttributes as requestBody" +
                    " from ClientFeatureIntegration cfi" +
                    " LEFT JOIN ApiFeatureIntegration afi ON afi.clientFeatureIntegrationId=cfi.id" +
                    " LEFT JOIN Feature f ON f.id=cfi.featureId" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=afi.apiIntegrationFormatId" +
                    " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId" +
                    " WHERE cfi.hospitalId= :hospitalId"+
                    " AND aif.status='Y'"+
                    " AND hrm.status='Y'"+
                    " AND afi.status='Y'"+
                    " AND f.status='Y'"+
                    " AND cfi.status='Y'";


    public static final String CLIENT_API_FEAUTRES_HEADERS_QUERY =
            " SELECT " +
                    " arh.keyName as keyParam," +
                    " arh.value as valueParam" +
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
                    " aqp.param as keyParam," +
                    " aqp.value as valueParam" +
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

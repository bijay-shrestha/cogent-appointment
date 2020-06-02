package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiIntegrationSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author rupak on 2020-05-19
 */
public class IntegrationQuery {

    public static final String VALIDATE_HOSPITAL_REQUEST_METHOD_AND_FEATURE =
            " SELECT" +
                    " cfi.id" +
                    " FROM ClientFeatureIntegration cfi" +
                    " LEFT JOIN ApiFeatureIntegration afi ON afi.clientFeatureIntegrationId=cfi.id" +
                    " LEFT JOIN Feature f ON f.id=cfi.featureId" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=afi.apiIntegrationFormatId" +
                    " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId" +
                    " WHERE f.id=:featureId" +
                    " AND hrm.id=:requestMethodId" +
                    " AND cfi.hospitalId=:hospitalId";


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

    public static final String ADMIN_MODE_FEATURES_INTEGRATION_API_QUERY =
            " SELECT" +
                    " aif.id as apiIntegrationFormatId," +
                    " f.code as featureCode," +
                    " hrm.name as requestMethod," +
                    " aif.url as url" +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId" +
                    " WHERE amfi.appointmentModeId.id=:appointmentModeId" +
                    " AND aif.status='Y'" +
                    " AND hrm.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND f.status='Y'" +
                    " AND amfi.status='Y'";


    public static final String CLIENT_API_FEATURES_HEADERS_QUERY =
            " SELECT " +
                    " arh.id as id," +
                    " arh.keyName as keyParam," +
                    " arh.value as valueParam," +
                    " arh.description as description," +
                    " arh.status as status" +
                    " FROM ClientFeatureIntegration cfi" +
                    " LEFT JOIN Feature f ON f.id=cfi.featureId" +
                    " LEFT JOIN ApiFeatureIntegration afi ON afi.clientFeatureIntegrationId=cfi.id" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=afi.apiIntegrationFormatId" +
                    " LEFT JOIN ApiRequestHeader arh ON arh.apiIntegrationFormatId=aif.id" +
                    " WHERE f.id=:featureId" +
                    " AND aif.status='Y'" +
                    " AND arh.status='Y'" +
                    " AND afi.status='Y'" +
                    " AND cfi.status='Y'";

    public static final String CLIENT_API_PARAMETERS_QUERY =
            " SELECT " +
                    " aqp.id as id," +
                    " aqp.param as keyParam," +
                    " aqp.value as valueParam," +
                    " aqp.status as status," +
                    " aqp.description as description" +
                    " FROM ClientFeatureIntegration cfi" +
                    " LEFT JOIN ApiFeatureIntegration afi ON afi.clientFeatureIntegrationId=cfi.id" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=afi.apiIntegrationFormatId" +
                    " LEFT JOIN ApiQueryParameters aqp ON aqp.apiIntegrationFormatId =aif.id" +
                    " LEFT JOIN Feature f ON f.id=cfi.featureId" +
                    " WHERE f.id=:featureId" +
                    " AND aif.status='Y'" +
                    " AND afi.status='Y'" +
                    " AND aqp.status='Y'" +
                    " AND cfi.status='Y'";

    public static final String ADMIN_MODE_API_FEAUTRES_HEADERS_QUERY =
            " SELECT " +
                    " arh.keyName as keyParam," +
                    " arh.value as valueParam" +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN ApiRequestHeader arh ON arh.apiIntegrationFormatId=aif.id" +
                    " WHERE aif.id=:apiIntegrationFormatId" +
                    " AND aif.status='Y'" +
                    " AND arh.status='Y'" +
                    " AND amfi.status='Y'" +
                    " AND amafi.status='Y'";

    public static final String ADMIN_MODE_API_PARAMETERS_QUERY =
            " SELECT " +
                    " aqp.param as keyParam," +
                    " aqp.value as valueParam" +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN ApiQueryParameters aqp ON aqp.apiIntegrationFormatId =aif.id" +
                    " WHERE aif.id=:apiIntegrationFormatId" +
                    " AND aif.status='Y'" +
                    " AND amfi.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND aqp.status='Y'";

    public static final String CLIENT_FEATURES_INTEGRATION_DETAILS_API_QUERY =
            "SELECT" +
                    " f.id as featureId," +
                    " h.name as hospitalName," +
                    " f.code as featureCode," +
                    " hrm.id as requestMethodId," +
                    " hrm.name as requestMethodName," +
                    " aif.url as url," +
                    " ic.id as integrationChannelId," +
                    " ic.name as integrationChannel," +
                    " ait.id as integrationTypeId," +
                    " ait.name as integrationType" +
                    " from ClientFeatureIntegration cfi" +
                    " LEFT JOIN ApiFeatureIntegration afi ON afi.clientFeatureIntegrationId=cfi.id" +
                    " LEFT JOIN Feature f ON f.id=cfi.featureId" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=afi.apiIntegrationFormatId" +
                    " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId" +
                    " LEFT JOIN Hospital h ON h.id=cfi.hospitalId" +
                    " LEFT JOIN IntegrationChannel ic ON ic.id=cfi.integrationChannelId.id" +
                    " LEFT JOIN ApiIntegrationType ait ON ait.id=f.apiIntegrationTypeId.id" +
                    " WHERE cfi.id= :clientFeatureIntegrationId" +
                    " AND aif.status='Y'" +
                    " AND hrm.status='Y'" +
                    " AND afi.status='Y'" +
                    " AND f.status='Y'" +
                    " AND cfi.status='Y'" +
                    " AND ic.status='Y'" +
                    " AND ait.status='Y'";

    public static final String QUERY_TO_FETCH_MIN_API_INTEGRATION_TYPE =
            "SELECT" +
                    " ait.id as value," +
                    " ait.name as label" +
                    " FROM" +
                    " ApiIntegrationType ait" +
                    " WHERE ait.status ='Y'" +
                    " ORDER by ait.name ASC";

    public static final String QUERY_TO_FETCH_MIN_API_INTEGRATION_TYPE_BY_INTEGRATION_TYPE_ID =
            "SELECT" +
                    " f.id as value," +
                    " f.name as label" +
                    " FROM" +
                    " Feature f" +
                    " LEFT JOIN ApiIntegrationType ait ON ait.id=f.apiIntegrationTypeId.id" +
                    " WHERE ait.status ='Y'" +
                    " AND f.status ='Y'" +
                    " AND ait.id=:apiIntegrationTypeId" +
                    " ORDER by ait.name ASC";
    public static final String QUERY_TO_FETCH_MIN_INTEGRATION_CHANNEL =
            "SELECT" +
                    " ic.id as value," +
                    " ic.name as label" +
                    " FROM" +
                    " IntegrationChannel ic" +
                    " WHERE ic.status ='Y'" +
                    " ORDER by ic.name ASC";


    public static Function<ClientApiIntegrationSearchRequestDTO, String> CLIENT_API_INTEGRATION_SEARCH_QUERY =
            (searchRequestDTO) ->
                    " SELECT" +
                            " cfi.id as id," +
                            " h.name as hospitalName," +
                            " f.name as featureName," +
                            " f.code as featureCode," +
                            " hrm.name as requestMethod," +
                            " aif.url as url" +
                            " FROM ClientFeatureIntegration cfi" +
                            " LEFT JOIN Hospital h ON h.id=cfi.hospitalId" +
                            " LEFT JOIN Feature f ON f.id=cfi.featureId" +
                            " LEFT JOIN ApiIntegrationType ait ON ait.id=f.apiIntegrationTypeId.id" +
                            " LEFT JOIN ApiFeatureIntegration afi ON afi.clientFeatureIntegrationId=cfi.id" +
                            " LEFT JOIN ApiIntegrationFormat aif ON aif.id=afi.apiIntegrationFormatId" +
                            " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId"
                            + GET_WHERE_CLAUSE_TO_SEARCH_CLIENT_API_INTEGRATION(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_CLIENT_API_INTEGRATION(
            ClientApiIntegrationSearchRequestDTO requestSearchDTO) {

        String whereClause = " WHERE" +
                " aif.status='Y'" +
                " AND afi.status='Y'" +
                " AND cfi.status='Y'";

        if (!Objects.isNull(requestSearchDTO.getHospitalId()))
            whereClause += " AND cfi.hospitalId=" + requestSearchDTO.getHospitalId();

        if (!Objects.isNull(requestSearchDTO.getFeatureTypeId()))
            whereClause += " AND cfi.featureId=" + requestSearchDTO.getFeatureTypeId();

        if (!Objects.isNull(requestSearchDTO.getRequestMethodId()))
            whereClause += " AND hrm.id=" + requestSearchDTO.getRequestMethodId();

        if (!Objects.isNull(requestSearchDTO.getApiIntegrationTypeId()))
            whereClause += " AND ait.id=" + requestSearchDTO.getApiIntegrationTypeId();

        if (!ObjectUtils.isEmpty(requestSearchDTO.getUrl()))
            whereClause += " AND aif.url like %'" + requestSearchDTO.getUrl() + "'%";


        return whereClause;
    }

}

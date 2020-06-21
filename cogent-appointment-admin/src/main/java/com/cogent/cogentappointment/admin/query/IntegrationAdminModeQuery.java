package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author rupak ON 2020/06/03-10:42 AM
 */
public class IntegrationAdminModeQuery {

    public static final String ADMIN_MODE_INTEGRATION_DETAILS_API_QUERY =
            "SELECT" +
                    " f.id as featureId," +
                    " aif.id as apiIntegrationFormatId," +
                    " h.id as hospitalId," +
                    " h.name as hospitalName," +
                    " am.id as appointmentModeId," +
                    " am.name as appointmentModeName," +
                    " f.name as featureName," +
                    " hrm.id as requestMethodId," +
                    " hrm.name as requestMethodName," +
                    " aif.url as url," +
                    " ic.id as integrationChannelId," +
                    " ic.name as integrationChannel," +
                    " ait.id as integrationTypeId," +
                    " ait.name as integrationType," +
                    " amfi.status as status," +
                    ADMIN_MODE_API_INTEGRATION_AUDITABLE_QUERY() +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN AppointmentMode am ON am.id=amfi.appointmentModeId.id" +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId" +
                    " LEFT JOIN IntegrationChannel ic ON ic.id=amfi.integrationChannelId.id" +
                    " LEFT JOIN ApiIntegrationType ait ON ait.id=f.apiIntegrationTypeId.id" +
                    " LEFT JOIN Hospital h ON h.id=amfi.hospitalId.id" +
                    " WHERE amfi.id= :adminModeFeatureIntegrationId" +
                    " AND aif.status='Y'" +
                    " AND hrm.status='Y'" +
                    " AND amfi.status!='D'" +
                    " AND amafi.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND f.status='Y'" +
                    " AND ic.status='Y'" +
                    " AND ait.status='Y'";

    public static final String ADMIN_MODE_FEATURES_HEADERS_QUERY =
            " SELECT " +
                    " amrh.id as id," +
                    " amrh.keyName as keyParam," +
                    " amrh.value as valueParam," +
                    " amrh.description as description," +
                    " amrh.status as status" +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN AdminModeRequestHeader amrh ON amrh.apiIntegrationFormatId=aif.id" +
                    " WHERE aif.id=:apiIntegrationFormatId" +
                    " AND amfi.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND aif.status='Y'" +
                    " AND amrh.status='Y'" +
                    " AND f.status='Y'";

    public static final String ADMIN_MODE_FEATURES_HEADERS_DETAILS_QUERY =
            " SELECT " +
                    " amrh.keyName as keyParam," +
                    " amrh.value as valueParam," +
                    " amrh.description as description" +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN AdminModeRequestHeader amrh ON amrh.apiIntegrationFormatId=aif.id" +
                    " WHERE aif.id=:apiIntegrationFormatId" +
                    " AND amfi.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND aif.status='Y'" +
                    " AND amrh.status='Y'" +
                    " AND f.status='Y'";

    public static final String ADMIN_MODE_QUERY_PARAMETERS_QUERY =
            " SELECT " +
                    " amqp.id as id," +
                    " amqp.param as keyParam," +
                    " amqp.value as valueParam," +
                    " amqp.status as status," +
                    " amqp.description as description" +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN AdminModeQueryParameters amqp ON amqp.apiIntegrationFormatId =aif.id" +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " WHERE aif.id=:apiIntegrationFormatId" +
                    " AND amfi.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND amqp.status='Y'" +
                    " AND f.status='Y'";

    public static final String ADMIN_MODE_QUERY_PARAMETERS_DETAILS_QUERY =
            " SELECT " +
                    " amqp.param as keyParam," +
                    " amqp.value as valueParam," +
                    " amqp.description as description" +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN AdminModeQueryParameters amqp ON amqp.apiIntegrationFormatId =aif.id" +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " WHERE aif.id=:apiIntegrationFormatId" +
                    " AND amfi.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND amqp.status='Y'" +
                    " AND f.status='Y'";


    public static final String VALIDATE_ADMIN_MODE_REQUEST_METHOD_AND_FEATURE =
            " SELECT" +
                    " COUNT(amfi.id)" +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId" +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " WHERE amfi.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND hrm.status='Y'" +
                    " AND aif.status='Y'" +
                    " AND f.id=:featureId" +
                    " AND hrm.id=:requestMethodId" +
                    " AND amfi.appointmentModeId.id=:appointmentModeId";

    public static final String APPOINTMENT_MODE_FEATURES_INTEGRATION_API_QUERY =
            " SELECT" +
                    " aif.id as apiIntegrationFormatId," +
                    " amfi.appointmentModeId.id as appointmentModeId," +
                    " ic.code as integrationChannelCode," +
                    " f.id as featureId," +
                    " f.code as featureCode," +
                    " hrm.name as requestMethod," +
                    " aif.url as url" +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN IntegrationChannel ic ON ic.id=amfi.integrationChannelId.id" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId" +
                    " WHERE aif.status='Y'" +
                    " AND hrm.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND f.status='Y'" +
                    " AND amfi.status='Y'" +
                    " AND ic.status='Y'";

    public static final String APPOINTMENT_MODE_FEATURES_INTEGRATION_BACKEND_API_QUERY =
            APPOINTMENT_MODE_FEATURES_INTEGRATION_API_QUERY +
                    " AND amfi.hospitalId.id=:hospitalId" +
                    " AND f.code=:featureCode" +
                    " AND ic.code=:integrationChannelCode" +
                    " AND amfi.appointmentModeId.id=:appointmentModeId";

    public static Function<AdminModeApiIntegrationSearchRequestDTO, String> ADMIN_MODE_API_INTEGRATION_SEARCH_QUERY =
            (searchRequestDTO) ->
                    " SELECT" +
                            " amfi.id as id," +
                            " amfi.status as status," +
                            " h.name as hospitalName," +
                            " am.name as appointmentMode," +
                            " ic.name as integrationChannel," +
                            " f.name as featureName," +
                            " f.code as featureCode," +
                            " hrm.name as requestMethod," +
                            " aif.url as url" +
                            " FROM AdminModeFeatureIntegration amfi" +
                            " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                            " LEFT JOIN AppointmentMode am ON am.id=amfi.appointmentModeId.id" +
                            " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                            " LEFT JOIN ApiIntegrationType ait ON ait.id=f.apiIntegrationTypeId.id" +
                            " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                            " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId" +
                            " LEFT JOIN IntegrationChannel ic ON ic.id=amfi.integrationChannelId.id" +
                            " LEFT JOIN Hospital h ON h.id=amfi.hospitalId.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_ADMIN_MODE_API_INTEGRATION(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_ADMIN_MODE_API_INTEGRATION(
            AdminModeApiIntegrationSearchRequestDTO requestSearchDTO) {

        String whereClause = " WHERE" +
                " aif.status='Y'" +
                " AND hrm.status='Y'" +
                " AND amafi.status='Y'" +
                " AND f.status='Y'" +
                " AND amfi.status!='D'";

        if (!Objects.isNull(requestSearchDTO.getAppointmentModeId()))
            whereClause += " AND amfi.appointmentModeId.id=" + requestSearchDTO.getAppointmentModeId();

        if (!Objects.isNull(requestSearchDTO.getFeatureTypeId()))
            whereClause += " AND amfi.featureId=" + requestSearchDTO.getFeatureTypeId();

        if (!Objects.isNull(requestSearchDTO.getRequestMethodId()))
            whereClause += " AND hrm.id=" + requestSearchDTO.getRequestMethodId();

        if (!Objects.isNull(requestSearchDTO.getApiIntegrationTypeId()))
            whereClause += " AND ait.id=" + requestSearchDTO.getApiIntegrationTypeId();

        if (!ObjectUtils.isEmpty(requestSearchDTO.getUrl()))
            whereClause += " AND aif.url LIKE '%" + requestSearchDTO.getUrl() + "%'";

        if (!ObjectUtils.isEmpty(requestSearchDTO.getStatus()))
            whereClause += " AND amfi.status='" + requestSearchDTO.getStatus() + "'";

        return whereClause;
    }

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

    public static String ADMIN_MODE_API_INTEGRATION_AUDITABLE_QUERY() {
        return " amfi.createdBy as createdBy," +
                " amfi.createdDate as createdDate," +
                " amfi.lastModifiedBy as lastModifiedBy," +
                " amfi.lastModifiedDate as lastModifiedDate";
    }

}

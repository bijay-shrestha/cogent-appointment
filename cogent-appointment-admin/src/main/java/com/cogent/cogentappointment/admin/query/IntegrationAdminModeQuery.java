package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author rupak ON 2020/06/03-10:42 AM
 */
public class IntegrationAdminModeQuery {

    public static Function<AdminModeApiIntegrationSearchRequestDTO, String> ADMIN_MODE_API_INTEGRATION_SEARCH_QUERY =
            (searchRequestDTO) ->
                    " SELECT" +
                            " amfi.id as id," +
                            " h.name as hospitalName,"+
                            " f.name as featureName,"+
                            " f.code as featureCode," +
                            " hrm.name as requestMethod," +
                            " aif.url as url" +
                            " FROM AdminModeFeatureIntegration amfi" +
                            " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                           " LEFT JOIN AppointmentModeHospitalInfo amhi ON amhi.appointmentModeId.id=amfi.appointmentModeId.id"+
                            " LEFT JOIN Hospital h ON h.id=amhi.hospitalId.id" +
                            " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                            " LEFT JOIN ApiIntegrationType ait ON ait.id=f.apiIntegrationTypeId.id" +
                            " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                            " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId"
                            + GET_WHERE_CLAUSE_TO_SEARCH_ADMIN_MODE_API_INTEGRATION(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_ADMIN_MODE_API_INTEGRATION(
            AdminModeApiIntegrationSearchRequestDTO requestSearchDTO) {

        String whereClause = " WHERE" +
                " aif.status='Y'" +
                " AND hrm.status='Y'" +
                " AND amafi.status='Y'" +
                " AND f.status='Y'" +
                " AND amfi.status='Y'";

//        if (!Objects.isNull(requestSearchDTO.getCompanyId()))
//            whereClause += " AND cfi.hospitalId=" + requestSearchDTO.getCompanyId();

        if (!Objects.isNull(requestSearchDTO.getAppointmentModeId()))
            whereClause += " AND amfi.appointmentModeId.id=" + requestSearchDTO.getAppointmentModeId();

        if (!Objects.isNull(requestSearchDTO.getFeatureTypeId()))
            whereClause += " AND amfi.featureId=" + requestSearchDTO.getFeatureTypeId();

        if (!Objects.isNull(requestSearchDTO.getRequestMethodId()))
            whereClause += " AND hrm.id=" + requestSearchDTO.getRequestMethodId();

        if (!Objects.isNull(requestSearchDTO.getApiIntegrationTypeId()))
            whereClause += " AND ait.id=" + requestSearchDTO.getApiIntegrationTypeId();

        if (!ObjectUtils.isEmpty(requestSearchDTO.getUrl()))
            whereClause += " AND aif.url like %'" + requestSearchDTO.getUrl() + "'%";


        return whereClause;
    }

    public static String ADMIN_MODE_API_INTEGRATION_AUDITABLE_QUERY() {
        return " cfi.createdBy as createdBy," +
                " cfi.createdDate as createdDate," +
                " cfi.lastModifiedBy as lastModifiedBy," +
                " cfi.lastModifiedDate as lastModifiedDate";
    }

}

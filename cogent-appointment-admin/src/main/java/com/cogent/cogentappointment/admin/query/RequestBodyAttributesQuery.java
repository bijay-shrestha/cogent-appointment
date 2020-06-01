package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute.ApiIntegrationRequestBodySearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author rupak ON 2020/05/29-11:00 AM
 */
public class RequestBodyAttributesQuery {
    public static final String QUERY_TO_FETCH_MIN_REQUEST_BODY_PARAMTERS =
            " SELECT" +
                    " airbp.id as value," +
                    " airbp.name as label" +
                    " FROM" +
                    " ApiIntegrationRequestBodyParameters airbp" +
                    " WHERE airbp.status ='Y'" +
                    " ORDER by airbp.name ASC";

    public static final String FETCH_REQUEST_BODY_ATTRIBUTE_BY_FEATURE_ID =
            " SELECT" +
                    " airbp.id as id," +
                    " airbp.name as name,"+
                    " FROM" +
                    " ApiFeatureIntegrationRequestBodyParameters afirbp" +
                    " LEFT JOIN ApiIntegrationRequestBodyParameters airbp " +
                    " ON airbp.id=afirbp.requestBodyParametersId.id" +
                    " WHERE airbp.status ='Y'" +
                    " AND afirbp.status ='Y'" +
                    " AND afirbp.featureId=:featureId";

//    public static final String FETCH_REQUEST_BODY_ATTRIBUTE_DETAILS =
//            " SELECT" +
//                    " f.id as featureId," +
//                    " f.name as featureName," +
//                    " GROUP_CONCAT(airbp.name) as requestBody," +
//                    " afirbp.status as status" +
//                    " FROM" +
//                    " feature f"+
//                    " LEFT JOIN api_feature_integration_request_body_parameters afirbp ON afirbp.feature_id=f.id" +
//                    " LEFT JOIN api_integration_request_body_parameters airbp ON airbp.id=afirbp.api_request_body_parameters_id"+
//                    " WHERE f.id=:"

    public static Function<ApiIntegrationRequestBodySearchRequestDTO, String>
            API_REQUEST_BODY_ATTRIBUTES_SEARCH_QUERY =
            (searchRequestDTO) ->
                    " SELECT" +
                            " f.id as featureId," +
                            " f.name as featureName," +
                            " GROUP_CONCAT(airbp.name) as requestBody," +
                            " afirbp.status as status"+
                            " FROM" +
                            " feature f" +
                            " LEFT JOIN api_feature_integration_request_body_parameters afirbp ON afirbp.feature_id=f.id" +
                            " LEFT JOIN api_integration_request_body_parameters airbp ON airbp.id=afirbp.api_request_body_parameters_id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_API_REQUEST_BODY_ATTRIBUTES(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_API_REQUEST_BODY_ATTRIBUTES(
            ApiIntegrationRequestBodySearchRequestDTO requestSearchDTO) {

        String whereClause = " WHERE f.status ='Y'" +
                "  AND afirbp.status='Y'";

        if (!Objects.isNull(requestSearchDTO.getFeatureTypeId()))
            whereClause += " AND f.id=" + requestSearchDTO.getFeatureTypeId();

        if (!Objects.isNull(requestSearchDTO.getRequestBodyId()))
            whereClause += " AND airbp.id=" + requestSearchDTO.getRequestBodyId();

        if (!ObjectUtils.isEmpty(requestSearchDTO.getStatus()))
            whereClause += " AND airbp.status='" + requestSearchDTO.getStatus() + "'";

        whereClause += " GROUP BY f.id";


        return whereClause;
    }

    public static String FETCH_REQUEST_BODY_ATTRIBUTE_BY_ID(String ids) {

        String query = "SELECT" +
                " airbp" +
                " FROM" +
                " ApiIntegrationRequestBodyParameters airbp" +
                " WHERE airbp.status ='Y'" +
                " AND airbp.id IN (" + ids + ")" +
                " ORDER by airbp.name ASC";

        return query;

    }

    public static String REQUEST_BODY_ATTRIBUTE_AUDITABLE_QUERY() {
        return " afirbp.created_by as createdBy," +
                " afirbp.created_date as createdDate," +
                " afirbp.last_modified_by as lastModifiedBy," +
                " afirbp.last_modified_date as lastModifiedDate";
    }
}

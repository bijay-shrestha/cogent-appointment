package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public class BillingModeQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT bm.name," +
                    " bm.code" +
                    " FROM BillingMode bm" +
                    " WHERE" +
                    " bm.status !='D'" +
                    " AND (bm.name=:name OR bm.code=:code)";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            " SELECT bm.name," +
                    " bm.code" +
                    " FROM  BillingMode bm" +
                    " WHERE" +
                    " bm.status !='D'" +
                    " AND bm.id!=:id" +
                    " AND (bm.name=:name OR bm.code=:code)";

    private static final String SELECT_CLAUSE_TO_FETCH_MINIMAL_BILLING_MODE =
            "SELECT bm.id as id," +
                    " bm.name as name," +
                    " bm.code as code," +
                    " bm.status as status," +
                    " bm.description as description" +
                    " FROM BillingMode bm";

    public static Function<BillingModeSearchRequestDTO, String> QUERY_TO_SEARCH_BILLING_MODE =
            (searchRequestDTO -> (
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_BILLING_MODE +
                            GET_WHERE_CLAUSE_FOR_SEARCHING_BILLING_MODE(searchRequestDTO)
            ));

    private static String GET_WHERE_CLAUSE_FOR_SEARCHING_BILLING_MODE
            (BillingModeSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE bm.status!='D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getCode()))
            whereClause += " AND bm.code = '" + searchRequestDTO.getCode() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getId()))
            whereClause += " AND bm.id=" + searchRequestDTO.getId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND bm.status='" + searchRequestDTO.getStatus() + "'";

        whereClause += "ORDER BY bm.id DESC";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_BILLING_MODE_DETAILS =
            "SELECT" +
                    " bm.id as id," +
                    " bm.name as name," +
                    " bm.code as code," +
                    " bm.status as status," +
                    " bm.remarks as remarks," +
                    " bm.description as description," +
                    BILLING_MODE_AUDITABLE_QUERY() +
                    " FROM BillingMode bm " +
                    " WHERE bm.status != 'D'" +
                    " AND bm.id =:id";

    public static final String QUERY_TO_FETCH_ACTIVE_BILLING_MODE_FOR_DROP_DOWN =
            "SELECT" +
                    " bm.id as value," +
                    " bm.name as label" +
                    " FROM BillingMode bm " +
                    " WHERE bm.status = 'Y'";

    public static final String QUERY_TO_FETCH_BILLING_MODE_FOR_DROP_DOWN =
            "SELECT" +
                    " bm.id as value," +
                    " bm.name as label" +
                    " FROM BillingMode bm " +
                    " WHERE bm.status != 'D'";

    public static String BILLING_MODE_AUDITABLE_QUERY() {
        return " bm.createdBy as createdBy," +
                " bm.createdDate as createdDate," +
                " bm.lastModifiedBy as lastModifiedBy," +
                " bm.lastModifiedDate as lastModifiedDate";
    }

    public static String QUERY_TO_GET_ACTIVE_BILLING_MODE_BY_HOSPITAL_ID =
            "SELECT " +
                    " bm" +
                    " FROM" +
                    " BillingMode bm" +
                    " LEFT JOIN HospitalBillingModeInfo hb ON hb.billingMode.id = bm.id AND hb.status='Y'" +
                    " WHERE" +
                    " bm.id =:billingModeId" +
                    " AND bm.status='Y'" +
                    " AND hb.hospital.id =:hospitalId";

    public static String QUERY_TO_GET_BILLING_MODE_BY_HOSPITAL_ID =
            "SELECT " +
                    " bm" +
                    " FROM" +
                    " BillingMode bm" +
                    " LEFT JOIN HospitalBillingModeInfo hb ON hb.billingMode.id = bm.id AND hb.status!='D'" +
                    " AND hb.status != 'D'" +
                    " WHERE" +
                    " bm.id =:billingModeId" +
                    " AND hb.hospital.id =:hospitalId";
}

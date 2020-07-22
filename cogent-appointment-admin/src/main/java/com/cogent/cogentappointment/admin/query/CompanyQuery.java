package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.company.CompanySearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author smriti ON 12/01/2020
 */
public class CompanyQuery {

    public static final String QUERY_TO_VALIDATE_COMPANY_DUPLICITY =
            "SELECT " +
                    " h.name," +                        //[0]
                    " h.code" +                         //[1]
                    " FROM Hospital h" +
                    " WHERE " +
                    " (h.name =:name OR h.code =:code)" +
                    " AND h.status != 'D'" +
                    " AND h.isCompany='Y'";

    public static final String QUERY_TO_VALIDATE_COMPANY_DUPLICITY_FOR_UPDATE =
            "SELECT " +
                    " h.name," +                        //[0]
                    " h.code" +                         //[1]
                    " FROM Hospital h" +
                    " WHERE " +
                    " h.id!=:id" +
                    " AND" +
                    " (h.name =:name OR h.code =:code)" +
                    " AND h.status != 'D'" +
                    " AND h.isCompany='Y'";

    public static final String QUERY_TO_FETCH_COMPANY_FOR_DROPDOWN =
            " SELECT" +
                    " h.id as value," +                     //[0]
                    " h.name as label," +                   //[1]
                    " h.isCompany as isCompany," +          //[2]
                    " h.code as companyCode" +               //[3]
                    " FROM" +
                    " Hospital h" +
                    " WHERE h.status ='Y'" +
                    " AND h.isCompany='Y'" +
                    " ORDER by h.name ASC";

    public static String QUERY_TO_SEARCH_COMPANY(CompanySearchRequestDTO searchRequestDTO) {
        return "SELECT" +
                " h.id as id," +                                //[0]
                " h.name as name," +                            //[1]
                " h.status as status," +                        //[2]
                " h.address as address," +                      //[3]
                " h.code as companyCode," +                     //[4]
                " CASE" +
                " WHEN" +
                " (hl.status is null OR hl.status = 'N')" +
                " THEN null" +
                " WHEN" +
                " hl.file_uri LIKE 'public%'" +
                " THEN" +
                " CONCAT(:cdnUrl,SUBSTRING_INDEX(hl.file_uri, 'public', -1))" +
                " ELSE" +
                " hl.file_uri" +
                " END as fileUri" +                             //[5]
                " FROM" +
                " hospital h" +
                " LEFT JOIN hospital_logo hl ON h.id = hl.hospital_id" +
                GET_WHERE_CLAUSE_FOR_SEARCHING_COMPANY.apply(searchRequestDTO);
    }

    private static Function<CompanySearchRequestDTO, String> GET_WHERE_CLAUSE_FOR_SEARCHING_COMPANY =
            (searchRequestDTO) -> {
                String whereClause = " WHERE h.is_company='Y' AND h.status!='D'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
                    whereClause += " AND h.status='" + searchRequestDTO.getStatus() + "'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getCompanyId()))
                    whereClause += " AND h.id =" + searchRequestDTO.getCompanyId();

                if (!ObjectUtils.isEmpty(searchRequestDTO.getCompanyCode()))
                    whereClause += " AND h.code LIKE '%" + searchRequestDTO.getCompanyCode() + "%'";

                whereClause += " ORDER BY h.id DESC";

                return whereClause;
            };

    public static final String QUERY_TO_FETCH_COMPANY_DETAILS =
            "SELECT" +
                    " h.id as id," +                                            //[0]
                    " h.name as name," +                                        //[1]
                    " h.status as status," +                                    //[2]
                    " h.address as address," +                                  //[3]
                    " h.pan_number as panNumber," +                             //[4]
                    " h.remarks as remarks," +                                  //[5]
                    " CASE" +
                    " WHEN" +
                    " (hl.status is null OR hl.status = 'N')" +
                    " THEN null" +
                    " WHEN" +
                    " hl.file_uri LIKE 'public%'" +
                    " THEN" +
                    " CONCAT(:cdnUrl,SUBSTRING_INDEX(hl.file_uri, 'public', -1))" +
                    " ELSE" +
                    " hl.file_uri" +
                    " END as companyLogo," +                                      //[6]
                    " h.code as companyCode," +                                //[7]
                    " tbl1.contact_details as contact_details," +               //[8]
                    " h.is_company as isCompany," +                             //[9]
                    " h.alias as alias," +                                       //10]
                    COMPANY_DETAILS_AUDITABLE_QUERY() +
                    " FROM" +
                    " hospital h" +
                    " LEFT JOIN hospital_logo hl ON h.id =hl.hospital_id " +
                    " LEFT JOIN hospital_banner hb ON h.id = hb.hospital_id" +
                    " LEFT JOIN " +
                    " (" +
                    " SELECT hc.hospital_id as hospitalId," +
                    " GROUP_CONCAT((CONCAT(hc.id, '/', hc.contact_number, '/', hc.status))) as contact_details" +
                    " FROM hospital_contact_number hc" +
                    " WHERE hc.status = 'Y'" +
                    " GROUP by hc.hospital_id" +
                    " )tbl1 ON tbl1.hospitalId = h.id" +
                    " WHERE h.id =:id" +
                    " AND h.is_company='Y'" +
                    " AND h.status !='D'" +
                    " AND h.is_company='Y'";

    public static String COMPANY_DETAILS_AUDITABLE_QUERY() {
        return " h.created_by as createdBy," +
                " h.created_date as createdDate," +
                " h.last_modified_by as lastModifiedBy," +
                " h.last_modified_date as lastModifiedDate";
    }


}

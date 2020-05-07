package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author smriti ON 12/01/2020
 */
public class HospitalQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            "SELECT " +
                    " h.name," +                        //[0]
                    " h.code," +                         //[1]
                    " h.alias" +                         //[2]
                    " FROM Hospital h" +
                    " WHERE " +
                    " (h.name =:name OR h.code =:code OR h.alias =:alias)" +
                    " AND h.status != 'D'";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            "SELECT " +
                    " h.name," +                        //[0]
                    " h.code," +                         //[1]
                    " h.alias" +                         //[2]
                    " FROM Hospital h" +
                    " WHERE " +
                    " h.id!=:id" +
                    " AND" +
                    " (h.name =:name OR h.code =:code OR h.alias = :alias)" +
                    " AND h.status != 'D'";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOR_DROPDOWN =
            " SELECT" +
                    " h.id as value," +                     //[0]
                    " h.name as label," +                   //[1]
                    " h.isCompany as isCompany," +          //[2]
                    " h.alias as alias" +                    //[3]
                    " FROM" +
                    " Hospital h" +
                    " WHERE h.status ='Y'" +
                    " AND h.isCompany='N'" +
                    " ORDER BY h.name ASC ";

    public static String QUERY_TO_SEARCH_HOSPITAL(HospitalSearchRequestDTO searchRequestDTO) {
        return "SELECT" +
                " h.id as id," +                                //[0]
                " h.name as name," +                            //[1]
                " h.status as status," +                        //[2]
                " h.address as address," +                      //[3]
                " tbl.file_uri as fileUri," +                  //[4]
                " h.code as hospitalCode" +                     //[5]
                " FROM" +
                " hospital h" +
                " LEFT JOIN" +
                " (" +
                " SELECT" +
                " hl.hospital_id as hospitalId," +
                " hl.file_uri FROM hospital_logo hl" +
                " WHERE hl.status = 'Y'" +
                " )tbl ON tbl.hospitalId= h.id" +
                GET_WHERE_CLAUSE_FOR_SEARCHING_HOSPITAL.apply(searchRequestDTO);
    }

    private static Function<HospitalSearchRequestDTO, String> GET_WHERE_CLAUSE_FOR_SEARCHING_HOSPITAL =
            (searchRequestDTO) -> {
                String whereClause = " WHERE h.status!='D' AND h.is_company='N'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
                    whereClause += " AND h.status='" + searchRequestDTO.getStatus() + "'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getName()))
                    whereClause += " AND h.name LIKE '%" + searchRequestDTO.getName() + "%'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getHospitalCode()))
                    whereClause += " AND h.code LIKE '%" + searchRequestDTO.getHospitalCode() + "%'";

                whereClause += " ORDER BY h.id DESC";

                return whereClause;
            };

    public static final String QUERY_TO_FETCH_HOSPITAL_DETAILS =
            "SELECT" +
                    " h.id as id," +                                            //[0]
                    " h.name as name," +                                        //[1]
                    " h.status as status," +                                    //[2]
                    " h.address as address," +                                  //[3]
                    " h.pan_number as panNumber," +                             //[4]
                    " h.remarks as remarks," +                                   //[5]
                    " CASE WHEN" +
                    " (hl.status IS NULL OR hl.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " hl.file_uri" +
                    " END as hospitalLogo," +                                   //[6]
                    " CASE WHEN" +
                    " (hb.status IS NULL OR hb.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " hb.file_uri" +
                    " END as hospitalBanner," +                                 //[7]
                    " h.code as hospitalCode," +                                //[8]
                    " tbl1.contact_details as contact_details," +               //[9]
                    " h.refund_percentage as refundPercentage," +               //[10]
                    " h.number_of_admins as numberOfAdmins," +                  //[11]
                    " h.number_of_follow_ups as numberOfFollowUps," +           //[12]
                    " h.follow_up_interval_days as followUpIntervalDays," +     //[13]
                    " h.is_company as isCompany," +                             //[14]
                    " h.alias as alias," +                                       //[15]
                    HOSPITAL_AUDITABLE_QUERY()+
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
                    " AND h.status !='D'" +
                    " AND h.is_company='N'";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_COUNT =
            " SELECT h.numberOfFollowUps as numberOfFollowUps" +
                    " FROM Hospital h" +
                    " WHERE h.id =:hospitalId";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_INTERVAL_DAYS =
            " SELECT h.followUpIntervalDays as followUpIntervalDays" +
                    " FROM Hospital h" +
                    " WHERE h.id =:hospitalId";

    public static String HOSPITAL_AUDITABLE_QUERY() {
        return " h.created_by as createdBy," +
                " h.created_date as createdDate," +
                " h.last_modified_by as lastModifiedBy," +
                " h.last_modified_date as lastModifiedDate";
    }
}

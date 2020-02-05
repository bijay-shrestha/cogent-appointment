package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospital.HospitalSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author smriti ON 12/01/2020
 */
public class HospitalQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            "SELECT " +
                    " h.name," +                        //[0]
                    " h.code" +                         //[1]
                    " FROM Hospital h" +
                    " WHERE " +
                    " (h.name =:name OR h.code =:code)" +
                    " AND h.status != 'D'";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            "SELECT " +
                    " h.name," +                        //[0]
                    " h.code" +                         //[1]
                    " FROM Hospital h" +
                    " WHERE " +
                    " h.id!=:id" +
                    " AND" +
                    " (h.name =:name OR h.code =:code)" +
                    " AND h.status != 'D'";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOR_DROPDOWN =
            " SELECT" +
                    " h.id as value," +                     //[0]
                    " h.name as label" +                    //[1]
                    " FROM" +
                    " Hospital h" +
                    " WHERE h.status ='Y'";

    public static String QUERY_TO_SEARCH_HOSPITAL(HospitalSearchRequestDTO searchRequestDTO) {

        return " SELECT" +
                " h.id as id," +                                       //[0]
                " h.name as name," +                                    //[1]
                " CASE WHEN" +
                " (hl.status IS NULL OR hl.status = 'N')" +
                " THEN null" +
                " ELSE" +
                " hl.fileUri" +
                " END as fileUri," +                                    //[2]
                " h.status as status," +                                //[3]
                " h.address as address," +                               //[4]
                " h.code as hospitalCode" +                              //[5]
                " FROM" +
                " Hospital h" +
                " LEFT JOIN HospitalLogo hl ON h.id =hl.hospital.id" +
                GET_WHERE_CLAUSE_FOR_SEARCHING_HOSPITAL.apply(searchRequestDTO);
    }

    private static Function<HospitalSearchRequestDTO, String> GET_WHERE_CLAUSE_FOR_SEARCHING_HOSPITAL =
            (searchRequestDTO) -> {
                String whereClause = " WHERE h.status!='D'";

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
                    " tbl1.contact_details as contact_details" +               //[9]
                    " FROM" +
                    " hospital h" +
                    " LEFT JOIN hospital_logo hl ON h.id =hl.hospital_id " +
                    " LEFT JOIN hospital_banner hb ON h.id = hb.hospital_id" +
                    " LEFT JOIN " +
                    " (" +
                    " SELECT hc.hospital_id as hospitalId," +
                    " GROUP_CONCAT((CONCAT(hc.id, '-', hc.contact_number))) as contact_details" +
                    " FROM hospital_contact_number hc" +
                    " WHERE hc.status = 'Y'" +
                    " GROUP by hc.hospital_id" +
                    " )tbl1 ON tbl1.hospitalId = h.id" +
                    " WHERE h.id =:id" +
                    " AND h.status !='D'";

    public static String QUERY_TO_FETCH_MIN_HOSPITAL(HospitalMinSearchRequestDTO requestDTO) {
        String query = " SELECT" +
                " h.id as hospitalId," +                                   //[0]
                " h.name as name," +                                       //[1]
                " h.address as address," +                                 //[2]
                " CASE WHEN" +
                " (hl.status IS NULL OR hl.status = 'N')" +
                " THEN null" +
                " ELSE" +
                " hl.file_uri" +
                " END as hospitalLogo," +                                  //[3]
                " tbl1.contactNumber," +                                    //[4]
                " CASE WHEN" +
                " (hb.status IS NULL OR hb.status = 'N')" +
                " THEN null" +
                " ELSE" +
                " hb.file_uri" +
                " END as hospitalBanner" +                                  //[5]
                " FROM" +
                " hospital h" +
                " LEFT JOIN hospital_logo hl ON h.id =hl.hospital_id " +
                " LEFT JOIN hospital_banner hb ON h.id = hb.hospital_id" +
                " LEFT JOIN " +
                " (" +
                " SELECT hc.hospital_id as hospitalId," +
                " GROUP_CONCAT(hc.contact_number) as contactNumber" +
                " FROM hospital_contact_number hc" +
                " WHERE hc.status = 'Y'" +
                " GROUP BY hc.hospital_id" +
                " )tbl1 ON tbl1.hospitalId = h.id" +
                " WHERE h.status ='Y'";

        if (!ObjectUtils.isEmpty(requestDTO.getName()))
            query += " AND h.name LIKE '%" + requestDTO.getName() + "%'";

        return query + " ORDER by h.name";
    }
}

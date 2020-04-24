package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.hospital.HospitalMinSearchRequestDTO;
import org.springframework.util.ObjectUtils;

/**
 * @author smriti ON 12/01/2020
 */
public class HospitalQuery {

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
                " END as hospitalBanner," +                                  //[5]
                " h.code as hospitalCode," +                                  //[6]
                HOSPITAL_AUDITABLE_QUERY() +
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
                " WHERE h.status ='Y'" +
                " AND h.is_company = 'N'";

        if (!ObjectUtils.isEmpty(requestDTO.getName()))
            query += " AND h.name LIKE '%" + requestDTO.getName() + "%'";

        return query + " ORDER by h.name";
    }

    public static final String QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_INTERVAL_DAYS =
            " SELECT h.followUpIntervalDays as followUpIntervalDays" +
                    " FROM Hospital h" +
                    " WHERE h.id =:hospitalId AND h.isCompany = 'N'";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_COUNT =
            " SELECT h.numberOfFollowUps as numberOfFollowUps" +
                    " FROM Hospital h" +
                    " WHERE" +
                    " h.id =:hospitalId AND h.isCompany = 'N'";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_DETAILS =
            " SELECT h.numberOfFollowUps as numberOfFollowUps," +                   //[0]
                    " h.followUpIntervalDays as followUpIntervalDays" +             //[1]
                    " FROM Hospital h" +
                    " WHERE h.id =:hospitalId" +
                    " AND h.status = 'Y' AND h.isCompany = 'N'";

    public static String HOSPITAL_AUDITABLE_QUERY() {
        return " h.createdBy as createdBy," +
                " h.createdDate as createdDate," +
                " h.lastModifiedBy as lastModifiedBy," +
                " h.lastModifiedDate as lastModifiedDate";
    }
}

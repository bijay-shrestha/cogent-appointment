package com.cogent.cogentappointment.esewa.query;


import com.cogent.cogentappointment.esewa.dto.request.hospital.HospitalMinSearchRequestDTO;
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
                " h.esewa_merchant_code as merchantCode," +                               //[3]
                " CASE WHEN" +
                " (hl.status IS NULL OR hl.status = 'N')" +
                " THEN null" +
                " ELSE" +
                " hl.file_uri" +
                " END as hospitalLogo," +                                  //[4]
                " tbl1.contactNumber," +                                   //[5]
                " CASE WHEN" +
                " (hb.status IS NULL OR hb.status = 'N')" +
                " THEN null" +
                " ELSE" +
                " hb.file_uri" +
                " END as hospitalBanner" +                                  //[6]
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
                " AND h.is_company='N'";

        if (!ObjectUtils.isEmpty(requestDTO.getName()))
            query += " AND h.name LIKE '%" + requestDTO.getName() + "%'";

        return query + " ORDER by h.name ASC";
    }

    public static final String QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_DETAILS =
            " SELECT h.numberOfFollowUps as numberOfFollowUps," +                   //[0]
                    " h.followUpIntervalDays as followUpIntervalDays" +             //[1]
                    " FROM Hospital h" +
                    " WHERE h.id =:hospitalId" +
                    " AND h.status = 'Y'" +
                    " AND h.isCompany = 'N'";

    public static final String QUERY_TO_FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE =
            " SELECT " +
                    " h.appointmentServiceType.id as appointmentServiceTypeId," +           //[0]
                    " h.appointmentServiceType.name as appointmentServiceTypeName," +       //[1]
                    " h.isPrimary as isPrimary" +                                           //[2]
                    " FROM HospitalAppointmentServiceType h " +
                    " WHERE h.status = 'Y'" +
                    " AND h.hospital.id =:hospitalId";
}

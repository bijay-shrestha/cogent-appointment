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
                    " h.esewaMerchantCode," +                         //[1]
                    " h.alias" +                         //[2]
                    " FROM Hospital h" +
                    " WHERE " +
                    " (h.name =:name OR h.esewaMerchantCode =:esewaMerchantCode OR h.alias =:alias)" +
                    " AND h.status != 'D'";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            "SELECT " +
                    " h.name," +                        //[0]
                    " h.esewaMerchantCode," +                         //[1]
                    " h.alias" +                         //[2]
                    " FROM Hospital h" +
                    " WHERE " +
                    " h.id!=:id" +
                    " AND" +
                    " (h.name =:name OR h.esewaMerchantCode =:esewaMerchantCode OR h.alias = :alias)" +
                    " AND h.status != 'D'";

    public static final String QUERY_TO_FETCH_ACTIVE_HOSPITAL_FOR_DROPDOWN =
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

    public static final String QUERY_TO_FETCH_HOSPITAL_FOR_DROPDOWN =
            " SELECT" +
                    " h.id as value," +                     //[0]
                    " h.name as label," +                   //[1]
                    " h.isCompany as isCompany," +          //[2]
                    " h.alias as alias" +                    //[3]
                    " FROM" +
                    " Hospital h" +
                    " WHERE h.status !='D'" +
                    " AND h.isCompany='N'" +
                    " ORDER BY h.name ASC ";

    public static String QUERY_TO_SEARCH_HOSPITAL(HospitalSearchRequestDTO searchRequestDTO) {
        return "SELECT" +
                " h.id as id," +                                //[0]
                " h.name as name," +                            //[1]
                " h.status as status," +                        //[2]
                " h.address as address," +                      //[3]
                " tbl.file_uri as fileUri," +                  //[4]
                " h.esewa_merchant_code as esewaMerchantCode" +                     //[5]
                " FROM" +
                " hospital h" +
                " LEFT JOIN" +
                " (" +
                " SELECT" +
                " hl.hospital_id as hospitalId," +
                " hl.file_uri FROM hospital_logo hl" +
                " WHERE hl.status = 'Y'" +
                " )tbl ON tbl.hospitalId= h.id" +
                " LEFT JOIN hospital_billing_mode_info hb ON hb.hospital_id=h.id AND hb.status!='D'" +
                GET_WHERE_CLAUSE_FOR_SEARCHING_HOSPITAL.apply(searchRequestDTO);
    }

    private static Function<HospitalSearchRequestDTO, String> GET_WHERE_CLAUSE_FOR_SEARCHING_HOSPITAL =
            (searchRequestDTO) -> {
                String whereClause = " WHERE h.status!='D' AND h.is_company='N'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
                    whereClause += " AND h.status='" + searchRequestDTO.getStatus() + "'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getBillingModeId()))
                    whereClause += " AND hb.billing_mode_id=" + searchRequestDTO.getBillingModeId();

                if (!ObjectUtils.isEmpty(searchRequestDTO.getName()))
                    whereClause += " AND h.name LIKE '%" + searchRequestDTO.getName() + "%'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getEsewaMerchantCode()))
                    whereClause += " AND h.esewa_merchant_code LIKE '%" + searchRequestDTO.getEsewaMerchantCode() + "%'";

                whereClause += "  GROUP BY h.id" +
                        " ORDER BY h.id DESC ";

                return whereClause;
            };

    public static final String QUERY_TO_FETCH_HOSPITAL_DETAILS =
            "SELECT" +
                    " h.id as id," +                                            //[0]
                    " h.name as name," +                                        //[1]
                    " h.status as status," +                                    //[2]
                    " h.address as address," +                                  //[3]
                    " h.panNumber as panNumber," +                             //[4]
                    " h.remarks as remarks," +                                 //[5]
                    " CASE WHEN" +
                    " (hl.status IS NULL OR hl.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " hl.fileUri" +
                    " END as hospitalLogo," +                                   //[6]
                    " CASE WHEN" +
                    " (hb.status IS NULL OR hb.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " hb.fileUri" +
                    " END as hospitalBanner," +                                 //[7]
                    " h.esewaMerchantCode as esewaMerchantCode," +                                //[8]
                    " h.refundPercentage as refundPercentage," +               //[9]
                    " h.numberOfAdmins as numberOfAdmins," +                  //[10]
                    " h.numberOfFollowUps as numberOfFollowUps," +            //[11]
                    " h.followUpIntervalDays as followUpIntervalDays," +      //[12]
                    " h.isCompany as isCompany," +                            //[13]
                    " h.alias as alias," +                                    //[14]
                    HOSPITAL_AUDITABLE_QUERY() +
                    " FROM" +
                    " Hospital h" +
                    " LEFT JOIN HospitalLogo hl ON h.id =hl.hospital.id " +
                    " LEFT JOIN HospitalBanner hb ON h.id = hb.hospital.id" +
                    " WHERE h.id =:id" +
                    " AND h.status !='D'" +
                    " AND h.isCompany='N'";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_COUNT =
            " SELECT h.numberOfFollowUps as numberOfFollowUps" +
                    " FROM Hospital h" +
                    " WHERE h.id =:hospitalId";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_INTERVAL_DAYS =
            " SELECT h.followUpIntervalDays as followUpIntervalDays" +
                    " FROM Hospital h" +
                    " WHERE h.id =:hospitalId";


    public static final String QUERY_TO_FETCH_HOSPITAL_CONTACT_NUMBER =
            " SELECT hc.id as hospitalContactNumberId," +               //[0]
                    " hc.contactNumber as contactNumber," +             //[1]
                    " hc.status as status" +                            //[2]
                    " FROM HospitalContactNumber hc" +
                    " WHERE hc.status = 'Y'" +
                    " AND hc.hospitalId=:hospitalId";

    private static String HOSPITAL_AUDITABLE_QUERY() {
        return " h.createdBy as createdBy," +
                " h.createdDate as createdDate," +
                " h.lastModifiedBy as lastModifiedBy," +
                " h.lastModifiedDate as lastModifiedDate";
    }

    public static String QUERY_TO_FETCH_ASSIGNED_APPOINTMENT_SERVICE_TYPE =
            " SELECT " +
                    " ast.name as name," +                          //[0]
                    " ast.code as code," +                          //[1]
                    " has.isPrimary as isPrimary"+                  //[2]
                    " FROM Hospital h " +
                    " LEFT JOIN HospitalAppointmentServiceType has ON h.id = has.hospital.id" +
                    " LEFT JOIN AppointmentServiceType ast ON ast.id = has.appointmentServiceType.id" +
                    " WHERE" +
                    " has.status = 'Y'" +
                    " AND h.id =:hospitalId";
}

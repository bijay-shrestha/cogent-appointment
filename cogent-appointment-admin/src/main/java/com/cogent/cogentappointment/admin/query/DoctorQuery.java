package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.query.CdnFileQuery.QUERY_TO_FETCH_DOCTOR_AVATAR_NATIVE;

/**
 * @author smriti on 2019-09-29
 */
public class DoctorQuery {

    public static final String QUERY_TO_VALIDATE_DOCTOR_DUPLICITY =
            "SELECT COUNT(d.id) FROM Doctor d" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE (d.name =:name" +
                    " AND d.mobileNumber=:mobileNumber" +
                    " AND h.id=:hospitalId)" +
                    " AND d.status != 'D'";

    public static final String QUERY_TO_VALIDATE_DOCTOR_DUPLICITY_FOR_UPDATE =
            "SELECT COUNT(d.id) FROM Doctor d" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE d.name =:name" +
                    " AND d.mobileNumber=:mobileNumber" +
                    " AND h.id=:hospitalId" +
                    " AND d.id!=:id" +
                    " AND d.status != 'D'";

    /*SEARCH*/
    public static String QUERY_TO_SEARCH_DOCTOR(DoctorSearchRequestDTO searchRequestDTO) {
        return " SELECT" +
                SELECT_CLAUSE_TO_FETCH_MINIMAL_DOCTOR + "," +
                " tbl1.specialization_name as specializationName," +
                " d.salutation as doctorSalutation," +
                " h.name as hospitalName," +
                " CASE" +
                " WHEN" + " (da.status is null OR da.status = 'N')" +
                " THEN null" +
                " WHEN" +
                " da.file_uri LIKE 'public%'" +
                " THEN" +
                " CONCAT(:cdnUrl,SUBSTRING_INDEX(da.file_uri, 'public', -1))" +
                " ELSE" +
                " da.file_uri" +
                " END as fileUri" +
                " FROM doctor d" +
                " LEFT JOIN hospital h ON h.id = d.hospital_id" +
                " LEFT JOIN doctor_avatar da ON da.doctor_id = d.id" +
                " RIGHT JOIN" +
                " (" +
                QUERY_TO_SEARCH_DOCTOR_SPECIALIZATION.apply(searchRequestDTO) +
                " )tbl1 ON tbl1.doctor_id = d.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_DOCTOR.apply(searchRequestDTO);
    }

    private static final String SELECT_CLAUSE_TO_FETCH_MINIMAL_DOCTOR =
            " d.id as id," +                                              //[0]
                    " d.name as doctorName," +                                  //[1]
                    " d.mobile_number as mobileNumber," +                      //[2]
                    " d.status as status," +                                   //[3]
                    " d.code as code";                                         //[4]

    private static Function<DoctorSearchRequestDTO, String> QUERY_TO_SEARCH_DOCTOR_SPECIALIZATION =
            (searchRequestDTO) -> {
                String query = " SELECT" +
                        " cs.doctor_id as doctor_id," +                               //[0]
                        " GROUP_CONCAT(s.name) as specialization_name" +             //[1]
                        " FROM" +
                        " doctor_specialization cs" +
                        " LEFT JOIN specialization s ON s.id = cs.specialization_id" +
                        " WHERE" +
                        " cs.status ='Y'";

                if (!Objects.isNull(searchRequestDTO)) {
                    if (!Objects.isNull(searchRequestDTO.getSpecializationId()))
                        query += " AND s.id IN (" + searchRequestDTO.getSpecializationId() + ")";
                }
                return query + " GROUP BY cs.doctor_id";
            };

    private static Function<DoctorSearchRequestDTO, String> GET_WHERE_CLAUSE_FOR_SEARCH_DOCTOR =
            (searchRequestDTO) -> {

                String whereClause = " WHERE d.status!='D'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getDoctorId()))
                    whereClause += " AND d.id= " + searchRequestDTO.getDoctorId();

                if (!ObjectUtils.isEmpty(searchRequestDTO.getCode()))
                    whereClause += " AND d.code LIKE '%" + searchRequestDTO.getCode() + "%'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
                    whereClause += " AND d.status='" + searchRequestDTO.getStatus() + "'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getMobileNumber()))
                    whereClause += " AND d.mobile_number LIKE '%" + searchRequestDTO.getMobileNumber() + "%'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getHospitalId()))
                    whereClause += " AND h.id= " + searchRequestDTO.getHospitalId();

                whereClause += " ORDER BY d.id DESC";

                return whereClause;
            };

    /*DROPDOWN*/
    public static final String QUERY_TO_FETCH_DOCTOR_FOR_DROPDOWN =
            " SELECT" +
                    " d.id as value," +                                     //[0]
                    CdnFileQuery.QUERY_TO_FETCH_DOCTOR_AVATAR +
                    " CASE WHEN" +
                    " (d.salutation is null)" +
                    " THEN d.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',d.salutation, d.name)" +
                    " END as label" +                             //[2]
                    " FROM" +
                    " Doctor d" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId" +
                    " WHERE d.status ='Y'" +
                    " ORDER BY label ASC";

    private static final String QUERY_TO_FETCH_DOCTOR_QUALIFICATION_FOR_DETAIL =
            " SELECT" +
                    " dq.doctor_id as doctor_id," +
                    " GROUP_CONCAT(q.name) as qualification_name" +
                    " FROM" +
                    " doctor_qualification dq" +
                    " LEFT JOIN qualification q ON q.id = dq.qualification_id" +
                    " WHERE" +
                    " dq.status = 'Y'" +
                    " GROUP BY dq.doctor_id";

    private static final String SELECT_CLAUSE_TO_FETCH_DOCTOR_DETAILS =
            " d.email as email," +                                                  //[5]
                    " d.nmc_number as nmcNumber," +                                 //[6]
                    " d.remarks as remarks," +                                      //[7]
                    " d.gender as gender," +                                        //[8]
                    " h.name as hospitalName," +                                    //[9]
                    " dac.appointment_charge as appointmentCharge," +                   //[10]
                    " dac.appointment_follow_up_charge as appointmentFollowUpCharge";    //[11]

    private static String QUERY_TO_FETCH_DOCTOR_AVATAR =
            " SELECT" +
                    " da.doctor_id as doctorId," +
                    " CASE" +
                    " WHEN" +
                    " (da.status is null OR da.status = 'N')" +
                    " THEN null" +
                    " WHEN" +
                    " da.file_uri LIKE 'public%'" +
                    " THEN" +
                    " CONCAT(:cdnUrl,SUBSTRING_INDEX(da.file_uri, 'public', -1))" +
                    " ELSE" +
                    " da.file_uri" +
                    " END as fileUri," +
                    " FROM doctor_avatar da" +
                    " WHERE da.status = 'Y'";

    public static final String QUERY_TO_FETCH_DOCTOR_DETAILS =
            " SELECT" +
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_DOCTOR + "," +
                    SELECT_CLAUSE_TO_FETCH_DOCTOR_DETAILS + "," +
                    " tbl1.specialization_name as specializationName," +                 //[11]
                    " tbl2.qualification_name as qualificationName," +                   //[12]
                    " d.salutation as doctorSalutation," +
                    DOCTOR_AUDITABLE_QUERY() + "," +
                    QUERY_TO_FETCH_DOCTOR_AVATAR_NATIVE +
                    " FROM doctor d" +
                    " LEFT JOIN hospital h ON h.id = d.hospital_id" +
                    " LEFT JOIN doctor_appointment_charge dac ON dac.doctor_id= d.id" +
                    " LEFT JOIN doctor_avatar da ON d.id = da.doctor_id" +
                    " RIGHT JOIN" +
                    " (" +
                    QUERY_TO_SEARCH_DOCTOR_SPECIALIZATION.apply(null) +
                    " )tbl1 ON tbl1.doctor_id = d.id" +
                    " RIGHT JOIN" +
                    " (" +
                    QUERY_TO_FETCH_DOCTOR_QUALIFICATION_FOR_DETAIL +
                    " )tbl2 ON tbl2.doctor_id = d.id" +
                    " WHERE d.status != 'D'" +
                    " AND d.id = :id";


//            " SELECT" +
//                    SELECT_CLAUSE_TO_FETCH_MINIMAL_DOCTOR + "," +
//                    SELECT_CLAUSE_TO_FETCH_DOCTOR_DETAILS + "," +
//                    " tbl1.specialization_name as specializationName," +                //[12]
//                    " tbl2.qualification_name as qualificationName," +                   //[13]
//                    QUERY_TO_FETCH_DOCTOR_AVATAR_NATIVE+","+                                                 //[14]
//                    DOCTOR_AUDITABLE_QUERY() + "," +
//                    " d.salutation as doctorSalutation" +
//                    " FROM doctor d" +
//                    " LEFT JOIN hospital h ON h.id = d.hospital_id" +
//                    " LEFT JOIN doctor_appointment_charge dac ON dac.doctor_id= d.id" +
//                    " RIGHT JOIN" +
//                    " (" +
//                    QUERY_TO_SEARCH_DOCTOR_SPECIALIZATION.apply(null) +
//                    " )tbl1 ON tbl1.doctor_id = d.id" +
//                    " RIGHT JOIN" +
//                    " (" +
//                    QUERY_TO_FETCH_DOCTOR_QUALIFICATION_FOR_DETAIL +
//                    " )tbl2 ON tbl2.doctor_id = d.id" +
//                    " LEFT JOIN" +
//                    " (" +
//                    QUERY_TO_FETCH_DOCTOR_AVATAR +
//                    " )tbl3 ON tbl3.doctorId= d.id" +
//                    " WHERE d.status != 'D'" +
//                    " AND d.id = :id";

    /*UPDATE MODAL*/
    private static final String QUERY_TO_FETCH_DOCTOR_SPECIALIZATION_FOR_UPDATE =
            " SELECT" +
                    " cs.doctor_id as doctor_id," +
                    " GROUP_CONCAT(cs.id) as doctor_specialization_id," +
                    " GROUP_CONCAT(s.id) as specialization_id," +
                    " GROUP_CONCAT(s.name) as specialization_name" +
                    " FROM" +
                    " doctor_specialization cs" +
                    " LEFT JOIN specialization s ON s.id = cs.specialization_id" +
                    " WHERE" +
                    " s.status = 'Y'" +
                    " AND cs.status ='Y'" +
                    " GROUP BY cs.doctor_id";

    private static final String QUERY_TO_FETCH_DOCTOR_QUALIFICATION_FOR_UPDATE =
            " SELECT" +
                    " dq.doctor_id as doctor_id," +
                    " GROUP_CONCAT(dq.id) as doctor_qualification_id," +
                    " GROUP_CONCAT(q.id) as qualification_id," +
                    " GROUP_CONCAT(q.name) as qualification_name" +
                    " FROM" +
                    " doctor_qualification dq" +
                    " LEFT JOIN qualification q ON q.id = dq.qualification_id" +
                    " WHERE" +
                    " dq.status = 'Y'" +
                    " AND q.status ='Y'" +
                    " GROUP BY dq.doctor_id";

    public static final String QUERY_TO_FETCH_DOCTOR_DETAILS_FOR_UPDATE =
            " SELECT" +
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_DOCTOR + "," +
                    SELECT_CLAUSE_TO_FETCH_DOCTOR_DETAILS + "," +
                    " h.id as hospitalId," +                                                //[12]
                    " tbl1.doctor_specialization_id as doctorSpecializationId," +           //[13]
                    " tbl1.specialization_id as specializationId," +                        //[14]
                    " tbl1.specialization_name as specializationName," +                    //[15]
                    " tbl2.doctor_qualification_id as doctorQualificationId," +             //[16]
                    " tbl2.qualification_id as qualificationId," +                          //[17]
                    " tbl2.qualification_name as qualificationName," +                      //[18]
                    " CASE" +
                    " WHEN" +
                    " (da.status is null OR da.status = 'N')" +
                    " THEN null" +
                    " WHEN" +
                    " da.file_uri LIKE 'public%'" +
                    " THEN" +
                    " CONCAT(:cdnUrl, SUBSTRING_INDEX(da.file_uri, 'public', -1))" +
                    " ELSE" +
                    " da.file_uri" +
                    " END as fileUri" +                                                     //[19]
                    " FROM doctor d" +
                    " LEFT JOIN hospital h ON h.id = d.hospital_id" +
                    " LEFT JOIN doctor_appointment_charge dac ON dac.doctor_id= d.id" +
                    " LEFT JOIN doctor_avatar da ON d.id = da.doctor_id" +
                    " RIGHT JOIN" +
                    " (" +
                    QUERY_TO_FETCH_DOCTOR_SPECIALIZATION_FOR_UPDATE +
                    " )tbl1 ON tbl1.doctor_id = d.id" +
                    " RIGHT JOIN" +
                    " (" +
                    QUERY_TO_FETCH_DOCTOR_QUALIFICATION_FOR_UPDATE +
                    " )tbl2 ON tbl2.doctor_id = d.id" +
                    " WHERE d.status != 'D'" +
                    " AND d.id = :id";

    public static String QUERY_TO_FETCH_DOCTOR_BY_SPECIALIZATION_ID =
            "SELECT" +
                    " d.id as value," +                                      //[0]
                    CdnFileQuery.QUERY_TO_FETCH_DOCTOR_AVATAR +
                    " CASE WHEN" +
                    " (d.salutation is null)" +
                    " THEN d.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',d.salutation, d.name)" +
                    " END as label" +                                  //[2]
                    " FROM DoctorSpecialization cs" +
                    " LEFT JOIN Doctor d ON d.id = cs.doctorId" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId" +
                    " WHERE cs.specializationId = :id" +
                    " AND cs.status = 'Y'" +
                    " AND d.status = 'Y'" +
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_DOCTOR_BY_HOSPITAL_ID =
            " SELECT" +
                    " d.id as value," +                          //[0]
                    CdnFileQuery.QUERY_TO_FETCH_DOCTOR_AVATAR +  //[1]
                    " CASE WHEN" +
                    " (d.salutation is null)" +
                    " THEN d.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',d.salutation, d.name)" +       //[2]
                    " END as label" +
                    " FROM" +
                    " Doctor d" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE d.status ='Y'" +
                    " AND h.id=:hospitalId " +
                    " ORDER BY label ASC";


    public static final String QUERY_TO_FETCH_MIN_DOCTOR_BY_HOSPITAL_ID =
            " SELECT" +
                    " d.id as value," +                                     //[0]
                    CdnFileQuery.QUERY_TO_FETCH_DOCTOR_AVATAR +
                    " CASE WHEN" +
                    " (d.salutation is null)" +
                    " THEN d.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',d.salutation, d.name)" +
                    " END as label" +                                  //[2]
                    " FROM" +
                    " Doctor d" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE d.status !='D'" +
                    " AND h.id=:hospitalId " +
                    " ORDER BY label ASC";

    public static String QUERY_TO_FETCH_DOCTOR_AVATAR_INFO(Long doctorId) {
        String sql = " SELECT" +
                CdnFileQuery.QUERY_TO_FETCH_DOCTOR_AVATAR +
                " d.id as value" +                                     //[0]
                " FROM" +
                " Doctor d" +
                " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId" +
                " WHERE d.status ='Y'" +
                " AND d.hospital.id =:hospitalId";

        if (!Objects.isNull(doctorId))
            sql += " AND d.id =" + doctorId;

        return sql;
    }

    public static String DOCTOR_AUDITABLE_QUERY() {
        return " d.created_by as createdBy," +
                " d.created_date as createdDate," +
                " d.last_modified_by as lastModifiedBy," +
                " d.last_modified_date as lastModifiedDate";
    }

}
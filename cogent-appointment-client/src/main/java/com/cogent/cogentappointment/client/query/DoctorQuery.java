package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.doctor.DoctorSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author smriti on 2019-09-29
 */
public class DoctorQuery {

    public static final String QUERY_TO_VALIDATE_DOCTOR_DUPLICITY =
            "SELECT COUNT(d.id) FROM Doctor d" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE" +
                    " (d.name =:name" +
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
                " CASE WHEN" +
                " (da.status is null OR da.status = 'N')" +
                " THEN null" +
                " ELSE" +
                " da.file_uri" +
                " END as fileUri" +
                " FROM doctor d" +
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

                String whereClause = " WHERE d.status!='D' AND d.hospital_id=:hospitalId";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
                    whereClause += " AND d.status='" + searchRequestDTO.getStatus() + "'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getDoctorId()))
                    whereClause += " AND d.id=" + searchRequestDTO.getDoctorId();

                if (!ObjectUtils.isEmpty(searchRequestDTO.getCode()))
                    whereClause += " AND d.code LIKE '%" + searchRequestDTO.getCode() + "%'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getMobileNumber()))
                    whereClause += " AND d.mobile_number LIKE '%" + searchRequestDTO.getMobileNumber() + "%'";

                whereClause += " ORDER BY d.id DESC";

                return whereClause;
            };

    /*DROPDOWN*/
    public static final String QUERY_TO_FETCH_DOCTOR_FOR_DROPDOWN =
            " SELECT" +
                    " d.id as value," +                                     //[0]
                    " d.name as label," +                                   //[1]
                    " CASE WHEN" +
                    " (da.status is null OR da.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " da.fileUri" +
                    " END as fileUri" +                                    //[2]
                    " FROM" +
                    " Doctor d" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId" +
                    " WHERE d.status ='Y'" +
                    " AND d.hospital.id=:hospitalId" +
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
            " d.email as email," +                                                        //[5]
                    " d.nmc_number as nmcNumber," +                                       //[6]
                    " d.remarks as remarks," +                                            //[7]
                    " d.gender as gender," +                                              //[8]
                    " dac.appointment_charge as appointmentCharge," +                     //[9]
                    " dac.appointment_follow_up_charge as appointmentFollowUpCharge";     //[10]

    private static String QUERY_TO_FETCH_DOCTOR_AVATAR =
            " SELECT" +
                    " da.doctor_id as doctorId," +
                    " da.file_uri" +
                    " FROM doctor_avatar da" +
                    " WHERE da.status = 'Y'";

    public static final String QUERY_TO_FETCH_DOCTOR_DETAILS =
            " SELECT" +
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_DOCTOR + "," +
                    SELECT_CLAUSE_TO_FETCH_DOCTOR_DETAILS + "," +
                    " tbl1.specialization_name as specializationName," +                 //[11]
                    " tbl2.qualification_name as qualificationName," +                   //[12]
                    " tbl3.file_uri as fileUri," +                                        //[13]
                    DOCTOR_AUDITABLE_QUERY() +
                    " FROM doctor d" +
                    " LEFT JOIN hospital h ON h.id = d.hospital_id" +
                    " LEFT JOIN doctor_appointment_charge dac ON dac.doctor_id= d.id" +
                    " RIGHT JOIN" +
                    " (" +
                    QUERY_TO_SEARCH_DOCTOR_SPECIALIZATION.apply(null) +
                    " )tbl1 ON tbl1.doctor_id = d.id" +
                    " RIGHT JOIN" +
                    " (" +
                    QUERY_TO_FETCH_DOCTOR_QUALIFICATION_FOR_DETAIL +
                    " )tbl2 ON tbl2.doctor_id = d.id" +
                    " LEFT JOIN" +
                    " (" +
                    QUERY_TO_FETCH_DOCTOR_AVATAR +
                    " )tbl3 ON tbl3.doctorId= d.id" +
                    " WHERE d.status != 'D'" +
                    " AND d.id = :id" +
                    " AND d.hospital_id=:hospitalId";

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
                    " cs.status ='Y'" +
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
                    " GROUP BY dq.doctor_id";

    public static final String QUERY_TO_FETCH_DOCTOR_DETAILS_FOR_UPDATE =
            " SELECT" +
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_DOCTOR + "," +
                    SELECT_CLAUSE_TO_FETCH_DOCTOR_DETAILS + "," +
                    " tbl1.doctor_specialization_id as doctorSpecializationId," +           //[11]
                    " tbl1.specialization_id as specializationId," +                        //[12]
                    " tbl1.specialization_name as specializationName," +                    //[13]
                    " tbl2.doctor_qualification_id as doctorQualificationId," +             //[14]
                    " tbl2.qualification_id as qualificationId," +                          //[15]
                    " tbl2.qualification_name as qualificationName," +                      //[16]
                    " tbl3.file_uri as fileUri" +                                            //[17]
                    " FROM doctor d" +
                    " LEFT JOIN hospital h ON h.id = d.hospital_id" +
                    " LEFT JOIN doctor_appointment_charge dac ON dac.doctor_id= d.id" +
                    " RIGHT JOIN" +
                    " (" +
                    QUERY_TO_FETCH_DOCTOR_SPECIALIZATION_FOR_UPDATE +
                    " )tbl1 ON tbl1.doctor_id = d.id" +
                    " RIGHT JOIN" +
                    " (" +
                    QUERY_TO_FETCH_DOCTOR_QUALIFICATION_FOR_UPDATE +
                    " )tbl2 ON tbl2.doctor_id = d.id" +
                    " LEFT JOIN" +
                    " (" +
                    QUERY_TO_FETCH_DOCTOR_AVATAR +
                    " )tbl3 ON tbl3.doctorId= d.id" +
                    " WHERE d.status != 'D'" +
                    " AND d.id = :id" +
                    " AND d.hospital_id=:hospitalId";

    public static String QUERY_TO_FETCH_DOCTOR_BY_SPECIALIZATION_ID =
            "SELECT" +
                    " d.id as value," +                                      //[0]
                    " d.name as label," +                                    //[1]
                    " CASE WHEN" +
                    " (da.status is null OR da.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " da.fileUri" +
                    " END as fileUri" +                                     //[2]
                    " FROM DoctorSpecialization cs" +
                    " LEFT JOIN Doctor d ON d.id = cs.doctorId" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId" +
                    " WHERE cs.specializationId = :id" +
                    " AND cs.status = 'Y'" +
                    " AND d.status = 'Y'" +
                    " AND d.hospital.id= :hospitalId" +
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_DOCTOR_BY_HOSPITAL_ID =
            " SELECT" +
                    " d.id as value," +                                     //[0]
                    " d.name as label," +                                   //[1]
                    " CASE WHEN" +
                    " (da.status is null OR da.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " da.fileUri" +
                    " END as fileUri" +                                    //[2]
                    " FROM" +
                    " Doctor d" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE d.status ='Y'" +
                    " AND h.id=:hospitalId "+
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_MIN_DOCTOR_INFO =
            " SELECT" +
                    " d.id as doctorId," +                                              //[0]
                    " d.name as doctorName," +                                          //[1]
                    " CASE WHEN" +
                    " (da.status IS NULL" +
                    " OR da.status = 'N')" +
                    " THEN NULL" +
                    " ELSE" +
                    " da.file_uri" +
                    " END as fileUri," +                                                //[2]
                    " s.id as specializationId," +                                      //[3]
                    " s.name as specializationName," +                                  //[4]
                    " tbl1.qualificationAlias as qualificationAlias," +                 //[5]
                    " d.nmc_number as nmcNumber" +                                     //[6]
                    " FROM" +
                    " doctor d" +
                    " LEFT JOIN doctor_avatar da ON d.id = da.doctor_id" +
                    " LEFT JOIN doctor_specialization ds ON d.id = ds.doctor_id" +
                    " LEFT JOIN specialization s ON s.id = ds.specialization_id" +
                    " LEFT JOIN(" +
                    " SELECT" +
                    " GROUP_CONCAT(qa.name) as qualificationAlias," +
                    " dq.doctor_id as doctorId" +
                    " FROM" +
                    " doctor_qualification dq" +
                    " LEFT JOIN qualification q ON q.id = dq.qualification_id" +
                    " LEFT JOIN qualification_alias qa ON qa.id = q.qualification_alias" +
                    " WHERE" +
                    " qa.status = 'Y'" +
                    " AND q.status = 'Y'" +
                    " AND dq.status = 'Y'" +
                    " GROUP BY" +
                    " dq.doctor_id" +
                    " )tbl1 ON tbl1.doctorId = d.id" +
                    " LEFT JOIN hospital h ON h.id = d.hospital_id" +
                    " WHERE" +
                    " d.status = 'Y'" +
                    " AND ds.status = 'Y'" +
                    " AND s.status = 'Y'" +
                    " AND h.status = 'Y'" +
                    " AND h.id =:hospitalId" +
                    " ORDER BY d.name";

    public static String QUERY_TO_FETCH_DOCTOR_APPOINTMENT_FOLLOW_UP_CHARGE =
            " SELECT da.appointmentFollowUpCharge" +
                    " FROM DoctorAppointmentCharge da " +
                    " WHERE da.doctorId.id = :doctorId" +
                    " AND da.doctorId.hospital.id = :hospitalId" +
                    " AND da.doctorId.status = 'Y'";

    public static String QUERY_TO_FETCH_DOCTOR_APPOINTMENT_CHARGE =
            " SELECT da.appointmentCharge" +
                    " FROM DoctorAppointmentCharge da " +
                    " WHERE da.doctorId.id = :doctorId" +
                    " AND da.doctorId.hospital.id = :hospitalId" +
                    " AND da.doctorId.status = 'Y'";

    public static String QUERY_TO_FETCH_DOCTOR_AVATAR_INFO(Long doctorId) {
        String sql = " SELECT" +
                " d.id as value," +                                     //[0]
                " CASE WHEN" +
                " (da.status is null OR da.status = 'N')" +
                " THEN null" +
                " ELSE" +
                " da.fileUri" +
                " END as fileUri" +                                    //[1]
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
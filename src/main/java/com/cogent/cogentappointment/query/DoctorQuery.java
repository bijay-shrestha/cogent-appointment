package com.cogent.cogentappointment.query;

import com.cogent.cogentappointment.dto.request.doctor.DoctorSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author smriti on 2019-09-29
 */
public class DoctorQuery {

    public static final String QUERY_TO_VALIDATE_DOCTOR_DUPLICITY =
            "SELECT COUNT(d.id) FROM Doctor d" +
                    " WHERE d.name =:name" +
                    " AND d.mobileNumber=:mobileNumber" +
                    " AND d.status != 'D'";

    public static final String QUERY_TO_VALIDATE_DOCTOR_DUPLICITY_FOR_UPDATE =
            "SELECT COUNT(d.id) FROM Doctor d" +
                    " WHERE d.name =:name" +
                    " AND d.mobileNumber=:mobileNumber" +
                    " AND d.id!=:id" +
                    " AND d.status != 'D'";

    /*SEARCH*/
    public static String QUERY_TO_SEARCH_DOCTOR(DoctorSearchRequestDTO searchRequestDTO) {
        return " SELECT" +
                SELECT_CLAUSE_TO_FETCH_MINIMAL_DOCTOR + "," +
                " tbl1.specialization_name as specializationName" +
                " FROM doctor d" +
                " RIGHT JOIN" +
                " (" +
                QUERY_TO_SEARCH_DOCTOR_SPECIALIZATION.apply(searchRequestDTO) +
                " )tbl1 ON tbl1.doctor_id = d.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_DOCTOR.apply(searchRequestDTO);
    }

    private static final String SELECT_CLAUSE_TO_FETCH_MINIMAL_DOCTOR =
            " d.id as doctorId," +                                              //[0]
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
                        " s.status = 'Y'" +
                        " AND cs.status ='Y'";

                if (!Objects.isNull(searchRequestDTO)) {
                    if (!Objects.isNull(searchRequestDTO.getSpecializationId()))
                        query += " AND s.id IN (" + searchRequestDTO.getSpecializationId() + ")";
                }
                return query + " GROUP BY cs.doctor_id";
            };

    private static Function<DoctorSearchRequestDTO, String> GET_WHERE_CLAUSE_FOR_SEARCH_DOCTOR =
            (searchRequestDTO) -> {

                String whereClause = " WHERE d.status!='D'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
                    whereClause += " AND d.status='" + searchRequestDTO.getStatus() + "'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getName()))
                    whereClause += " AND d.name LIKE '%" + searchRequestDTO.getName() + "%'";

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
                    " END as fileUri," +                                    //[2]
                    " dac.appointmentCharge as appointmentCharge" +          //[3]
                    " FROM" +
                    " Doctor d" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId" +
                    " LEFT JOIN DoctorAppointmentCharge dac ON d.id = dac.doctorId.id" +
                    " WHERE d.status ='Y'";

    private static final String QUERY_TO_FETCH_DOCTOR_QUALIFICATION_FOR_DETAIL =
            " SELECT" +
                    " dq.doctor_id as doctor_id," +
                    " GROUP_CONCAT(q.name) as qualification_name" +
                    " FROM" +
                    " doctor_qualification dq" +
                    " LEFT JOIN qualification q ON q.id = dq.qualification_id" +
                    " WHERE" +
                    " dq.status = 'Y'" +
                    " AND q.status ='Y'" +
                    " GROUP BY dq.doctor_id";

    private static final String SELECT_CLAUSE_TO_FETCH_DOCTOR_DETAILS =
            " d.email as email," +                                                  //[5]
                    " d.nmc_number as nmcNumber," +                                 //[6]
                    " d.remarks as remarks," +                                      //[7]
                    " d.gender as gender," +                                        //[8]
                    " h.name as hospitalName," +                                    //[9]
                    " dac.appointment_charge as appointmentCharge";                 //[10]

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
                    " tbl1.specialization_name as specializationName," +                //[11]
                    " tbl2.qualification_name as qualificationName," +                   //[12]
                    " tbl3.file_uri as fileUri" +                                       //[13]
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
                    " AND d.id = :id";

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
                    " h.id as hospitalId," +                                                //[11]
                    " tbl1.doctor_specialization_id as doctorSpecializationId," +           //[12]
                    " tbl1.specialization_id as specializationId," +                        //[13]
                    " tbl1.specialization_name as specializationName," +                    //[14]
                    " tbl2.doctor_qualification_id as doctorQualificationId," +             //[15]
                    " tbl2.qualification_id as qualificationId," +                          //[16]
                    " tbl2.qualification_name as qualificationName," +                      //[17]
                    " tbl3.file_uri as fileUri" +                                            //[18]
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
                    " AND d.id = :id";

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
                    " AND d.status = 'Y'";

}
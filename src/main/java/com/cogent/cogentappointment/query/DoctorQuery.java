package com.cogent.cogentappointment.query;

import com.cogent.cogentappointment.dto.request.doctor.DoctorSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author smriti on 2019-09-29
 */
public class DoctorQuery {

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
                    " d.code as code";                                         //[6]

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
                    " d.id as value," +                                      //[0]
                    " d.name as label" +                                     //[1]
                    " FROM" +
                    " Doctor d" +
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
            " d.email as email," +
                    " d.nmc_number as nmcNumber," +
                    " d.remarks as remarks," +
                    " d.gender as gender," +
                    " h.name as hospitalName";

    public static final String QUERY_TO_FETCH_DOCTOR_DETAILS =
            " SELECT" +
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_DOCTOR + "," +
                    SELECT_CLAUSE_TO_FETCH_DOCTOR_DETAILS + "," +
                    " tbl1.specialization_name as specializationName," +
                    " tbl2.qualification_name as qualificationName" +
                    " FROM doctor d" +
                    " LEFT JOIN hospital h ON h.id = d.hospital_id" +
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
                    " h.id as hospitalId," +
                    " tbl1.doctor_specialization_id as doctorSpecializationId," +
                    " tbl1.specialization_id as specializationId," +
                    " tbl1.specialization_name as specializationName," +
                    " tbl2.doctor_qualification_id as doctorQualificationId," +
                    " tbl2.qualification_id as qualificationId," +
                    " tbl2.qualification_name as qualificationName" +
                    " FROM doctor d" +
                    " LEFT JOIN hospital h ON h.id = d.hospital_id" +
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
                    " d.name as label" +                                     //[1]
                    " FROM DoctorSpecialization cs" +
                    " LEFT JOIN Doctor d ON d.id = cs.doctorId" +
                    " WHERE cs.specializationId = :id" +
                    " AND cs.status = 'Y'" +
                    " AND d.status = 'Y'";

}
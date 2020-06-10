package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.specialization.SpecializationSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author smriti on 2019-09-25
 */
public class SpecializationQuery {

    public static final String QUERY_TO_VALIDATE_SPECIALIZATION =
            "SELECT" +
                    " s.name," +                                                     //[0]
                    " s.code" +                                                     //[1]
                    " FROM Specialization s" +
                    " LEFT JOIN Hospital h ON h.id = s.hospital.id" +
                    " WHERE" +
                    " (s.name =:name OR s.code =:code)" +
                    " AND s.status != 'D'" +
                    " AND h.status !='D'" +
                    " AND h.id=:hospitalId";

    public static final String QUERY_TO_VALIDATE_SPECIALIZATION_FOR_UPDATE =
            "SELECT" +
                    " s.name," +                                                     //[0]
                    " s.code" +                                                     //[1]
                    " FROM Specialization s" +
                    " LEFT JOIN Hospital h ON h.id = s.hospital.id" +
                    " WHERE s.id!= :id" +
                    " AND (s.name =:name OR s.code =:code)" +
                    " AND s.status != 'D'" +
                    " AND h.id=:hospitalId";

    public static final String QUERY_TO_FETCH_ACTIVE_SPECIALIZATION_FOR_DROPDOWN =
            " SELECT" +
                    " s.id as value," +                                      //[0]
                    " s.name as label" +                                     //[1]
                    " FROM" +
                    " Specialization s" +
                    " WHERE s.status ='Y'" +
                    " AND s.hospital.id=:hospitalId" +
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_SPECIALIZATION_DETAILS =
            " SELECT s.name as name," +                                     //[0]
                    " s.code as code," +                                     //[1]
                    " s.status as status," +                                //[2]
                    " s.remarks as remarks," +                               //[3]
                    SPECIALIZATION_AUDITABLE_QUERY()+
                    " FROM" +
                    " Specialization s" +
                    " WHERE s.id = :id" +
                    " AND s.status != 'D'" +
                    " AND s.hospital.id=:hospitalId";

    public static String QUERY_TO_SEARCH_SPECIALIZATION(SpecializationSearchRequestDTO searchRequestDTO) {
        return " SELECT" +
                " s.id as id," +                                      //[0]
                " s.name as name," +                                  //[1]
                " s.status as status," +                              //[2]
                " s.code as code" +                                   //[3]
                " FROM" +
                " Specialization s" +
                " LEFT JOIN Hospital h ON h.id = s.hospital.id" +
                GET_WHERE_CLAUSE_FOR_SEARCHING_SPECIALIZATION.apply(searchRequestDTO);
    }

    private static Function<SpecializationSearchRequestDTO, String>
            GET_WHERE_CLAUSE_FOR_SEARCHING_SPECIALIZATION = (searchRequestDTO) -> {

        String whereClause = " WHERE s.status!='D' AND h.id =:hospitalId";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND s.status='" + searchRequestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getCode()))
            whereClause += " AND s.code LIKE '%" + searchRequestDTO.getCode() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getName()))
            whereClause += " AND s.name LIKE '%" + searchRequestDTO.getName() + "%'";

        whereClause += " ORDER BY s.id DESC";

        return whereClause;
    };

    public static String QUERY_TO_FETCH_SPECIALIZATION_BY_DOCTOR_ID =
            "SELECT" +
                    " s.id as value," +                                                   //[0]
                    " s.name as label" +                                                 //[1]
                    " FROM DoctorSpecialization cs" +
                    " LEFT JOIN Specialization s ON s.id = cs.specializationId" +
                    " WHERE" +
                    " cs.doctorId =:id" +
                    " AND cs.status = 'Y'" +
                    " AND s.status = 'Y'" +
                    " AND s.hospital.id=:hospitalId"+
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_SPECIALIZATION_BY_HOSPITAL_ID =
            " SELECT" +
                    " s.id as value," +                                      //[0]
                    " s.name as label" +                                     //[1]
                    " FROM" +
                    " Specialization s" +
                    " LEFT JOIN Hospital h ON h.id = s.hospital.id" +
                    " WHERE s.status ='Y'" +
                    " AND h.status = 'Y'" +
                    " AND h.id =:hospitalId" +
                    " ORDER BY s.name";

    public static String SPECIALIZATION_AUDITABLE_QUERY() {
        return " s.createdBy as createdBy," +
                " s.createdDate as createdDate," +
                " s.lastModifiedBy as lastModifiedBy," +
                " s.lastModifiedDate as lastModifiedDate";
    }
}

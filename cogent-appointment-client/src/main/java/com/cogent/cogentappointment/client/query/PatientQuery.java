package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import org.springframework.util.ObjectUtils;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientQuery {

    public final static String QUERY_TO_VALIDATE_PATIENT_DUPLICITY =
            "SELECT " +
                    " COUNT(p.id)" +
                    " FROM Patient p" +
                    " LEFT JOIN Hospital h ON h.id = p.hospitalId.id" +
                    " WHERE " +
                    " (p.name =:name" +
                    " AND p.mobileNumber =:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth)" +
                    " AND h.id =:hospitalId" +
                    " AND p.status != 'D'";

    public static final String SELECT_CLAUSE_TO_FETCH_PATIENT_DETAILS =
            " SELECT p.id as patientId," +                              //[0]
                    " p.name as name," +                                //[1]
                    " p.mobileNumber as mobileNumber," +                //[2]
                    " p.address as address," +                          //[3]
                    " p.email as email," +                              //[4]
                    " p.gender as gender," +                            //[5]
                    " p.dateOfBirth as dateOfBirth," +                   //[6]
                    " p.registrationNumber as registrationNumber" +      //[7]
                    " FROM Patient p";

    public static final String GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS =
            " WHERE p.eSewaId=:eSewaId" +
                    " AND p.isSelf=:isSelf" +
                    " AND p.hospitalId.id=:hospitalId" +
                    " AND p.status='Y'" +
                    " AND p.isRegistered='Y'";

    public static final String QUERY_TO_FETCH_PATIENT_DETAILS =
            SELECT_CLAUSE_TO_FETCH_PATIENT_DETAILS + GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS;

    public static final String QUERY_TO_FETCH_MINIMAL_PATIENT =
            " SELECT p.id as patientId," +                                  //[0]
                    " p.name as name," +                                    //[1]
                    " p.mobileNumber as mobileNumber," +                    //[2]
                    " p.gender as gender," +                                //[3]
                    " p.address as address," +                              //[4]
                    " p.dateOfBirth as dateOfBirth," +                      //[5]
                    " p.registrationNumber as registrationNumber" +         //[6]
                    " FROM Patient p" +
                    GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS;

    public static final String GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS_BY_ID =
            " WHERE p.id=:id" +
                    " AND p.status='Y'";

    public static final String QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID =
            "SELECT" +
                    " p.name as name," +
                    " p.gender as gender," +
                    " p.address as address," +
                    " p.email as email," +
                    " p.mobileNumber as mobileNumber," +
                    " p.registrationNumber as registrationNumber," +
                    " p.eSewaId as eSewaId," +
                    " p.status as status," +
                    " p.dateOfBirth as dateOfBirth," +
                    " p.hospitalNumber as hospitalNumber," +
                    " p.isSelf as isSelf," +
                    " p.isRegistered as isRegistered," +
                    " h.name as hospitalName" +
                    " FROM Patient p" +
                    " LEFT JOIN Hospital h ON h.id=p.hospitalId" +
                    " WHERE p.id=:id" +
                    " AND p.status='Y'";

    public static final String QUERY_TO_FETCH_PATIENT(PatientSearchRequestDTO searchRequestDTO) {
        return "SELECT" +
                " p.name as name," +
                " p.gender as gender," +
                " p.address as address," +
                " p.email as email," +
                " p.mobileNumber as mobileNumber," +
                " p.registrationNumber as registrationNumber," +
                " p.eSewaId as eSewaId," +
                " p.status as status," +
                " p.dateOfBirth as dateOfBirth," +
                " p.hospitalNumber as hospitalNumber," +
                " h.name as hospitalName" +
                " FROM Patient p" +
                " LEFT JOIN Hospital h ON h.id=p.hospitalId" +
                " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(searchRequestDTO);
    }

    private static final String GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(PatientSearchRequestDTO searchRequestDTO) {
        String whereClause = " WHERE p.status!='D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getEsewaId()))
            whereClause += " p.eSewaId=" + searchRequestDTO.getEsewaId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " p.status='" + searchRequestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientMetaInfo())) {
            whereClause += " pmi.metaInfo LIKE '%" + searchRequestDTO.getPatientMetaInfo() + "%'";
        }

        whereClause += " ORDER BY p.id DESC";

        return whereClause;
    }
}

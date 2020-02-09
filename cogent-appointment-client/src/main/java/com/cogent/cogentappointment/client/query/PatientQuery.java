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

    public final static String QUERY_TO_VALIDATE_UPDATED_PATIENT_DUPLICITY =
            "SELECT " +
                    " COUNT(p.id)" +
                    " FROM Patient p" +
                    " LEFT JOIN Hospital h ON h.id = p.hospitalId.id" +
                    " WHERE " +
                    " (p.name =:name" +
                    " AND p.mobileNumber =:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth" +
                    " AND p.id !=:id)" +
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
            " WHERE p.name=:name" +
                    " AND p.mobileNumber=:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth" +
                    " AND p.hospitalId.id=:hospitalId" +
                    " AND p.isSelf=:isSelf" +
                    " AND p.status='Y'" +
                    " AND p.isRegistered='Y'";

    /*FOR SELF*/
    public static final String QUERY_TO_FETCH_PATIENT_DETAILS_FOR_SELF =
            SELECT_CLAUSE_TO_FETCH_PATIENT_DETAILS + GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS;

    public static final String QUERY_TO_FETCH_MINIMAL_PATIENT_FOR_OTHERS =
            " SELECT p.id as patientId," +                                  //[0]
                    " p.name as name," +                                    //[1]
                    " p.mobileNumber as mobileNumber," +                    //[2]
                    " p.gender as gender," +                                //[3]
                    " p.address as address," +                              //[4]
                    " p.dateOfBirth as dateOfBirth," +                      //[5]
                    " p.registrationNumber as registrationNumber" +         //[6]
                    " FROM Patient p" +
                    GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS;

    /*FOR OTHERS*/
    public static final String QUERY_TO_FETCH_OTHER_PATIENT_DETAILS_BY_ID =
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

    public static String QUERY_TO_SEARCH_PATIENT(PatientSearchRequestDTO searchRequestDTO) {
        return "SELECT" +
                " p.name as name," +                                             //[0]
                " p.gender as gender," +                                         //[1]
                " p.address as address," +                                       //[2]
                " p.email as email," +                                           //[3]
                " p.mobileNumber as mobileNumber," +                             //[4]
                " p.registrationNumber as registrationNumber," +                 //[5]
                " p.eSewaId as eSewaId," +                                       //[6]
                " p.status as status," +                                         //[7]
                " p.dateOfBirth as dateOfBirth," +                               //[8]
                " p.hospitalNumber as hospitalNumber," +                         //[9]
                " h.name as hospitalName" +                                      //[10]
                " FROM Patient p" +
                " LEFT JOIN Hospital h ON h.id=p.hospitalId" +
                " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(searchRequestDTO);
    }

    private static String GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(PatientSearchRequestDTO searchRequestDTO) {
        String whereClause = " WHERE p.status!='D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getEsewaId()))
            whereClause += " AND p.eSewaId LIKE '%" + searchRequestDTO.getEsewaId() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND p.status='" + searchRequestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientMetaInfo()))
            whereClause += " AND pmi.id=" + searchRequestDTO.getPatientMetaInfo();

        whereClause += " ORDER BY p.id DESC";

        return whereClause;
    }
}

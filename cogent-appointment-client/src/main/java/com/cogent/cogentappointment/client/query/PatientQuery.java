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
                    " LEFT JOIN HospitalPatientInfo hp ON hp.patientId = p.id" +
                    " WHERE " +
                    " (p.name =:name" +
                    " AND p.mobileNumber =:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth)" +
                    " AND hp.hospitalId =:hospitalId" +
                    " AND hp.status != 'D'";

    public final static String QUERY_TO_VALIDATE_UPDATED_PATIENT_DUPLICITY =
            "SELECT " +
                    " COUNT(p.id)" +
                    " FROM Patient p" +
                    " LEFT JOIN HospitalPatientInfo hp ON hp.patientId = p.id" +
                    " WHERE " +
                    " (p.name =:name" +
                    " AND p.mobileNumber =:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth" +
                    " AND p.id !=:id)" +
                    " AND hp.hospitalId =:hospitalId" +
                    " AND hp.status != 'D'";

    private static final String SELECT_CLAUSE_TO_FETCH_PATIENT_DETAILS =
            " SELECT p.id as patientId," +                                  //[0]
                    " p.name as name," +                                    //[1]
                    " p.mobileNumber as mobileNumber," +                    //[2]
                    " p.gender as gender," +                                //[3]
                    " p.dateOfBirth as dateOfBirth," +                      //[4]
                    " hp.address as address," +                             //[5]
                    " hp.email as email," +                                 //[6]
                    " hp.registrationNumber as registrationNumber" +        //[7]
                    " FROM Patient p" +
                    " LEFT JOIN HospitalPatientInfo hp ON hp.patientId = p.id";

    private static final String GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS =
            " WHERE p.name=:name" +
                    " AND p.mobileNumber=:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth" +
                    " AND hp.hospitalId =:hospitalId" +
                    " AND hp.isSelf=:isSelf" +
                    " AND hp.status='Y'";

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

    public static final String QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID =
            "SELECT" +
                    " p.id as id," +
                    " p.name as name," +
                    " p.dateOfBirth as dateOfBirth," +
                    " p.mobileNumber as mobileNumber," +
                    " p.gender as gender," +
                    " p.eSewaId as eSewaId," +
                    " hpi.status as status," +
                    " h.name as hospitalName," +
                    " hpi.registrationNumber as registrationNumber," +
                    " hpi.hospitalNumber as hospitalNumber," +
                    " hpi.email as email," +
                    " hpi.address as address," +
                    " hpi.isSelf as isSelf," +
                    " hpi.isRegistered as isRegistered" +
                    " FROM Patient p " +
                    " LEFT JOIN HospitalPatientInfo hpi On p.id=hpi.patientId" +
                    " LEFT JOIN Hospital h ON h.id=hpi.hospitalId" +
                    " WHERE p.id=:id" +
                    " AND hpi.status='Y'";

    public static String QUERY_TO_SEARCH_PATIENT(PatientSearchRequestDTO searchRequestDTO) {
        return "SELECT" +
                " p.id as id," +
                " p.name as name," +                                             //[0]
                " hpi.address as address," +                                     //[1]
                " hpi.email as email," +                                         //[2]
                " p.mobileNumber as mobileNumber," +                             //[3]
                " hpi.registrationNumber as registrationNumber," +               //[4]
                " p.eSewaId as eSewaId," +                                       //[5]
                " hpi.status as status," +                                       //[6]
                " p.dateOfBirth as dateOfBirth," +                               //[7]
                " hpi.hospitalNumber as hospitalNumber," +                       //[8]
                " h.name as hospitalName," +                                     //[9]
                " h.id as hospitalId" +                                         //[10]
                " FROM Patient p" +
                " LEFT JOIN HospitalPatientInfo hpi ON p.id=hpi.patientId" +
                " LEFT JOIN Hospital h ON h.id=hpi.hospitalId" +
                " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(searchRequestDTO);
    }

    private static String GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(PatientSearchRequestDTO searchRequestDTO) {
        String whereClause = " WHERE h.id=" + searchRequestDTO.getHospitalId() + " AND hpi.status!='D' ";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getEsewaId()))
            whereClause += " AND p.eSewaId LIKE '%" + searchRequestDTO.getEsewaId() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND hpi.status='" + searchRequestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientMetaInfo()))
            whereClause += " AND pmi.id=" + searchRequestDTO.getPatientMetaInfo();


        whereClause += " ORDER BY p.id DESC";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_LATEST_REGISTRATION_NUMBER =
            " SELECT registration_number" +
                    " FROM hospital_patient_info p " +
                    " WHERE" +
                    " registration_number IS NOT NULL" +
                    " AND p.hospital_id=:hospitalId" +
                    " ORDER BY id DESC" +
                    " LIMIT 1";
}

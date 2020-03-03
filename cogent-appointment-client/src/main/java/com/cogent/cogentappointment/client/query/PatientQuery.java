package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.StringConstant.COMMA_SEPARATED;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientQuery {

    public final static String QUERY_TO_VALIDATE_PATIENT_DUPLICITY =
            "SELECT " +
                    " COUNT(p.id)" +
                    " FROM Patient p" +
                    " WHERE " +
                    " (p.name =:name" +
                    " AND p.mobileNumber =:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth" +
                    " AND p.id !=:id)";

    /* AGE CALCULATION:
    TIMESTAMPDIFF(YEAR, date_of_birth , CURDATE() ) as _year
    TIMESTAMPDIFF(MONTH, date_of_birth, CURDATE() ) % 12 as _month
    FLOOR( TIMESTAMPDIFF( DAY, date_of_birth ,  CURDATE()) % 30.4375 ) as _day
    * */
    public static final String QUERY_TO_CALCULATE_PATIENT_AGE =
            " CASE" +
                    " WHEN" +
                    " (((TIMESTAMPDIFF(YEAR, p.dateOfBirth, CURDATE()))<=0) AND" +
                    " ((TIMESTAMPDIFF(MONTH, p.dateOfBirth, CURDATE()) % 12)<=0))" +
                    " THEN" +
                    " CONCAT((FLOOR(TIMESTAMPDIFF(DAY, p.dateOfBirth, CURDATE()) % 30.4375)), ' days')" +
                    " WHEN" +
                    " ((TIMESTAMPDIFF(YEAR, p.dateOfBirth ,CURDATE()))<=0)" +
                    " THEN" +
                    " CONCAT(((TIMESTAMPDIFF(MONTH, p.dateOfBirth, CURDATE()) % 12)), ' months')" +
                    " ELSE" +
                    " CONCAT(((TIMESTAMPDIFF(YEAR, p.dateOfBirth ,CURDATE()))), ' years')" +
                    " END AS age";

    public static final String QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE =
            " CASE" +
                    " WHEN" +
                    " (((TIMESTAMPDIFF(YEAR, p.date_of_birth, CURDATE()))<=0) AND" +
                    " ((TIMESTAMPDIFF(MONTH, p.date_of_birth, CURDATE()) % 12)<=0))" +
                    " THEN" +
                    " CONCAT((FLOOR(TIMESTAMPDIFF(DAY, p.date_of_birth, CURDATE()) % 30.4375)), ' days')" +
                    " WHEN" +
                    " ((TIMESTAMPDIFF(YEAR, p.date_of_birth ,CURDATE()))<=0)" +
                    " THEN" +
                    " CONCAT(((TIMESTAMPDIFF(MONTH, p.date_of_birth, CURDATE()) % 12)), ' months')" +
                    " ELSE" +
                    " CONCAT(((TIMESTAMPDIFF(YEAR, p.date_of_birth ,CURDATE()))), ' years')" +
                    " END AS age";

    private static final String SELECT_CLAUSE_TO_FETCH_PATIENT_DETAILS =
            " SELECT p.id as patientId," +                                  //[0]
                    " p.name as name," +                                    //[1]
                    " p.mobileNumber as mobileNumber," +                    //[2]
                    " p.gender as gender," +                                //[3]
                    " p.dateOfBirth as dateOfBirth," +                      //[4]
                    " hpi.address as address," +                             //[5]
                    " hpi.email as email," +                                 //[6]
                    " hpi.registrationNumber as registrationNumber," +        //[7]
                    QUERY_TO_CALCULATE_PATIENT_AGE;

    private static final String GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS =
            " WHERE p.name=:name" +
                    " AND p.mobileNumber=:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth";

    /*FOR SELF*/
    public static final String QUERY_TO_FETCH_PATIENT_DETAILS_FOR_SELF =
            SELECT_CLAUSE_TO_FETCH_PATIENT_DETAILS +
                    " FROM Patient p" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id = p.id"
                    + GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS;

    /*FOR OTHERS*/
    public static final String QUERY_TO_FETCH_CHILD_PATIENT_IDS =
            " SELECT pm.childPatientId.id" +
                    " FROM Patient p" +
                    " LEFT JOIN PatientRelationInfo pm ON pm.parentPatientId.id= p.id" +
                    GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS +
                    " AND pm.status = 'Y'";

    public static String QUERY_TO_FETCH_MIN_PATIENT_INFO_FOR_OTHERS(List<Long> childPatientIds) {

        String patientIds = StringUtils.join(childPatientIds, COMMA_SEPARATED);

        return " SELECT" +
                " hpi.id as hospitalPatientInfoId," +                    //[0]
                " p.id as patientId," +                                 //[1]
                " p.name as name," +                                    //[2]
                " p.mobileNumber as mobileNumber," +                    //[3]
                " p.gender as gender," +                                //[4]
                " hpi.address as address," +                            //[5]
                " hpi.registrationNumber as registrationNumber," +      //[6]
                QUERY_TO_CALCULATE_PATIENT_AGE +                        //[7]
                " FROM Patient p" +
                " LEFT JOIN HospitalPatientInfo hpi ON hp.patient.id = p.id" +
                " WHERE p.id IN (" + patientIds + ")";
    }

    public static String QUERY_TO_FETCH_MIN_PATIENT_DETAILS_FOR_OTHERS =
            SELECT_CLAUSE_TO_FETCH_PATIENT_DETAILS +
                    " FROM HospitalPatientInfo hpi " +
                    " LEFT JOIN Patient p ON p.id = hpi.patient.id" +
                    " WHERE hpi.id =:hospitalPatientInfoId";

    public static final String QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID =
            "SELECT" +
                    " p.id as id," +
                    " p.name as name," +
                    " p.dateOfBirth as dateOfBirth," +
                    " p.mobileNumber as mobileNumber," +
                    " p.gender as gender," +
                    " p.eSewaId as eSewaId," +
                    " hpi.status as status," +
                    " hpi.registrationNumber as registrationNumber," +
                    " hpi.hospitalNumber as hospitalNumber," +
                    " hpi.email as email," +
                    " hpi.address as address," +
                    " hpi.isRegistered as isRegistered," +
                    QUERY_TO_CALCULATE_PATIENT_AGE +
                    " FROM Patient p " +
                    " LEFT JOIN HospitalPatientInfo hpi ON p.id=hpi.patient.id" +
                    " LEFT JOIN Hospital h ON h.id=hpi.hospital.id" +
                    " WHERE p.id=:id" +
                    " AND h.id =:hospitalId" +
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
                " hpi.hospitalNumber as hospitalNumber," +                       //[7]
                QUERY_TO_CALCULATE_PATIENT_AGE +                                //[8]
                " FROM Patient p" +
                " LEFT JOIN HospitalPatientInfo hpi ON p.id=hpi.patient.id" +
                " LEFT JOIN Hospital h ON h.id=hpi.hospital.id" +
                " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(searchRequestDTO);
    }

    private static String GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(PatientSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE h.id= :hospitalId AND hpi.status!='D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getEsewaId()))
            whereClause += " AND p.eSewaId LIKE '%" + searchRequestDTO.getEsewaId() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND hpi.status='" + searchRequestDTO.getStatus() + "'";

        if (!Objects.isNull(searchRequestDTO.getPatientMetaInfo()))
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

    public static final String QUERY_TO_FETCH_PATIENT =
            " SELECT p FROM Patient p" +
                    " WHERE " +
                    " p.name =:name" +
                    " AND p.mobileNumber =:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth";

    public static final String QUERY_TO_FETCH_PATIENT_DETAIL_BY_APPOINTMENT_ID =
            " SELECT " +
                    " a.appointmentNumber as appointmentNumber," +              //[0]
                    " p.name as name," +                                        //[1]
                    " p.mobileNumber as mobileNumber," +                        //[2]
                    " p.gender as gender," +                                    //[3]
                    " hpi.address as address," +                                //[4]
                    " a.isSelf as isSelf," +                                    //[5]
                    QUERY_TO_CALCULATE_PATIENT_AGE +                            //[6]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " WHERE a.id =:appointmentId";


}

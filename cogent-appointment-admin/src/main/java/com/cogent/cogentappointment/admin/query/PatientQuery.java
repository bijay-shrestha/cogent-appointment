package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import org.springframework.util.ObjectUtils;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientQuery {

    /* AGE CALCULATION:
        TIMESTAMPDIFF(YEAR, date_of_birth , CURDATE() ) as _year
        TIMESTAMPDIFF(MONTH, date_of_birth, CURDATE() ) % 12 as _month
        FLOOR( TIMESTAMPDIFF( DAY, date_of_birth ,  CURDATE()) % 30.4375 ) as _day
    * */
    public static String QUERY_TO_CALCULATE_PATIENT_AGE =
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

    public static String QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE =
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

    public final static String QUERY_TO_VALIDATE_UPDATED_PATIENT_DUPLICITY =
            "SELECT " +
                    " COUNT(p.id)" +
                    " FROM Patient p" +
                    " LEFT JOIN HospitalPatientInfo hp ON hp.patient.id = p.id" +
                    " WHERE " +
                    " (p.name =:name" +
                    " AND p.mobileNumber =:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth" +
                    " AND p.id !=:id)" +
                    " AND hp.hospital.id =:hospitalId" +
                    " AND hp.status != 'D'";

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
                    " hpi.isRegistered as isRegistered," +
                    " h.id as hospitalId" +
                    " FROM Patient p " +
                    " LEFT JOIN HospitalPatientInfo hpi On p.id=hpi.patient.id" +
                    " LEFT JOIN Hospital h ON h.id=hpi.hospital.id" +
                    " WHERE hpi.id=:hospitalPatientInfoId" +
                    " AND hpi.status='Y'";

    public static final String QUERY_TO_FETCH_LATEST_REGISTRATION_NUMBER =
            " SELECT registration_number" +
                    " FROM hospital_patient_info p " +
                    " WHERE" +
                    " registration_number IS NOT NULL" +
                    " AND p.hospital_id=:hospitalId" +
                    " ORDER BY id DESC" +
                    " LIMIT 1";

    public static String QUERY_TO_FETCH_PATIENT(PatientSearchRequestDTO searchRequestDTO) {
        return "SELECT" +
                " hpi.id as id," +
                " p.name as name," +                                             //[0]
                " hpi.address as address," +                                       //[1]
                " hpi.email as email," +                                           //[2]
                " p.mobileNumber as mobileNumber," +                             //[3]
                " hpi.registrationNumber as registrationNumber," +                 //[4]
                " p.eSewaId as eSewaId," +                                       //[5]
                " hpi.status as status," +                                         //[6]
                " p.dateOfBirth as dateOfBirth," +                               //[7]
                " hpi.hospitalNumber as hospitalNumber," +                         //[8]
                " h.name as hospitalName" +                                      //[9]
                " FROM Patient p" +
                " LEFT JOIN HospitalPatientInfo hpi ON p.id=hpi.patient.id" +
                " LEFT JOIN Hospital h ON h.id=hpi.hospital.id" +
                " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(searchRequestDTO);
    }

    private static String GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(PatientSearchRequestDTO searchRequestDTO) {
        String whereClause = " WHERE hpi.status!='D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getHospitalId()))
            whereClause += " AND  h.id=" + searchRequestDTO.getHospitalId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getEsewaId()))
            whereClause += " AND p.eSewaId LIKE '%" + searchRequestDTO.getEsewaId() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND hpi.status='" + searchRequestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientMetaInfo()))
            whereClause += " AND pmi.id=" + searchRequestDTO.getPatientMetaInfo();


        whereClause += " ORDER BY p.id DESC";

        return whereClause;
    }

    public static String QUERY_TO_FETCH_PATIENT_BY_HOSPITAL_PATIENT_INFO_ID =
            "SELECT" +
                    " p" +
                    " FROM" +
                    " Patient p" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id=p.id" +
                    " WHERE hpi.id=:hospitalPatientInfoId";

    public static final String QUERY_TO_FETCH_PATIENT_DETAIL_BY_APPOINTMENT_ID =
            " SELECT " +
                    " a.appointmentNumber as appointmentNumber," +              //[0]
                    " p.name as name," +                                        //[1]
                    " p.mobileNumber as mobileNumber," +                        //[2]
                    " p.gender as gender," +                                    //[3]
                    " hpi.address as address," +                                //[4]
                    " a.isSelf as isSelf," +                                   //[5]
                    QUERY_TO_CALCULATE_PATIENT_AGE +                           //[6]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " WHERE a.id =:appointmentId";
}

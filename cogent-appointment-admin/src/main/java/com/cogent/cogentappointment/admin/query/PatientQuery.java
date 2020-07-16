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
                    " AND p.dateOfBirth =:dateOfBirth)" +
                    " AND p.id !=:id" +
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
                    " h.id as hospitalId," +
                    " hpi.remarks as remarks," +
                    QUERY_TO_CALCULATE_PATIENT_AGE + "," +
                    PATIENT_AUDITABLE_QUERY() +
                    " FROM Patient p " +
                    " LEFT JOIN HospitalPatientInfo hpi On p.id=hpi.patient.id" +
                    " LEFT JOIN Hospital h ON h.id=hpi.hospital.id" +
                    " WHERE hpi.id=:hospitalPatientInfoId" +
                    " AND hpi.status='Y'";

    public static final String QUERY_TO_FETCH_LATEST_REGISTRATION_NUMBER =
            "SELECT" +
                    " MAX(hpi.registrationNumber)" +
                    " FROM" +
                    " HospitalPatientInfo hpi" +
                    " WHERE" +
                    " hpi.hospital.id = :hospitalId" +
                    " AND hpi.isRegistered='Y'";

    public static String QUERY_TO_SEARCH_PATIENT(PatientSearchRequestDTO searchRequestDTO) {
        return "SELECT" +
                " hpi.id as id," +
                " p.name as name," +                                             //[0]
                " hpi.address as address," +                                     //[1]
                " hpi.email as email," +                                         //[2]
                " p.mobileNumber as mobileNumber," +                             //[3]
                " hpi.registrationNumber as registrationNumber," +               //[4]
                " p.eSewaId as eSewaId," +                                       //[5]
                " p.gender as gender," +                                         //[6]
                " hpi.status as status," +                                       //[7]
                " p.dateOfBirth as dateOfBirth," +                               //[8]
                " hpi.hospitalNumber as hospitalNumber," +                       //[9]
                " h.name as hospitalName," +                                     //[10]
                " hpi.isRegistered as isRegistered," +                           //[11]
                QUERY_TO_CALCULATE_PATIENT_AGE +                                 //[12]
                " FROM Patient p" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id" +
                " LEFT JOIN Hospital h ON h.id=hpi.hospital.id" +
                " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id AND pmi.status='Y'" +
                GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(searchRequestDTO);
    }

    public static String QUERY_TO_GET_PATIENT_FOR_EXCEL(PatientSearchRequestDTO searchRequestDTO) {
        return "SELECT" +
                " p.name as patientName," +
                " p.mobile_number as contact," +
                " CASE" +
                " WHEN hpi.registration_number IS NULL THEN '-'" +
                " WHEN hpi.registration_number IS NOT NULL THEN hpi.registration_number END as registrationNumber," +
                " CASE" +
                " WHEN p.e_sewa_id IS NULL THEN '-'" +
                " WHEN p.e_sewa_id IS NOT NULL THEN p.e_sewa_id END as esewaId," +
                " CASE " +
                " When hpi.status ='Y' THEN 'Active'" +
                " When hpi.status ='N' THEN 'Inactive' END as status," +
                " CASE" +
                " WHEN hpi.hospital_number IS NULL THEN '-'" +
                " WHEN hpi.hospital_number IS NOT NULL THEN hpi.hospital_number END as hospitalNumber," +
                " h.name as client," +
                " CASE " +
                " When hpi.is_registered ='Y' THEN 'Registered'" +
                " When hpi.is_registered ='N' THEN 'New' END as patientType," +
                " p.gender as gender," +
                QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE +
                " FROM" +
                " patient p" +
                " LEFT JOIN hospital_patient_info hpi ON" +
                " hpi.patient_id = p.id" +
                " LEFT JOIN hospital h ON" +
                " h.id = hpi.hospital_id " +
                " LEFT JOIN patient_meta_info pmi ON pmi.patient_id = p.id AND pmi.status = 'Y'" +
                GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT_FOR_EXCEL(searchRequestDTO);
    }

    private static String GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT(PatientSearchRequestDTO searchRequestDTO) {
        String whereClause = " WHERE hpi.status!='D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getHospitalId()))
            whereClause += " AND h.id=" + searchRequestDTO.getHospitalId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getEsewaId()))
            whereClause += " AND p.eSewaId LIKE '%" + searchRequestDTO.getEsewaId() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND hpi.status='" + searchRequestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientMetaInfoId()))
            whereClause += " AND pmi.id=" + searchRequestDTO.getPatientMetaInfoId();

        whereClause += " ORDER BY p.id DESC";

        return whereClause;
    }

    private static String GET_WHERE_CLAUSE_FOR_SEARCH_PATIENT_FOR_EXCEL(PatientSearchRequestDTO searchRequestDTO) {
        String whereClause = " WHERE hpi.status!='D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getHospitalId()))
            whereClause += " AND h.id=" + searchRequestDTO.getHospitalId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getEsewaId()))
            whereClause += " AND p.e_sewa_id LIKE '%" + searchRequestDTO.getEsewaId() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND hpi.status='" + searchRequestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientMetaInfoId()))
            whereClause += " AND pmi.id=" + searchRequestDTO.getPatientMetaInfoId();

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
                    " h.name as hospitalName," +                                //[5]
                    " hpi.isRegistered as patientType," +                       //[6]
                    " hpi.registrationNumber as registrationNumber," +          //[7]
                    " p.eSewaId as eSewaId," +                                  //[8]
                    " atd.transactionNumber as transactionNumber," +            //[9]
                    " a.isSelf as isSelf," +                                    //[10]
                    " atd.appointmentAmount as appointmentAmount," +            //[11]
                    " a.appointmentModeId.name as appointmentMode," +            //[12]
                    QUERY_TO_CALCULATE_PATIENT_AGE + "," +                            //[13]
                    " a.id as appointmentId," +
                    " a.isFollowUp as isFollowUp" +
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON h.id=hpi.hospital.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                    " WHERE a.id =:appointmentId";

    public static String PATIENT_AUDITABLE_QUERY() {
        return " p.createdBy as createdBy," +
                " p.createdDate as createdDate," +
                " p.lastModifiedBy as lastModifiedBy," +
                " p.lastModifiedDate as lastModifiedDate";
    }

    public static final String QUERY_TO_FETCH_ESEWA_ID =
            " SELECT p.id as value," +                                      //[0]
                    " p.eSewaId as label" +                                 //[1]
                    " FROM Patient p " +
                    " LEFT JOIN HospitalPatientInfo hp ON p.id = hp.patient.id" +
                    " WHERE p.eSewaId IS NOT NULL" +
                    " AND hp.hospital.id =:hospitalId";

    public static final String QUERY_TO_CALCULATE_PATIENT_AGE_YEAR =
            " FLOOR(TIMESTAMPDIFF(YEAR, p.dateOfBirth ,CURDATE()))" +
                    " AS age";

    public static final String QUERY_TO_CALCULATE_PATIENT_AGE_MONTH =
            " FLOOR(TIMESTAMPDIFF(MONTH, p.dateOfBirth, CURDATE()) % 12)" +
                    " AS ageMonth";

    public static final String QUERY_TO_CALCULATE_PATIENT_AGE_DAY =
            " FLOOR(TIMESTAMPDIFF(DAY, p.dateOfBirth, CURDATE()) % 30.4375)" +
                    " AS ageDay";

}

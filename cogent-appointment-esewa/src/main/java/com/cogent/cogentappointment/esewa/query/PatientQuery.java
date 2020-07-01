package com.cogent.cogentappointment.esewa.query;

import java.util.Objects;

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
            " SELECT " +
                    " pm.parentPatientId.id as parentPatientId," +                 //[0]
                    " pm.childPatientId.id as childPatientId" +                   //[1]
                    " FROM Patient p" +
                    " LEFT JOIN PatientRelationInfo pm ON pm.parentPatientId.id= p.id" +
                    GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS +
                    " AND pm.status = 'Y'";

    public static String QUERY_TO_FETCH_MIN_PATIENT_INFO_FOR_OTHERS(String childPatientIds) {

        return " SELECT" +
                " hpi.id as hospitalPatientInfoId," +                    //[0]
                " p.id as patientId," +                                 //[1]
                " p.name as name," +                                    //[2]
                " p.mobileNumber as mobileNumber," +                    //[3]
                " p.dateOfBirth as dateOfBirth," +                       //[4]
                " p.gender as gender," +                                //[5]
                " hpi.address as address," +                            //[6]
                " hpi.registrationNumber as registrationNumber," +      //[7]
                " hpi.hospital.name as hospitalName," +                  //[8]
                QUERY_TO_CALCULATE_PATIENT_AGE +                        //[9]
                " FROM Patient p" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id = p.id" +
                " WHERE p.id IN (" + childPatientIds + ")";
    }

    public static String QUERY_TO_FETCH_MIN_PATIENT_DETAILS_FOR_OTHERS =
            SELECT_CLAUSE_TO_FETCH_PATIENT_DETAILS +
                    " FROM HospitalPatientInfo hpi " +
                    " LEFT JOIN Patient p ON p.id = hpi.patient.id" +
                    " WHERE hpi.id =:hospitalPatientInfoId";

    public static final String QUERY_TO_FETCH_PATIENT =
            " SELECT p FROM Patient p" +
                    " WHERE " +
                    " p.name =:name" +
                    " AND p.mobileNumber =:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth";

    /*PATIENT INFO HOSPITAL WISE*/
    /*FOR SELF*/
    public static final String QUERY_TO_FETCH_PATIENT_INFO_FOR_SELF =
            " SELECT p.id as patientId," +                                  //[0]
                    " p.name as name," +                                    //[1]
                    " p.mobileNumber as mobileNumber," +                    //[2]
                    " p.gender as gender," +                                //[3]
                    " p.dateOfBirth as dateOfBirth," +                      //[4]
                    QUERY_TO_CALCULATE_PATIENT_AGE +
                    " FROM Patient p" +
                    GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS;

    public static String QUERY_TO_FETCH_PATIENT_HOSPITAL_WISE_INFO(Long hospitalId) {

        String query = " SELECT " +
                " hpi.address as address," +                             //[0]
                " hpi.email as email," +                                 //[1]
                " hpi.registrationNumber as registrationNumber," +       //[2]
                " hpi.hasAddress as hasAddress,"+                        //[3]
                " COALESCE(pr.value, '') as province," +                  //[3]
                " COALESCE(d.value, '') as district," +                  //[4]
                " COALESCE(vm.value, '') as vdcOrMunicipality," +        //[5]
                " COALESCE(hpi.wardNumber, '') as wardNumber" +          //[6]
                " FROM Patient p" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id = p.id" +
                " LEFT JOIN Address pr ON pr.id = hpi.province.id" +
                " LEFT JOIN Address d ON d.id = hpi.district.id" +
                " LEFT JOIN Address vm ON vm.id = hpi.vdcOrMunicipality.id" +
                " WHERE p.id =:patientId";

        if (!Objects.isNull(hospitalId) && hospitalId != 0)
            query += " AND hpi.hospital.id =" + hospitalId;

        return query;
    }

    /*FOR OTHERS -> HOSPITAL WISE*/
    public static String QUERY_TO_FETCH_CHILD_PATIENT_IDS(Long hospitalId) {

        String query = " SELECT " +
                " pm.parentPatientId.id as parentPatientId," +                 //[0]
                " pm.childPatientId.id as childPatientId" +                   //[1]
                " FROM Patient p" +
                " LEFT JOIN PatientRelationInfo pm ON pm.parentPatientId.id= p.id" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id = pm.childPatientId.id" +
                GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS +
                " AND pm.status = 'Y'";

        if (!Objects.isNull(hospitalId) && hospitalId != 0)
            query += " AND hpi.hospital.id=" + hospitalId;

        return query;
    }
}

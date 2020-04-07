package com.cogent.cogentappointment.esewa.query;

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
                " p.gender as gender," +                                //[4]
                " hpi.address as address," +                            //[5]
                " hpi.registrationNumber as registrationNumber," +      //[6]
                QUERY_TO_CALCULATE_PATIENT_AGE +                        //[7]
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
}

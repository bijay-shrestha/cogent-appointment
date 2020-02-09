package com.cogent.cogentappointment.admin.query;

/**
 * @author Sauravi Thapa २०/२/७
 */
public class PatientMetaInfoQuery {


    public static String QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN =
            "SELECT" +
                    " pmi.id as value," +
                    " pmi.metaInfo as label" +
                    " FROM PatientMetaInfo pmi" +
                    " WHERE pmi.status='Y'";

    public static String QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN =
            "SELECT" +
                    " pmi.id as value," +
                    " pmi.metaInfo as label" +
                    " FROM PatientMetaInfo pmi" +
                    " WHERE pmi.status!='D'";

    public static String CLAUSE_TO_FIND_BY_HOSPITAL_ID = " AND pmi.patient.hospitalId.id=:hospitalId";

    public static String QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN_BY_HOSPITAL_ID =
            QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN +
                    CLAUSE_TO_FIND_BY_HOSPITAL_ID;

    public static String QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN_BY_HOSPITAL_ID =
            QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN +
                    CLAUSE_TO_FIND_BY_HOSPITAL_ID;


}

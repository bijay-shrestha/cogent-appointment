package com.cogent.cogentappointment.client.query;

/**
 * @author Sauravi Thapa २०/२/७
 */
public class PatientMetaInfoQuery {

    public static final String CLAUSE_TO_FIND_BY_HOSPITAL_ID = " AND hpi.hospitalId=:hospitalId";

    public static final String QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN =
            "SELECT" +
                    " pmi.id as value," +
                    " pmi.metaInfo as label" +
                    " FROM PatientMetaInfo pmi" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patientId=pmi.patient.id" +
                    " WHERE pmi.status='Y'" +
                    CLAUSE_TO_FIND_BY_HOSPITAL_ID;
    ;

    public static final String QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN =
            "SELECT" +
                    " pmi.id as value," +
                    " pmi.metaInfo as label" +
                    " FROM PatientMetaInfo pmi" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patientId=pmi.patient.id" +
                    " WHERE pmi.status!='D'" +
                    CLAUSE_TO_FIND_BY_HOSPITAL_ID;
    ;
}

package com.cogent.cogentappointment.client.query;

/**
 * @author Sauravi Thapa २०/२/७
 */
public class PatientMetaInfoQuery {

    private static final String SELECT_CLAUSE_TO_FETCH_MIN_PATIENT_META_INFO =
            "SELECT" +
                    " pmi.id as value," +
                    " pmi.metaInfo as label" +
                    " FROM PatientMetaInfo pmi" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id=pmi.patient.id" +
                    " WHERE hpi.hospital.id=:hospitalId";

    public static final String QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN =
            SELECT_CLAUSE_TO_FETCH_MIN_PATIENT_META_INFO +
                    " AND pmi.status='Y'";

    public static final String QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN =
            SELECT_CLAUSE_TO_FETCH_MIN_PATIENT_META_INFO +
                    " AND pmi.status!='D'";
}

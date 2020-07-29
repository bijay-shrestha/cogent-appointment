package com.cogent.cogentappointment.admin.query;

/**
 * @author Sauravi Thapa २०/२/७
 */
public class PatientMetaInfoQuery {

    private static String QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN =
            "SELECT" +
                    " pmi.id as value," +
                    " pmi.metaInfo as label" +
                    " FROM PatientMetaInfo pmi" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id=pmi.patient.id" +
                    " WHERE pmi.status='Y'";

    private static String QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN =
            "SELECT" +
                    " pmi.id as value," +
                    " pmi.metaInfo as label" +
                    " FROM PatientMetaInfo pmi" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id=pmi.patient.id" +
                    " WHERE pmi.status!='D'";

    public static String QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN_BY_HOSPITAL_ID(Long hospitalId) {
        return QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN + CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }

    public static String QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN_BY_HOSPITAL_ID(Long hospitalId) {
        return QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN + CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }

    private static String CLAUSE_TO_FIND_BY_HOSPITAL_ID(Long hospitalId) {
        if (hospitalId != 0)
            return " AND hpi.hospital.id=" + hospitalId + " ORDER BY label ASC";

        return " ORDER BY label ASC";
    }
}

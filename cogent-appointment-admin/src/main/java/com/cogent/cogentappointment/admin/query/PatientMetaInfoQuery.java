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
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patientId=pmi.patient.id" +
                    " WHERE pmi.status='Y'";

    public static String QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN =
            "SELECT" +
                    " pmi.id as value," +
                    " pmi.metaInfo as label" +
                    " FROM PatientMetaInfo pmi" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patientId=pmi.patient.id" +
                    " WHERE pmi.status!='D'";

    public static String QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN_BY_HOSPITAL_ID (Long hospitalId){
        return  QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN +CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }


    public static String QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN_BY_HOSPITAL_ID (Long hospitalId) {
        return   QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN + CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }


    public static String CLAUSE_TO_FIND_BY_HOSPITAL_ID(Long hospitalId){
        if(hospitalId!=0){
           return  " AND hpi.hospitalId="+ hospitalId;
        }
        return "";
    }

}

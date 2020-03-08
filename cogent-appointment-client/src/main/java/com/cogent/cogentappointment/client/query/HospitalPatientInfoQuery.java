package com.cogent.cogentappointment.client.query;

/**
 * @author smriti on 28/02/20
 */
public class HospitalPatientInfoQuery {

    public static final String QUERY_TO_FETCH_HOSPITAL_PATIENT_INFO_COUNT =
            " SELECT COUNT(h.id) FROM HospitalPatientInfo h" +
                    " WHERE" +
                    " h.patient.id=:patientId " +
                    " AND h.hospital.id =:hospitalId";

    public static final String QUERY_TO_FETCH_HOSPITAL_PATIENT_INFO =
            " SELECT h FROM HospitalPatientInfo h" +
                    " WHERE" +
                    " h.patient.id=:patientId " +
                    " AND h.hospital.id =:hospitalId";
}

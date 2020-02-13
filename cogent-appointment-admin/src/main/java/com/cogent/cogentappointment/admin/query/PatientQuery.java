package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientQuery {

    public static final String QUERY_TO_FETCH_LATEST_REGISTRATION_NUMBER =
            " SELECT registration_number" +
                    " FROM hospital_patient_info p " +
                    " WHERE" +
                    " registration_number IS NOT NULL" +
                    " ORDER BY id DESC" +
                    " LIMIT 1";
}

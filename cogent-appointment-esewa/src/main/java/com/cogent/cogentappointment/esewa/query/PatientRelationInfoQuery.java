package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 28/02/20
 */
public class PatientRelationInfoQuery {

    public static final String QUERY_TO_FETCH_PATIENT_RELATION_INFO =
            " SELECT p FROM PatientRelationInfo p" +
                    " WHERE" +
                    " p.parentPatientId.id =:parentPatientId" +
                    " AND p.childPatientId.id=:childPatientId";
}

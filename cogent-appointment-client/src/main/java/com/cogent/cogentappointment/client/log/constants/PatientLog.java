package com.cogent.cogentappointment.client.log.constants;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientLog {
    public final static String PATIENT = "PATIENT";

    public final static String HOSPITAL_PATIENT_INFO = "HOSPITAL PATIENT INFO";

    public final static String PATIENT_META_INFO = "PATIENT META INFO";

    public final static String PATIENT_RELATION_INFO = "PATIENT RELATION INFO";

    public final static String PATIENT_NOT_FOUND_BY_APPOINTMENT_ID=":::: PATIENT WITH APPOINTMENT ID : {} NOT FOUND ::::";

    public final static String PATIENT_NOT_FOUND_BY_HOSPITAL_PATIENT_INFO_ID=":::: PATIENT WITH HOSPITAL PATIENT INFO ID" +
            " : {} NOT FOUND ::::";

    public final static String PATIENT_NOT_FOUND_BY_NAME=":::: PATIENT BY NAME : {} NOT FOUND ::::";

    public static final String REGISTERING_PATIENT_PROCESS_STARTED = ":::: REGISTERING PATIENT PROCESS STARTED ::::";

    public static final String REGISTERING_PATIENT_PROCESS_COMPLETED = ":::: REGISTERING PATIENT PROCESS COMPLETED" +
            " IN {} ms ::::";

    public static final String PATIENT_ESEWA_ID = "PATIENT eSewa ID";
}

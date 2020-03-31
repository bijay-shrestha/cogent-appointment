package com.cogent.cogentappointment.admin.log.constants;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientLog {
    public  static final String PATIENT= "PATIENT";

    public  static final String PATIENT_META_INFO= "PATIENT META INFO";


    public static final String REGISTERING_PATIENT_PROCESS_STARTED = ":::: REGISTERING PATIENT PROCESS STARTED ::::";

    public static final String REGISTERING_PATIENT_PROCESS_COMPLETED = ":::: REGISTERING PATIENT PROCESS COMPLETED" +
            " IN {} ms ::::";

    public final static String PATIENT_NOT_FOUND_BY_APPOINTMENT_ID="::: Patient with appointmentId : {} not found :::";

    public final static String PATIENT_NOT_FOUND_BY_HOSPITAL_PATIENT_INFO_ID="::: Patient with hospitalPatientInfoId" +
            " : {} not found :::";

    public final static String PATIENT_NOT_FOUND_BY_NAME="::: Patient by name : {} not found :::";


}

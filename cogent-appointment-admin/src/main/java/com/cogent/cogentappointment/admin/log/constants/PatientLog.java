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

    public static final String DUPLICATE_PATIENT_ERROR_MESSAGE = "Patient already exists with name: {}, mobile number: {} and" +
            " date of birth: {}";

    public final static String PATIENT_NOT_FOUND="::: Patient with id : {} not found :::";
}

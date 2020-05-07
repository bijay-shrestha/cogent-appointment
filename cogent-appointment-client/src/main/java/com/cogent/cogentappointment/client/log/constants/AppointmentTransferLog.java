package com.cogent.cogentappointment.client.log.constants;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public class AppointmentTransferLog {
    public final static String APPOINTMENT_TRANSFER = "APPOINTMENT TRANSFER";

    public static String FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_STARTED = ":::: FETCHING AVAILABLE DOCTOR DATES " +
            "BY DOCTOR ID PROCESS STARTED FOR {} ::::";
    public static String FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_COMPLETED = ":::: FETCHING AVAILABLE DOCTOR DATES" +
            " BY DOCTOR ID PROCESS  COMPLETED FOR {} :::: {} ms";

    public static String FETCHING_AVAILABLE_DOCTOR_TIME_PROCESS_STARTED = ":::: FETCHING AVAILABLE DOCTOR TIME " +
            "BY DOCTOR ID AND DATE PROCESS STARTED FOR {} ::::";
    public static String FETCHING_AVAILABLE_DOCTOR_TIME_PROCESS_COMPLETED = ":::: FETCHING AVAILABLE DOCTOR DATES" +
            " BY DOCTOR ID AND DATE PROCESS  COMPLETED FOR {} :::: {} ms";
}

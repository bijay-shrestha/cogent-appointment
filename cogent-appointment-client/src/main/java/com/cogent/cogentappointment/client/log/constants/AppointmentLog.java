package com.cogent.cogentappointment.client.log.constants;

/**
 * @author smriti on 2019-10-24
 */
public class AppointmentLog {
    public final static String APPOINTMENT = "APPOINTMENT";

    public static String PENDING_APPOINTMENTS = "PENDING APPOINTMENTS";

    public static String CHECK_AVAILABILITY_PROCESS_STARTED = ":::: CHECKING APPOINTMENT AVAILABILITY PROCESS STARTED ::::";
    public static String CHECK_AVAILABILITY_PROCESS_COMPLETED = ":::: CHECKING APPOINTMENT AVAILABILITY PROCESS COMPLETED IN :::: {} ms";

    public static String CANCELLING_PROCESS_STARTED = ":::: CANCELLING APPOINTMENT PROCESS STARTED ::::";
    public static String CANCELLING_PROCESS_COMPLETED = ":::: CANCELLING APPOINTMENT PROCESS COMPLETED IN :::: {} ms";

    public static String RESCHEDULE_PROCESS_STARTED = ":::: RESCHEDULE APPOINTMENT PROCESS STARTED ::::";
    public static String RESCHEDULE_PROCESS_COMPLETED = ":::: RESCHEDULE APPOINTMENT PROCESS COMPLETED IN :::: {} ms";

    public final static String APPOINTMENT_RESCHEDULE_LOG = "APPOINTMENT RESCHEDULE LOG";

    public final static String APPOINTMENT_STATUS = "APPOINTMENT STATUS";

    public final static String APPOINTMENT_TODAY_QUEUE = "APPOINTMENT QUEUE FOR LOG";

}

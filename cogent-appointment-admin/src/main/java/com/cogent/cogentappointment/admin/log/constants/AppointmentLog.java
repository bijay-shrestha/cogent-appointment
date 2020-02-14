package com.cogent.cogentappointment.admin.log.constants;

/**
 * @author smriti on 2019-10-24
 */
public class AppointmentLog {
    public final static String APPOINTMENT = "APPOINTMENT";

    public final static String APPOINTMENT_REFUND = "REFUND REQUEST APPOINTMENT";

    public final static String APPOINTMENT_LOG = "APPOINTMENT LOG";

    public final static String APPOINTMENT_RESCHEDULE_LOG = "APPOINTMENT RESCHEDULE LOG";

    public final static String APPOINTMENT_STATUS = "APPOINTMENT STATUS";

    public static String PENDING_APPROVAL_LIST = "PENDING APPROVALS";

    public final static String APPROVE_PROCESS_STARTED = ":::: APPROVING {} STARTED ::::";
    public final static String APPROVE_PROCESS_COMPLETED = ":::: APPROVING {} COMPLETED IN {} ms ::::";

    public final static String REJECT_PROCESS_STARTED = ":::: REJECTING {} STARTED ::::";
    public final static String REJECT_PROCESS_COMPLETED = ":::: REJECTING {} COMPLETED IN {} ms ::::";

    public static String FETCHING_PROCESS_STARTED = ":::: FETCHING BOOKED APPOINTMENT PROCESS STARTED ::::";
    public static String FETCHING_PROCESS_COMPLETED = ":::: FETCHING BOOKED APPOINTMENT PROCESS COMPLETED IN :::: {} ms";
}

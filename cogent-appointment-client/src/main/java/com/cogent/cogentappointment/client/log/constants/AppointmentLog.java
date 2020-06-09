package com.cogent.cogentappointment.client.log.constants;

/**
 * @author smriti on 2019-10-24
 */
public class AppointmentLog {
    public final static String APPOINTMENT = "APPOINTMENT";

    public final static String CHECK_AVAILABILITY_PROCESS_STARTED = ":::: CHECKING APPOINTMENT AVAILABILITY " +
            "PROCESS STARTED ::::";
    public final static String CHECK_AVAILABILITY_PROCESS_COMPLETED = ":::: CHECKING APPOINTMENT AVAILABILITY" +
            " PROCESS COMPLETED IN :::: {} ms";

    public final static String CANCELLING_PROCESS_STARTED = ":::: CANCELLING APPOINTMENT PROCESS STARTED ::::";
    public final static String CANCELLING_PROCESS_COMPLETED = ":::: CANCELLING APPOINTMENT PROCESS " +
            "COMPLETED IN :::: {} ms";

    public final static String RESCHEDULE_PROCESS_STARTED = ":::: RESCHEDULE APPOINTMENT PROCESS STARTED ::::";
    public final static String RESCHEDULE_PROCESS_COMPLETED = ":::: RESCHEDULE APPOINTMENT PROCESS " +
            "COMPLETED IN :::: {} ms";

    public final static String APPROVE_PROCESS_STARTED = ":::: APPROVING {} STARTED ::::";
    public final static String APPROVE_PROCESS_COMPLETED = ":::: APPROVING {} COMPLETED IN {} ms ::::";

    public final static String REJECT_PROCESS_STARTED = ":::: REJECTING {} STARTED ::::";
    public final static String REJECT_PROCESS_COMPLETED = ":::: REJECTING {} COMPLETED IN {} ms ::::";

    public final static String APPOINTMENT_CANCEL_APPROVAL = "APPOINTMENT CANCEL APPROVALS";

    public final static String APPOINTMENT_LOG = "APPOINTMENT LOG";

    public final static String TRANSACTION_LOG = "TRANSACTION LOG";

    public final static String PENDING_APPOINTMENTS = "PENDING APPOINTMENTS";

    public final static String APPOINTMENT_RESCHEDULE_LOG = "APPOINTMENT RESCHEDULE LOG";

    public final static String APPOINTMENT_STATUS = "APPOINTMENT STATUS";

    public final static String DEPARTMENT_APPOINTMENT_STATUS = "DEPARTMENT APPOINTMENT STATUS";

    public final static String DEPARTMENT_APPOINTMENT_STATUS_ROOM_WISE = "DEPARTMENT APPOINTMENT STATUS ROOM-WISE";

    public final static String APPOINTMENT_TODAY_QUEUE = "APPOINTMENT QUEUE FOR LOG";

    public final static String PENDING_APPROVAL_LIST = "PENDING APPROVALS";

    public final static String PENDING_APPROVAL_DETAIL = "PENDING APPROVALS DETAIL";


}

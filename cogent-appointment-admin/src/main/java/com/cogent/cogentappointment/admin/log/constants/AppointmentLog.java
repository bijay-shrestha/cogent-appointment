package com.cogent.cogentappointment.admin.log.constants;

/**
 * @author smriti on 2019-10-24
 */
public class AppointmentLog {
    public final static String APPOINTMENT = "APPOINTMENT";

    public final static String APPOINTMENT_REFUND = "REFUND REQUEST APPOINTMENT";

    public final static String APPOINTMENT_REFUND_DETAIL = "APPOINTMENT REFUND DETAIL";

    public final static String APPOINTMENT_LOG = "APPOINTMENT LOG";

    public final static String HOSPITAL_DEPARTMENT_APPOINTMENT_LOG = "HOSPITAL DEPARTMENT APPOINTMENT LOG";

    public final static String TRANSACTION_LOG = "TRANSACTION LOG";

    public final static String HOSPITAL_DEPARTMENT_TRANSACTION_LOG = "HOSPITAL DEPARTMENT TRANSACTION LOG";

    public final static String APPOINTMENT_RESCHEDULE_LOG = "APPOINTMENT RESCHEDULE LOG";

    public final static String HOSPITAL_DEPARTMENT_APPOINTMENT_RESCHEDULE_LOG = "HOSPITAL_DEPARTMENT APPOINTMENT " +
            "RESCHEDULE LOG";

    public final static String APPOINTMENT_STATUS = "APPOINTMENT STATUS";

    public final static String DEPARTMENT_APPOINTMENT_STATUS = "DEPARTMENT APPOINTMENT STATUS";

    public final static String DEPARTMENT_APPOINTMENT_STATUS_ROOM_WISE = "DEPARTMENT APPOINTMENT STATUS ROOM-WISE";

    public final static String APPOINTMENT_TODAY_QUEUE = "APPOINTMENT QUEUE FOR LOG";

    public final static String PENDING_APPOINTMENT_APPROVAL = "PENDING APPOINTMENT APPROVALS";

    public static String PENDING_APPROVAL_DETAIL = "PENDING APPROVALS DETAIL";

    public final static String APPROVE_PROCESS_STARTED = ":::: APPROVING {} STARTED ::::";
    public final static String APPROVE_PROCESS_COMPLETED = ":::: APPROVING {} COMPLETED IN {} ms ::::";

    public final static String REJECT_PROCESS_STARTED = ":::: REJECTING {} STARTED ::::";
    public final static String REJECT_PROCESS_COMPLETED = ":::: REJECTING {} COMPLETED IN {} ms ::::";

    public final static String FETCHING_PROCESS_STARTED = ":::: FETCHING BOOKED APPOINTMENT PROCESS STARTED ::::";
    public final String FETCHING_PROCESS_COMPLETED = ":::: FETCHING BOOKED APPOINTMENT PROCESS COMPLETED" +
            " IN :::: {} ms";

    public final static String APPOINTMENT_DOCTOR_INFO = "APPOINTMENT DOCTOR INFO";

}

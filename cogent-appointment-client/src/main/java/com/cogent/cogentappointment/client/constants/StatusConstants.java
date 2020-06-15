package com.cogent.cogentappointment.client.constants;

/**
 * @author smriti on 2019-09-20
 */
public class StatusConstants {

    public static final Character ACTIVE = 'Y';
    public static final Character INACTIVE = 'N';
    public static final Character DELETED = 'D';

    public static final Character YES = 'Y';
    public static final Character NO = 'N';



    public interface AppointmentStatusConstants {
        String PENDING_APPROVAL = "PA";
        String APPROVED = "A";
        String CANCELLED = "C";
        String REJECTED = "R";
        String RESCHEDULED = "RES";
        String REFUNDED = "RE";
        String VACANT = "V";
        String ALL = "";
    }
}

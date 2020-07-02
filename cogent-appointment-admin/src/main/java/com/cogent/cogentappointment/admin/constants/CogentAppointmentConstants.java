package com.cogent.cogentappointment.admin.constants;

/**
 * @author Sauravi Thapa ON 5/26/20
 */
public class CogentAppointmentConstants {

    /**
     * This Should be equal with Appointment Mode Table data
     */
    public interface AppointmentModeConstant {
        String APPOINTMENT_MODE_ESEWA_CODE = "eSewa";
        String APPOINTMENT_MODE_FONEPAY_CODE = "FP";
    }

    public interface RefundResponseConstant {
        String COMPLETE = "COMPLETE";
        String PARTIAL_REFUND = "PARTIAL_REFUND";
        String FULL_REFUND = "FULL_REFUND";
        String SUCCESS = "SUCCESS";
        String AMBIGIOUS = "AMBIGIOUS";
    }

    public interface AppointmentServiceTypeConstant {
        String DOCTOR_CONSULTATION_CODE = "DOC";
        String DEPARTMENT_CONSULTATION_CODE = "DEP";
    }

}

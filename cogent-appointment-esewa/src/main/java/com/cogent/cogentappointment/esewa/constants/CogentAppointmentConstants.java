package com.cogent.cogentappointment.esewa.constants;

/**
 * @author smriti on 05/06/20
 */
public class CogentAppointmentConstants {

    public interface AppointmentServiceTypeConstant {
        String DOCTOR_CONSULTATION_CODE = "DOC";
        String DEPARTMENT_CONSULTATION_CODE = "DEP";
    }

    /**
     * This Should be equal with Appointment Mode Table data
     */
    public interface AppointmentModeConstant{
        String APPOINTMENT_MODE_ESEWA_CODE="eSewa";
        String APPOINTMENT_MODE_FONEPAY_CODE="FP";
    }
}

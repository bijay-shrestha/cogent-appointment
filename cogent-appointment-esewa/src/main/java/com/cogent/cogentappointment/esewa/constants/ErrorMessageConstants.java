package com.cogent.cogentappointment.esewa.constants;

/**
 * @author smriti on 7/9/19
 */
public class ErrorMessageConstants {

    //A

    public interface AppointmentServiceMessage {
        String APPOINTMENT_EXISTS = "Appointment already exists for the selected time: '%s'";

        String INVALID_APPOINTMENT_TIME = "Invalid appointment time: '%s'";

        String INVALID_APPOINTMENT_DATE_TIME = "Sorry! Appointment failed because requested date/time has already passed.";

        String APPOINTMENT_FAILED_MESSAGE = "The selected time slot has expired. Please try again!";

        String APPOINTMENT_FAILED_DEBUG_MESSAGE = "AppointmentReservationLog(s) is null";

        String INVALID_TRANSACTION_NUMBER = "Transaction number '%s' and patient '%s' not found";
    }

    public final static String AUTH_HEADER_NULL = "AUTH HEADER NULL";


    public final static String HMAC_BAD_SIGNATURE = "HmacAccessFilter.badSignature";

    //C
    public final static String CANNOT_CREATE_SIGNATURE = "Cannot create signature: ";

    public static final String CODE_DUPLICATION_MESSAGE = "%s already exists with code '%s'";


    //D
    public interface DoctorServiceMessages {
        String DOCTOR_NOT_AVAILABLE = "Sorry, Doctor is not available in selected date '%s'.";
    }

    //E

    //F


    //G
    public static final String INVALID_GENDER_CODE_MESSAGE = "Invalid Gender code.";
    public static final String INVALID_GENDER_CODE_DEBUG_MESSAGE = "Gender enum doesn't have the requested code.";

    //H
    public static final String HOSPITAL_NULL_MESSAGE = " Hospital id must not be null";


    //I

    public static final String INVALID_COMPANY_CODE = "INVALID COMPANY CODE";

    public static final String UNAUTHORIZED = "UNAUTHORIZED";

    //J

    //K

    //L

    //M

    //N
    public static final String NAME_DUPLICATION_MESSAGE = "%s already exists with name '%s'";


    public static final String NAME_AND_CODE_DUPLICATION_MESSAGE = "%s already exists with name '%s' " +
            "and code '%s' ";


    //O

    //P
    public interface PatientServiceMessages {
        String DUPLICATE_PATIENT_MESSAGE = "Patient already exists with name '%s', mobile number '%s' and date of birth " +
                "'%s'";
    }


    //Q

    //R


    //S


    //T


    //U

    //V

    //W

    //X

    //Y

    //Z
}

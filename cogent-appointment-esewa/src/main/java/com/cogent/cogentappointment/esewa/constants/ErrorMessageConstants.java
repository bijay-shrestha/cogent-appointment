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

        String INVALID_APPOINTMENT_DATE = "Sorry! Appointment failed because requested date has already passed.";

        String APPOINTMENT_FAILED_MESSAGE = "The selected time slot has expired. Please try again!";

        String APPOINTMENT_FAILED_DEBUG_MESSAGE = "AppointmentReservationLog(s) is null";

        String INVALID_TRANSACTION_NUMBER = "Transaction number '%s' and patient '%s' not found";

        String ESEWA_ID_CANNOT_BE_NULL = "eSewa Id cannot be null";

        String INVALID_APPOINTMENT_SERVICE_TYPE_CODE = "Invalid Appointment Service Type Code : '%s'";

        String INVALID_HOSPITAL_APPOINTMENT_SERVICE_TYPE = "Requested appointment service type is not assigned to hospital '%s'";

        String INVALID_ADDRESS_INFO = "Sorry, address information cannot be null or empty";
    }

    public interface AppointmentHospitalDepartmentMessage {
        String INVALID_ROOM_CHECK_AVAILABILITY_REQUEST = "Sorry, Requested room is not assigned to department duty roster";

        String HOSPITAL_DEPARTMENT_APPOINTMENT_CHARGE_INVALID = "Sorry, requested appointment amount '%s' doesn't match with " +
                "actual hospital department appointment charge";
        String HOSPITAL_DEPARTMENT_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE = "Appointment amount is not equal to " +
                "Hospital Department actual appointment charge/ follow-up appointment charge";

        String APPOINTMENT_AVAILABLE_DATE_NOT_FOUND = "No available appointment date(s) found";
    }

    public final static String HMAC_BAD_SIGNATURE = "HmacAccessFilter.badSignature";

    //C
    public final static String CANNOT_CREATE_SIGNATURE = "Cannot create signature: ";

    public static final String CODE_DUPLICATION_MESSAGE = "%s already exists with code '%s'";


    //D
    public interface DoctorServiceMessages {
        String DOCTOR_NOT_AVAILABLE = "Sorry, Doctor is not available in selected date '%s'.";
        String DOCTOR_APPOINTMENT_CHARGE_INVALID = "Sorry, requested appointment amount '%s' doesn't match with " +
                "actual doctor appointment charge";
        String DOCTOR_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE = "Appointment amount is not equal to " +
                "Doctor actual appointment charge/ follow-up appointment charge";
    }

    //E
    public static final String ERROR_VALIDATING_ENCRYPTED_REQUEST="Error occurred while validating encrypted request :: {}";

    //F


    //G
    public static final String INVALID_GENDER_CODE_MESSAGE = "Invalid Gender code.";
    public static final String INVALID_GENDER_CODE_DEBUG_MESSAGE = "Gender enum doesn't have the requested code.";

    //H
    public static final String HOSPITAL_NULL_MESSAGE = " Hospital id must not be null";

    public interface HospitalDepartmentDutyRosterMessages {
        String HOSPITAL_DEPARTMENT_NOT_AVAILABLE_MESSAGE = "Sorry, Selected Hospital Department is not available" +
                " on date '%s'.";

        String HOSPITAL_DEPARTMENT_NOT_AVAILABLE_DEBUG_MESSAGE = "Sorry, Selected Hospital Department has day off" +
                " on date '%s'.";
    }


    //I
    public static final String INVALID_DATE_MESSAGE = "From date cannot be greater than to date";
    public static final String INVALID_DATE_DEBUG_MESSAGE = "Failed to complete operation due to invalid from and to date";

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
    public static final String REQUEST_BODY_IS_NULL="Request body is null";


    //S


    //T


    //U

    //V

    //W

    //X

    //Y

    //Z
}

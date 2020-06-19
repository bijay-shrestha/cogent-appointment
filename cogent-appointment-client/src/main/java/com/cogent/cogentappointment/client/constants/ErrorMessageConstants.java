package com.cogent.cogentappointment.client.constants;

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

        String APPOINTMENT_FAILED_MESSAGE = "The selected time slot has expired/or is invalid. Please try again!";

        String APPOINTMENT_FAILED_DEBUG_MESSAGE = "AppointmentReservationLog(s) is null";
    }

    public interface AdminServiceMessages {

        String ADMIN_DUPLICATION_MESSAGE = "Admin with email '%s' and mobile number" +
                " '%s' already exists.";

        String ADMIN_DUPLICATION_IN_DIFFERENT_HOSPITAL_MESSAGE = "Cannot create Admin with email '%s'" +
                " and mobile number '%s'.";

        String EMAIL_DUPLICATION_MESSAGE = "Admin with email '%s' already exists.";

        String EMAIL_DUPLICATION_IN_DIFFERENT_HOSPITAL_MESSAGE = "Cannot create Admin with email '%s'.";

        String MOBILE_NUMBER_DUPLICATION_MESSAGE = "Admin with mobile number '%s' already exists  .";

        String MOBILE_NUMBER_DUPLICATION_IN_DIFFERENT_HOSPITAL_MESSAGE = "Cannot create Admin with" +
                " and mobile number '%s'.";

        String ADMIN_NOT_FOUND = "Admin '%s' doesn't exist";

        String ADMIN_NOT_ACTIVE = "Admin '%s' is not active";

        String ADMIN_REGISTERED = "Admin is already registered";

        String DUPLICATE_PASSWORD_MESSAGE = "New Password must be different than current password.";

        String INVALID_CONFIRMATION_TOKEN = "Invalid Confirmation Link. Please try again.";

        String PASSWORD_MISMATCH_MESSAGE = "Old password doesn't match.";

        String ADMIN_INFO_NOT_FOUND = "Admin info is not found.";

        String ADMIN_CANNOT_BE_REGISTERED_MESSAGE = "Admin exceeds for the selected hospital";

        String ADMIN_CANNOT_BE_REGISTERED_DEBUG_MESSAGE = "Admin count is equal to hospital's allowed number of admins";

        String INVALID_DELETE_REQUEST = "Sorry! Cannot delete this admin since it has Super Admin Profile";

        String ACCOUNT_NOT_ACTIVATED_MESSAGE = "Sorry '%s', please activate your account first.";
        String ACCOUNT_NOT_ACTIVATED_DEBUG_MESSAGE = "'status' flag in Admin entity is 'N'";

        String BAD_UPDATE_MESSAGE = "Operation failed, save password pending.";
        String BAD_UPDATE_DEBUG_MESSAGE = "Admin hasn't saved its password yet and is still inactive.";
    }

    public interface AppointmentTransferMessage {
        String APPOINTMENT_DOCTOR_INFORMATION_NOT_FOUND = "Appointment Doctor Information not found";

    }

    public final static String HMAC_BAD_SIGNATURE = "HmacAccessFilter.badSignature";

    //C
    public final static String CANNOT_CREATE_SIGNATURE = "Cannot create signature: ";

    public static final String CODE_DUPLICATION_MESSAGE = "%s already exists with code '%s'";

    //D

    public interface DashboardMessages {
        String DOCTOR_REVENUE_NOT_FOUND = "Doctor Revenue(s) not found";
    }

    public interface DoctorServiceMessages {
        String DOCTOR_NOT_AVAILABLE = "Sorry, Doctor is not available in selected date '%s'.";
        String DOCTOR_APPOINTMENT_CHARGE_INVALID = "Sorry, requested appointment amount '%s' doesn't match with " +
                "actual doctor appointment charge";
        String DOCTOR_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE = "Appointment amount is not equal to " +
                "Doctor actual appointment charge/ follow-up appointment charge";
    }


    public interface DoctorDutyRosterServiceMessages {
        String DUPLICATION_MESSAGE = "Doctor Duty Roster already exists for selected doctor.";

        String BAD_REQUEST_MESSAGE = "Doctor Duty Roster Override doesn't lie within the duty roster date range.";

        String APPOINTMENT_EXISTS_MESSAGE = "Cannot update doctor duty roster because appointment" +
                " exists within the selected date range.";

        String APPOINTMENT_EXISTS_ON_WEEK_DAY_MESSAGE = "Cannot update doctor duty roster because appointment" +
                " exists within the selected date range '%s'.";
    }

    //E
    public static final String ESEWA_REFUND_API_BAD_REQUEST_MESSAGE = "Third Party API Bad Request...";
    public static final String ESEWA_REFUND_API_NOT_FOUND_MESSAGE = "Third Party API Not Found Exception...";
    public static final String ESEWA_REFUND_API_FORBIDDEN_MESSAGE = "Third Party API Forbidden";

    //F
    public interface ForgotPasswordMessages {
        String INVALID_RESET_CODE = "Invalid Password Reset Code. Please try again.";
        String RESET_CODE_EXPIRED = "Reset code has expired. Request a new password reset code.";
    }


    //G
    public static final String INVALID_GENDER_CODE_MESSAGE = "Invalid Gender code.";
    public static final String INVALID_GENDER_CODE_DEBUG_MESSAGE = "Gender enum doesn't have the requested code.";

    //H
    public static final String HOSPITAL_NULL_MESSAGE = " Hospital id must not be null";

    public interface HospitalDeptDutyRosterMessages {
        String BAD_REQUEST_MESSAGE = "Department Duty Roster Override doesn't lie within the actual" +
                " duty roster date range '%s' to '%s'";

        String BAD_ROOM_REQUEST = "If room is enabled, corresponding room details is also required";

        String DUPLICATE_DUTY_ROSTER_WITHOUT_ROOM =
                "Department Duty Roster already exists within '%s' to '%s'";

        String DUPLICATE_DUTY_ROSTER_WITH_ROOM =
                "Department Duty Roster already exists within '%s' to '%s' for selected room";

        String DUPLICATE_DUTY_ROSTER_OVERRIDE_WITHOUT_ROOM =
                "Department Duty Roster Override already exists within '%s' to '%s'";

        String DUPLICATE_DUTY_ROSTER_OVERRIDE_WITH_ROOM =
                "Department Duty Roster Override already exists within '%s' to '%s' for selected room";

        String HOSPITAL_DEPARTMENT_DOCTOR_NOT_ASSIGNED = "Sorry, Requested doctor of '%s' are not assigned to " +
                "the selected hospital department";
    }

    //I
    public interface IntegrationApiMessages {
        String INTEGRATION_PARAMETER_NOT_FOUND = "Integration parameters not found";
        String INTEGRATION_PARAMETER_IS_NULL = "Integration parameter return null";

        String INTEGRATION_BHERI_HOSPITAL_ERROR = "An error occurred while saving the patient record.";
        String INTEGRATION_BHERI_HOSPITAL_FORBIDDEN_ERROR = "Hospital API Forbidden...";
        String INTEGRATION_API_BAD_REQUEST = "Bad Third Party API Request.";
        String INVALID_INTEGRATION_CHANNEL_CODE = "Requested Integration Channel Code '%s' is invalid";
    }


    public static final String INVALID_DATE_MESSAGE = "From date cannot be greater than to date";
    public static final String INVALID_DATE_DEBUG_MESSAGE = "Failed to complete operation due to invalid from and to date";

    public static final String INVALID_DATE_TIME_MESSAGE = "Start time cannot be greater than end time";
    public static final String INVALID_DATE_TIME_DEBUG_MESSAGE = "Failed to complete operation due to invalid start and end date-time";

    public static final String EQUAL_DATE_TIME_MESSAGE = "Start time cannot be equal to end time";
    public static final String EQUAL_DATE_TIME_DEBUG_MESSAGE = "Failed to complete operation due to invalid start and end date-time";


    public static final String INVALID_USERNAME_OR_ACCESS_KEY = "Invalid username or access key";
    public static final String INVALID_PASSWORD = "Invalid password";

    public static final String INVALID_VERIFICATION_TOKEN = "Invalid verification token";

    //J

    //K

    //L

    //M

    //N
    public static final String NAME_DUPLICATION_MESSAGE = "%s already exists with name '%s'";
    public static final String NAME_AND_MOBILE_NUMBER_DUPLICATION_MESSAGE = "%s already exists with name '%s' " +
            "and mobile number '%s'";

    public static final String NAME_AND_CODE_DUPLICATION_MESSAGE = "%s already exists with name '%s' " +
            "and code '%s' ";

    public static String NO_RECORD_FOUND = "No %s(s) found.";


    //O

    //P
    public interface PatientServiceMessages {
        String DUPLICATE_PATIENT_MESSAGE = "Patient already exists with name '%s', mobile number '%s' and date of birth " +
                "'%s'";

    }

    public interface ProfileServiceMessages {
        String INVALID_DELETE_REQUEST = "Sorry! Cannot delete Super Admin Profile";
    }

    //Q

    //R
    public static final String ROOM_NUMBER_DUPLICATION_MESSAGE = "Room Number '%s' already in use";


    //S


    //T


    //U

    //V

    //W

    //X

    //Y

    //Z
}

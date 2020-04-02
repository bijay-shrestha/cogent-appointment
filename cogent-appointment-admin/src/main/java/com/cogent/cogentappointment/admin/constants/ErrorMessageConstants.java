package com.cogent.cogentappointment.admin.constants;

/**
 * @author smriti on 7/9/19
 */
public class ErrorMessageConstants {

    //A

    public interface AdminServiceMessages {

        String ADMIN_DUPLICATION_MESSAGE = "Admin with given username, email and mobile number" +
                " already exists.";

        String USERNAME_DUPLICATION_MESSAGE = "%s already exists with username '%s'";

        String EMAIL_DUPLICATION_MESSAGE = "%s already exists with email '%s'";

        String MOBILE_NUMBER_DUPLICATION_MESSAGE = "%s already exists with mobile number '%s'";

        String ADMIN_NOT_FOUND = "Admin '%s' doesn't exist";

        String ADMIN_NOT_ACTIVE = "Admin '%s' is not active";

        String ADMIN_REGISTERED = "Admin is already registered";

        String DUPLICATE_PASSWORD_MESSAGE = "New Password must be different than current password.";

        String INVALID_CONFIRMATION_TOKEN = "Invalid Confirmation Link. Please try again.";

        String PASSWORD_MISMATCH_MESSAGE = "Old password doesn't match.";

        String ADMIN_INFO_NOT_FOUND = "Admin info is not found.";

        String ADMIN_CANNOT_BE_REGISTERED_MESSAGE = "Admin exceeds for the selected hospital";

        String ADMIN_CANNOT_BE_REGISTERED_DEBUG_MESSAGE = "Admin count is equal to hospital's allowed number of admins";
    }


    //B

    //C
    public final static String CANNOT_CREATE_SIGNATURE = "Cannot create signature: ";
    public static final String CODE_DUPLICATION_MESSAGE = "%s already exists with code '%s'";

    //D
    public interface DoctorDutyRosterServiceMessages {
        String DUPLICATION_MESSAGE = "Doctor Duty Roster already exists for selected doctor.";

        String BAD_REQUEST_MESSAGE = "Doctor Duty Roster Override doesn't lie within the duty roster date range.";

        String APPOINTMENT_EXISTS_MESSAGE = "Cannot update doctor duty roster because appointment" +
                " exists within the selected date range.";

        String APPOINTMENT_EXISTS_ON_WEEK_DAY_MESSAGE = "Cannot update doctor duty roster because appointment" +
                " exists within the selected date range '%s'.";
    }

    //E

    //F
    public interface FileServiceMessages {
        String FILES_EMPTY_MESSAGE = "Failed to store empty file";
        String INVALID_FILE_TYPE_MESSAGE = "Could not read file :";
        String INVALID_FILE_SEQUENCE = "Sorry! Filename contains invalid path sequence";
        String FILE_EXCEPTION = "Unable to store file. Please try again later";
    }

    public interface ForgotPasswordMessages {
        String INVALID_RESET_CODE = "Invalid Password Reset Code. Please try again.";
        String RESET_CODE_EXPIRED = "Reset code has expired. Request a new password reset code.";
    }


    //G
    public static final String INVALID_GENDER_CODE_MESSAGE = "Invalid Gender code.";
    public static final String INVALID_GENDER_CODE_DEBUG_MESSAGE = "Gender enum doesn't have the requested code.";

    //H
    public final static String HMAC_BAD_SIGNATURE = "HmacAccessFilter.badSignature";

    //I
    public static final String INVALID_DATE_MESSAGE = "From date cannot be greater than to date";
    public static final String INVALID_DATE_DEBUG_MESSAGE = "Failed to complete operation due to invalid from and to date";
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
    public static final String INVALID_PASSWORD = "Invalid Password.";

    //V

    //W

    //X

    //Y

    //Z
}

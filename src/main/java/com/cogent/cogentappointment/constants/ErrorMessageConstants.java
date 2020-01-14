package com.cogent.cogentappointment.constants;

/**
 * @author smriti on 7/9/19
 */
public class ErrorMessageConstants {

    //A

    public interface AppointmentServiceMessage {
        String APPOINTMENT_EXISTS_MESSAGE = "Cannot update doctor duty roster because appointment" +
                " exists within the selected date range";
    }

    //B

    //C
    public final static String CODE_DUPLICATION_DEBUG_MESSAGE = " entity is not null with given Code : ";
    public final static String CODE_DUPLICATION_MESSAGE = " code already exists with  : ";

    //D

    //E

    //F
    public interface FileServiceMessages {
        String FILES_EMPTY_MESSAGE = "Failed to store empty file";
        String INVALID_FILE_TYPE_MESSAGE = "Could not read file :";
        String INVALID_FILE_SEQUENCE = "Sorry! Filename contains invalid path sequence";
        String FILE_EXCEPTION = "Unable to store file. Please try again later";
    }


    //G
    public static final String INVALID_GENDER_CODE_MESSAGE = "Invalid Gender code.";
    public static final String INVALID_GENDER_CODE_DEBUG_MESSAGE = "Gender enum doesn't have the requested code.";

    //H


    //I

    //J

    //K

    //L

    //M

    //N
    public static final String NAME_DUPLICATION_MESSAGE = "%s already exists with name '%s'";
    public static final String NAME_AND_MOBILE_NUMBER_DUPLICATION_MESSAGE = "%s already exists with name '%s'" +
            "and mobile number '%s': ";


    //O

    //P


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

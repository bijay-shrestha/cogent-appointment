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
    public interface DoctorDutyRosterServiceMessages {
        String DUPLICATION_MESSAGE = "Doctor Duty Roster already exists for selected doctor.";
        String BAD_REQUEST_MESSAGE = "Doctor Duty Roster Override doesn't lie within the duty roster date range.";
    }

    //E

    //F


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
    public static final String NAME_DUPLICATION_MESSAGE = "%s already exists with name '%s': ";


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

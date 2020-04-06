package com.cogent.cogentappointment.esewa.constants;

public class SwaggerConstants {
    public static String BASE_PACKAGE = "com.cogent.cogentappointment.esewa.resource";

    public static String PATH_REGEX = "/esewa/api.*";

    //A


    public interface AppointmentConstant {
        String BASE_API_VALUE = "This is Appointment Resource";
        String CHECK_APPOINTMENT_AVAILABILITY = "Check available time schedules in requested date" +
                " for specific doctor and specialization";
        String SAVE_OPERATION = "Save new appointment";
        String FETCH_PENDING_APPOINTMENT = "Fetch pending appointments";
        String CANCEL_APPOINTMENT_OPERATION = "Cancel appointment and request refund";
        String RESCHEDULE_OPERATION = "Reschedule appointment date and time.";
        String FETCH_APPOINTMENT_HISTORY = " Fetch appointment history (only approved)";
        String CANCEL_REGISTRATION_OPERATION = "Delete Appointment Reservation when user cancels registration process";

    }

    public static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
            // other public endpoints of your API may be appended to this array
    };

    //B

    //C
    public interface CommonConstant {
        String BASE_API_VALUE = "This is Common Resource";
        String FETCH_DOCTOR_SPECIALIZATION_INFO = "Fetch doctor and specialization combined info";
    }


    //D




    //E
    public interface EsewaConstant {
        String BASE_API_VALUE = "This is appointmentDetails Resource";
        String FETCH_AVAILABLE_APPOINTMENT_DATES = "Returns all the avaliable appointment dates and time by doctorId and" +
                " specializationId";
        String FETCH_AVAILABLE_DOCTOR_DATES = "Returns all the avaliable appointment dates by doctorId";
        String FETCH_AVAILABLE_SPECIALIZATION_DATES = "Returns all the avaliable appointment dates by specializationId";
        String FETCH_AVAILABLE_DATES = "Returns all the avaliable appointment dates by doctorId and specializationId";
        String FETCH_DOCTOR_AVAILABLE_STATUS_OPERATION = "Return message if the doctor is available on the date" +
                " (Case V/Case VIII)";
        String FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION_OPERATION = "Fetch all available doctors and " +
                "their specialization on the choosen date (Case II/Case IV)";

    }


    //F

    public interface FollowUpTrackerConstant {
        String BASE_API_VALUE = "This is Follow-Up Tracker Resource";
        String FETCH_FOLLOW_UP_DETAILS = "Fetch follow up details";
    }


    //G

    //H
    public interface HospitalConstant {
        String BASE_API_VALUE = "This is Hospital Resource";
        String FETCH_MIN_DETAILS = "Fetch min hospital details (search by name)";
    }

    //I

    //J
    //K
    //L
    //M

    //N


    //O

    //P
    public interface PatientConstant {
        String BASE_PATIENT_API_VALUE = "This is Patient Resource";
        String SEARCH_PATIENT_WITH_SELF_TYPE_OPERATION = "Search patient info according to given " +
                "request parameters (name, mobile number, dob)";
        String SEARCH_PATIENT_WITH_OTHERS_TYPE_OPERATION = "Fetch list of other child patients for" +
                " selected name, mobile number and dob.";
        String FETCH_DETAILS_OF_OTHERS = " Fetch patient details by hospital patient info id";
        String UPDATE_PATIENT_INFO_OPERATION = "Update patient info (others)";
        String DELETE_PATIENT_INFO_OPERATION = "Delete patient info (others)";
        String FETCH_DETAILS_BY_ID = " Fetch patient details by id";

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

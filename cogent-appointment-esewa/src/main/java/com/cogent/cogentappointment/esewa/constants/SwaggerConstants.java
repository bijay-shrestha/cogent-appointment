package com.cogent.cogentappointment.esewa.constants;

public class SwaggerConstants {
    public static String BASE_PACKAGE = "com.cogent.cogentappointment.esewa.resource";

    public static String PATH_REGEX = "/api.*";

    //A
    public interface AddressConstant {
        String BASE_API_VALUE = "This resource is used to fetch address details ";
        String FETCH_ZONE_LIST="Fetch Zones for dropdown";
        String FETCH_PROVINCE_LIST="Fetch Province for dropdown";
        String FETCH_DISTRICT_LIST_BY_ZONE_ID="Fetch District for dropdown by zone id ";
        String FETCH_DISTRICT_LIST_BY_PROVINCE_ID="Fetch District for dropdown by province id ";
        String FETCH_STREET_LIST_BY_DISTRICT_ID="Fetch Street for dropdown by district id ";
        String FETCH_MUNICIPALITY_LIST_BY_DISTRICT_ID="Fetch Municipality for dropdown by district id ";
    }


    public interface AppointmentConstant {
        String BASE_API_VALUE = "This is Appointment Resource";
        String CHECK_APPOINTMENT_AVAILABILITY = "Check available time schedules in requested date" +
                " for specific doctor and specialization";
        String CHECK_CURRENT_APPOINTMENT_AVAILABILITY = "Check current available time schedules in requested date" +
                " for specific doctor and specialization";
        String SAVE_OPERATION = "Save new appointment";
        String FETCH_PENDING_APPOINTMENT = "Fetch pending appointments";
        String CANCEL_APPOINTMENT_OPERATION = "Cancel appointment and request refund";
        String RESCHEDULE_OPERATION = "Reschedule appointment date and time.";
        String FETCH_APPOINTMENT_HISTORY = " Fetch appointment history (only approved)";
        String CANCEL_REGISTRATION_OPERATION = "Delete Appointment Reservation when user cancels registration process";
        String FETCH_APPOINTMENT_TRANSACTION_STATUS = "Fetch Appointment Transaction Status." +
                " If 'Y', then it is successful appointment, else it is failed appointment";
        String SEARCH_APPOINTMENT = " Search appointment history for myself/others";
    }

    public interface AppointmentHospitalDepartmentConstant {
        String BASE_API_VALUE = "This is Appointment Hospital Department Resource";
        String CHECK_APPOINTMENT_AVAILABILITY = "Check available time schedules in requested date" +
                " for specific hospital department";
        String CHECK_APPOINTMENT_AVAILABILITY_ROOM_WISE = "Check available time schedules in requested date" +
                " for specific hospital department and room";
        String FETCH_AVAILABLE_HOSPITAL_DEPARTMENT_DATES = "Fetch available hospital department appointment dates";
    }

    public interface AppointmentServiceTypeConstant {
        String BASE_API_VALUE = "This is Appointment Service Type Resource.";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal Appointment Service Type details for dropdown";
    }

    public static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/api/v1/test/**",
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
        String BASE_API_VALUE = "This is esewa Resource";
        String FETCH_AVAILABLE_APPOINTMENT_DATES = "Returns all the avaliable appointment dates and time by doctorId and" +
                " specializationId";
        String FETCH_AVAILABLE_DOCTOR_DATES = "Returns all the avaliable appointment dates by doctorId";
        String FETCH_AVAILABLE_SPECIALIZATION_DATES = "Returns all the avaliable appointment dates by specializationId";
        String FETCH_AVAILABLE_DATES = "Returns all the avaliable appointment dates by doctorId and specializationId";
        String FETCH_DOCTOR_AVAILABLE_STATUS_OPERATION = "Return message if the doctor is available on the date" +
                " (Case V/Case VIII)";
        String FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION_OPERATION = "Fetch all available doctors and " +
                "their specialization on the choosen date (Case II/Case IV)";
        String SEARCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION_OPERATION = "Fetch all available doctors and " +
                "their specialization on the selected date range(Case II/Case IV)";

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
        String FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE = "Fetch assigned appointment service type of hospital";
    }

    public interface HospitalDepartmentConstant {
        String BASE_HOSPITAL_DEPARTMENT_API_VALUE = "This is Hospital Department Resource";
        String FETCH_ACTIVE_MIN_HOSPITAL_DEPARTMENT_INFO = "Fetch minimal active hospital department" +
                " details (id and name) for dropdown by hospital id";
        String FETCH_COMBINED_HOSPITAL_DEPARTMENT_INFO = "Fetch combined hospital department" +
                " details (id and name) and billing mode info by hospital id";
    }

    //I

    //J
    //K
    //L
    //M
    public interface MinioFileConstant {
        String BASE_API_VALUE = "This is Minio File Resource";
        String FETCH_FILE_OPERATION = "Fetch file from Minio server";
    }

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
    public interface RefundStatusConstant {
        String BASE_API_VALUE = "This is Refund Status Resource";
        String FETCH_REFUND_DETAILS_TO_APPROVE="Fetch Appointment Refund Details To Approve(change status to A) ";
    }

    //S


    //T
    public interface TestConstant {
        String BASE_API_VALUE = "This is Test Resource";
        String TEST_OPERATION = "This is Test Operation";
    }


    //U

    //V

    //W


    //X
    //Y
    //Z
}

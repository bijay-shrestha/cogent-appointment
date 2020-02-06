package com.cogent.cogentappointment.admin.constants;

public class SwaggerConstants {
    public static String BASE_PACKAGE = "com.cogent.cogentappointment.admin.resource";

    public static String PATH_REGEX = "/api.*";

    //A
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

    public interface AdminConstant {
        String BASE_API_VALUE = "This is Admin Controller";
        String SAVE_OPERATION = "Save new admin";
        String UPDATE_OPERATION = "Update existing admin details";
        String DELETE_OPERATION = "Set admin status as 'D' with specific remarks";
        String SEARCH_OPERATION = "Search admin according to given request parameters";
        String DETAILS_OPERATION = "Fetch admin details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal admin details (id and name) for dropdown";
        String CHANGE_PASSWORD_OPERATION = "Validate the requested password with original and update it accordingly" +
                " (Change password).";
        String RESET_PASSWORD_OPERATION = "Reset password of any admin. This can be done only by those admin who has privilege to do so";
        String UPDATE_AVATAR_OPERATION = "Update admin avatar and if the file size is empty " +
                "then change the isDefaultImage status as 'Y' else update accordingly.";
        String VERIFY_ADMIN = "Verify if the confirmation token sent in email (after successful save) is valid" +
                " and admin has not been registered.";
        String SAVE_PASSWORD_OPERATION = "Save admin password";
        String FETCH_LOGGED_IN_ADMIN_INFO = "Fetch logged in admin information." +
                " Used to show in top-bar and to validate if the admin updates its own profile";
        String FETCH_ADMIN_META_INFO = "Fetch active admin meta info for dropdown";
    }

    public interface AppointmentConstant {
        String BASE_API_VALUE = "This is Appointment Controller";
        String CHECK_APPOINTMENT_AVAILABILITY = "Check available time schedules in requested date" +
                " for specific doctor and specialization";
        String SAVE_OPERATION = "Save new appointment";
        String UPDATE_OPERATION = "Update existing appointment";
        String DELETE_OPERATION = "Set appointment status as 'D' when deleted";
        String SEARCH_OPERATION = "Search appointment according to given request parameters";
        String DETAILS_OPERATION = "Fetch appointment details by its id";
        String DETAILS_APPROVAL_VISIT_OPERATION = "Fetch appointment visit approval details by its id";
        String RESCHEDULE_OPERATION = "Reschedule appointment date and time.";
    }

    public interface AuthenticateConstant {
        String BASE_API_VALUE = "This is Login and SignUp Controller.";
        String LOGIN_OPERATION = "Login User";
    }

    //B

    //C
    public interface CountryConstant {
        String BASE_API_VALUE = "This is Country Controller.";
        String FETCH_ACTIVE_COUNTRY = "Fetch active country for dropdown.";
    }


    //D
    public interface DepartmentConstant {
        String BASE_DEPARTMENT_API_VALUE = "This is Department Controller";
        String SAVE_DEPARTMENT_OPERATION = "Save new department";
        String UPDATE_DEPARTMENT_OPERATION = "Update existing department";
        String DELETE_DEPARTMENT_OPERATION = "Set department status as 'D' when deleted";
        String SEARCH_DEPARTMENT_OPERATION = "Search department according to given request parameters";
        String DEPARTMENT_DETAILS_OPERATION = "Fetch department details";
        String FETCH_DEPARTMENT_FOR_DROP_DOWN_OPERATION = "Fetch minimal department details (id and name) for dropdown";
        String FETCH_ACTIVE_DEPARTMENT_FOR_DROP_DOWN_OPERATION = "Fetch minimal active department details (id and name)" +
                " for dropdown";
        String FETCH_DEPARTMENT_BY_HOSPITAL_OPERATION = "Fetch department by hospital id";
    }

    public interface DoctorConstant {
        String BASE_API_VALUE = "This is Doctor Controller";
        String SAVE_OPERATION = "Save new Doctor like Dr.Sanjeeev Upreti, Dr. Daniel Shrestha, etc";
        String UPDATE_OPERATION = "Update existing Doctor";
        String DELETE_OPERATION = "Set Doctor status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Doctor according to given request parameters";
        String DETAILS_OPERATION = "Fetch Doctor details by its id";
        String DETAILS_FOR_UPDATE_MODAL_OPERATION = "Fetch Doctor details for update modal by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch Doctor details (id and name) for dropdown";
        String FETCH_BY_SPECIALIZATION_ID = "Fetch active doctors by specialization id";
        String FETCH_BY_HOSPITAL_ID = "Fetch active doctors by hospital id";
    }

    public interface DoctorDutyRosterConstant {
        String BASE_API_VALUE = "This is Doctor Duty Roster Controller";
        String SAVE_OPERATION = "Save Doctor Duty Roster";
        String UPDATE_OPERATION = "Update Doctor Duty Roster. Note that week days time can be updated " +
                "only if there are no appointments within the selected date range";
        String DELETE_OPERATION = "Set Doctor Duty Roster status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Doctor Duty Roster according to given request parameters";
        String DETAILS_OPERATION = "Fetch Doctor Duty Roster details by its id";
        String FETCH_DOCTOR_DUTY_ROSTER_STATUS_OPERATION = "Fetch doctor duty roster status (used in Appointment status)";
        String UPDATE_DOCTOR_DUTY_ROSTER_OVERRIDE_OPERATION = "Update Doctor Duty Roster Override schedules";
        String FETCH_EXISTING_ROSTERS = " Fetch existing doctor duty rosters within the selected date range";
    }


    //E


    //F
    public interface ForgotPasswordConstant {
        String BASE_API_VALUE = "This is Forgot Password Controller";
        String FORGOT_PASSWORD_OPERATION = "Validate admin and send reset code in email";
        String VERIFY_RESET_CODE = "Verify if the reset code is valid and has not expired";
        String UPDATE_PASSWORD = "Update password of respective admin (Reset password)";
    }


    //G

    //H
    public interface HospitalConstant {
        String BASE_API_VALUE = "This is Hospital setup Controller";
        String SAVE_OPERATION = "Save new hospital";
        String UPDATE_OPERATION = "Update existing hospital";
        String DELETE_OPERATION = "Set hospital status as 'D' when deleted with remarks";
        String SEARCH_OPERATION = "Search hospital according to given request parameters";
        String DETAILS_OPERATION = "Fetch hospital details by id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal hospital details (id and name) for dropdown";
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
        String BASE_PATIENT_API_VALUE = "This is Patient Controller";
        String SEARCH_PATIENT_WITH_SELF_TYPE_OPERATION = "Search patient info according to given " +
                "request parameters (esewa id, isSelf='Y' and hospital id)";
        String SEARCH_PATIENT_WITH_OTHERS_TYPE_OPERATION = "Fetch list of minimal patient info according to given " +
                "request parameters (esewa id, isSelf='N' and hospital id)";
        String FETCH_DETAILS_BY_ID = " Fetch patient(with type: OTHERS) details by id";
    }

    public interface ProfileConstant {
        String BASE_API_VALUE = "This is Profile Controller";
        String SAVE_OPERATION = "Save new profile";
        String UPDATE_OPERATION = "Update existing profile";
        String DELETE_OPERATION = "Set profile status as 'D' when deleted";
        String SEARCH_OPERATION = "Search profile according to given request parameters";
        String DETAILS_OPERATION = "Fetch profile details by its id. Group profile-menu response by parent-id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal profile details (id and name) for dropdown";
        String FETCH_PROFILE_BY_DEPARTMENT_ID = "Fetch active profiles by department id";
    }


    //Q
    public interface QualificationConstant {
        String BASE_API_VALUE = "This is Qualification Controller";
        String SAVE_OPERATION = "Save new Qualification";
        String UPDATE_OPERATION = "Update existing Qualification";
        String DELETE_OPERATION = "Set Qualification status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Qualification according to given request parameters";
        String DETAILS_OPERATION = "Fetch Qualification details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal Qualification details for dropdown";
    }

    public interface QualificationAliasConstant {
        String BASE_API_VALUE = "This is Qualification Alias Controller";
        String FETCH_ACTIVE_QUALIFICATION_ALIAS = "Fetch active Qualification Alias like M.D.,M.B.B.S, etc";
    }

    //R

    //S

    public interface SpecializationConstant {
        String BASE_API_VALUE = "This is Specialization Controller";
        String SAVE_OPERATION = "Save new Specialization like Physician, Surgeon, etc. Generates random 3-digit code.";
        String UPDATE_OPERATION = "Update existing Specialization";
        String DELETE_OPERATION = "Set Specialization status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Specialization according to given request parameters";
        String DETAILS_OPERATION = "Fetch Specialization details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal Specialization details (id and name) for dropdown";
        String FETCH_BY_DOCTOR_ID = "Fetch active specializations by doctor id";
        String FETCH_BY_HOSPITAL_ID = "Fetch active specializations by hospital id";
    }

    public interface SideBarConstant {
        String BASE_API_VALUE = "This is SideBar Controller";
        String FETCH_ASSIGNED_PROFILE_RESPONSE = "Fetch assigned profile response";
    }


    //T


    //U
    public interface UniversityConstant {
        String BASE_API_VALUE = "This is University Controller.";
        String SAVE_OPERATION = "Save new University";
        String UPDATE_OPERATION = "Update existing University";
        String DELETE_OPERATION = "Set University status as 'D' when deleted";
        String SEARCH_OPERATION = "Search University according to given request parameters";
        String DETAILS_OPERATION = "Fetch University details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal University details for dropdown";
        String FETCH_ACTIVE_UNIVERSITY = "Fetch active University for dropdown.";
    }


    //V

    //W
    public interface WeekDaysConstant {
        String BASE_API_VALUE = "This is Week Days Controller.";
        String FETCH_ACTIVE_WEEK_DAYS = "Fetch active week days.";
    }


    //X
    //Y
    //Z
}

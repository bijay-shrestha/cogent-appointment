package com.cogent.cogentappointment.client.constants;

public class SwaggerConstants {
    public static String BASE_PACKAGE = "com.cogent.cogentappointment.client.resource";

    public static String PATH_REGEX = "/api.*";

    //A
    public interface AdminConstant {
        String BASE_API_VALUE = "This is Admin Resource";
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
        String VERIFY_ADMIN = "Verify if the confirmation token sent in email (after successful saveSelfPatient) is valid" +
                " and admin has not been registered.";
        String SAVE_PASSWORD_OPERATION = "Save admin password";
        String FETCH_LOGGED_IN_ADMIN_INFO = "Fetch logged in admin information." +
                " Used to show in top-bar and to validate if the admin updates its own profile";
        String FETCH_ADMIN_META_INFO = "Fetch active admin meta info for dropdown";
        String VERIFY_EMAIL_ADMIN = "Verify if the confirmation token sent in email (after successful email update by admin) is valid.";
    }

    public interface AdminFeatureConstant {
        String BASE_API_VALUE = "This is Admin Feature Resource";
        String UPDATE_OPERATION = "Update 'isSideBarCollapse' flag of corresponding admin";
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

        /*admin*/
        String FETCH_APPOINTMENT_CANCEL_APPROVALS = "Fetch  Appointment Cancel Approvals List";
        String FETCH_REFUND_APPOINTMENTS_DETAIL = "Fetch Refund Appointment Detail By appointmentId";
        String APPROVE_REFUND_APPOINTMENT = "Approve Refund Request Appointment";
        String REJECT_REFUND_APPOINTMENT = "Reject Refund Request Appointment";
        String FETCH_PENDING_APPOINTMENT_APPROVAL = "Fetch pending appointment approvals";
        String FETCH_PENDING_APPOINTMENT_APPROVAL_DETAIL = "Fetch pending appointment approval detail by appointmentId";
        String APPROVE_APPOINTMENT = "Approve Appointment and set status as 'A'.";
        String REJECT_APPOINTMENT = "Reject Appointment and set status as 'R'.";
        String FETCH_APPOINTMENT_LOG = "Fetch Appointment Log";
        String FETCH_APPOINTMENT_RESCHEDULE_LOG = "Fetch Appointment Reschedule Log";
        String FETCH_TRANSACTION_LOG = "Fetch Transaction Log";
    }

    public interface AppointmentStatusConstant {
        String BASE_API_VALUE = "This is Appointment Status Resource";
        String FETCH_APPOINTMENT_STATUS = "Fetch appointment status.";
    }

    public interface AuthenticateConstant {
        String BASE_API_VALUE = "This is Login and SignUp Resource.";
        String LOGIN_OPERATION = "Login User";
        String LOGOUT_OPERATION = "Logout User";
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
    public interface CountryConstant {
        String BASE_API_VALUE = "This is Country Resource.";
        String FETCH_ACTIVE_COUNTRY = "Fetch active country for dropdown.";
    }

    public interface CommonConstant {
        String BASE_API_VALUE = "This is Common Resource";
        String FETCH_DOCTOR_SPECIALIZATION_INFO = "Fetch doctor and specialization combined info";
    }


    //D

    public interface DashboardConstant {
        String BASE_API_VALUE = "This is Dashboard Resource";
        String FETCH_DYNAMIC_DASHBOARD_FEATURE = " Fetch Dynamic Dashboard feature";
        String GENERATE_REVENUE_OPERATION = "Fetch revenue generated.";
        String OVER_ALL_DASHBOARD_FEATURE = " Fetch Over all Dashboard feature";
        String OVER_ALL_APPOINTMENT_OPERATION = "Fetch over all appointments.";
        String COUNT_REGISTERED_PATIENTS_OPERATION = "Count no. of registered patients.";
        String REVENUE_STATISTICS_OPERATION = "Revenue statistics as per the filter.";
        String REVENUE_TREND_OPERATION = "Revenue Trend as per the filter.";
        String FETCH_APPOINTMENT_QUEUE = "Fetch Appointment Queue of Today for Dashboard.";
        String DOCTOR_REVENUE_OPERATION = "Doctor Revenue list.";
        String REFUND_AMOUNT_OPERATION = "Calculate Total Refunded Amount.";

    }

    public interface DepartmentConstant {
        String BASE_DEPARTMENT_API_VALUE = "This is Department Resource";
        String SAVE_DEPARTMENT_OPERATION = "Save new department";
        String UPDATE_DEPARTMENT_OPERATION = "Update existing department";
        String DELETE_DEPARTMENT_OPERATION = "Set department status as 'D' when deleted";
        String SEARCH_DEPARTMENT_OPERATION = "Search department according to given request parameters";
        String DEPARTMENT_DETAILS_OPERATION = "Fetch department details";
        String FETCH_DEPARTMENT_FOR_DROP_DOWN_OPERATION = "Fetch minimal department details (id and name) for dropdown";
        String FETCH_ACTIVE_DEPARTMENT_FOR_DROP_DOWN_OPERATION = "Fetch minimal active department details (id and name)" +
                " for dropdown";
    }

    public interface DoctorConstant {
        String BASE_API_VALUE = "This is Doctor Resource";
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
        String BASE_API_VALUE = "This is Doctor Duty Roster Resource";
        String SAVE_OPERATION = "Save Doctor Duty Roster";
        String UPDATE_OPERATION = "Update Doctor Duty Roster. Note that week days time can be updated " +
                "only if there are no appointments within the selected date range";
        String DELETE_OPERATION = "Set Doctor Duty Roster status as 'D' when deleted. " +
                " Note that doctor duty roster can be deleted " +
                " only if there are no appointments within the selected date range";
        String SEARCH_OPERATION = "Search Doctor Duty Roster according to given request parameters";
        String DETAILS_OPERATION = "Fetch Doctor Duty Roster details by its id";
        String UPDATE_DOCTOR_DUTY_ROSTER_OVERRIDE_OPERATION = "Update Doctor Duty Roster Override schedules." +
                " (Can saveSelfPatient new override schedules for the same roster or update existing rosters). " +
                " Returns saved/updated override id as response.";
        String DELETE_DOCTOR_DUTY_ROSTER_OVERRIDE_OPERATION = "Set Doctor Duty Roster Override status as 'D' when deleted. " +
                " Note that doctor duty roster override can be deleted " +
                " only if there are no appointments within the selected date range";
        String FETCH_EXISTING_ROSTERS = " Fetch existing doctor duty rosters within the selected date range";
        String REVERT_DOCTOR_DUTY_ROSTER_OVERRIDE_OPERATION = "Revert doctor duty roster.";
    }


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
    public interface ForgotPasswordConstant {
        String BASE_API_VALUE = "This is Forgot Password Resource";
        String FORGOT_PASSWORD_OPERATION = "Validate admin and send reset code in email";
        String VERIFY_RESET_CODE = "Verify if the reset code is valid and has not expired";
        String UPDATE_PASSWORD = "Update password of respective admin (Reset password)";
    }


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
        String FETCH_ACTIVE_MIN_PATIENT_META_INFO = "Fetch minimal active patient meta info details " +
                "(id and metaInfo) for dropdown";
        String FETCH_MIN_PATIENT_META_INFO = "Fetch minimal patient meta info details " +
                "(id and metaInfo) for dropdown";
        String SEARCH_PATIENT_WITH_SELF_TYPE_OPERATION = "Search patient info according to given " +
                "request parameters (name, mobile number, dob)";
        String SEARCH_PATIENT_WITH_OTHERS_TYPE_OPERATION = "Fetch list of other child patients for" +
                " selected name, mobile number and dob.";
        String FETCH_DETAILS_OF_OTHERS = " Fetch patient details by hospital patient info id";
        String UPDATE_PATIENT_INFO_OPERATION = "Update patient info (others)";
        String DELETE_PATIENT_INFO_OPERATION = "Delete patient info (others)";
        String SEARCH_OPERATION = "Search Patient Info";
        String FETCH_DETAILS_BY_ID = " Fetch patient details by id";
        String FETCH_PATIENT_MIN_DETAIL_BY_APPOINTMENT_ID = "Fetch min patient detail by appointment id.";

    }

    public interface ProfileConstant {
        String BASE_API_VALUE = "This is Profile Resource";
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
        String BASE_API_VALUE = "This is Qualification Resource";
        String SAVE_OPERATION = "Save new Qualification";
        String UPDATE_OPERATION = "Update existing Qualification";
        String DELETE_OPERATION = "Set Qualification status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Qualification according to given request parameters";
        String DETAILS_OPERATION = "Fetch Qualification details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal Qualification details for dropdown";
    }

    public interface QualificationAliasConstant {
        String BASE_API_VALUE = "This is Qualification Alias Resource";
        String SAVE_OPERATION = "Save new Qualification Alias Resource";
        String UPDATE_OPERATION = "Update existing Qualification Alias Resource";
        String DELETE_OPERATION = "Set Qualification Alias Resource status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Qualification Alias Resource according to given request parameters";
        String FETCH_ACTIVE_QUALIFICATION_ALIAS = "Fetch active Qualification Alias like M.D.,M.B.B.S, etc";
    }

    //R

    //S

    public interface SalutationConstant {
        String BASE_API_VALUE = "This is Salutation Resource";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal Salutation details (id and code) for dropdown";
    }

    public interface SpecializationConstant {
        String BASE_API_VALUE = "This is Specialization Resource";
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
        String BASE_API_VALUE = "This is SideBar Resource";
        String FETCH_ASSIGNED_PROFILE_RESPONSE = "Fetch assigned profile response";
    }


    //T

    public interface TestConstant {
        String BASE_API_VALUE = "This is Test Resource";
        String TEST_OPERATION = "This is Test Operation";
    }


    //U
    public interface UniversityConstant {
        String BASE_API_VALUE = "This is University Resource.";
        String SAVE_OPERATION = "Save new University";
        String UPDATE_OPERATION = "Update existing University";
        String DELETE_OPERATION = "Set University status as 'D' when deleted";
        String SEARCH_OPERATION = "Search University according to given request parameters";
        String DETAILS_OPERATION = "Fetch University details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal University details for dropdown";
    }


    //V

    //W
    public interface WeekDaysConstant {
        String BASE_API_VALUE = "This is Week Days Resource.";
        String FETCH_ACTIVE_WEEK_DAYS = "Fetch active week days.";
        String FETCH_PREPARE_WEEK_DAYS = "Prepare active week days (for doctor duty roster).";
    }


    //X
    //Y
    //Z
}

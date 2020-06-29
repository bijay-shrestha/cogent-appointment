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

    public interface AddressConstant {
        String BASE_API_VALUE = "This resource is used to fetch address details ";
        String FETCH_ZONE_LIST = "Fetch Zones for dropdown";
        String FETCH_PROVINCE_LIST = "Fetch Province for dropdown";
        String FETCH_DISTRICT_LIST_BY_ZONE_ID = "Fetch District for dropdown by zone id ";
        String FETCH_DISTRICT_LIST_BY_PROVINCE_ID = "Fetch District for dropdown by province id ";
        String FETCH_STREET_LIST_BY_DISTRICT_ID = "Fetch Street for dropdown by district id ";
        String FETCH_MUNICIPALITY_LIST_BY_DISTRICT_ID = "Fetch Municipality for dropdown by district id ";
    }

    public interface AdminConstant {
        String BASE_API_VALUE = "This is Admin Resource";
        String SAVE_OPERATION = "Save new admin";
        String UPDATE_OPERATION = "Update existing admin details";
        String DELETE_OPERATION = "Set admin status as 'D' with specific remarks";
        String SEARCH_OPERATION = "Search admin according to given request parameters";
        String DETAILS_OPERATION = "Fetch admin details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal admin details (id and name) for dropdown";
        String RESET_PASSWORD_OPERATION = "Reset password of any admin. This can be done only by those admin who has privilege to do so";
        String UPDATE_AVATAR_OPERATION = "Update admin avatar and if the file size is empty " +
                "then change the isDefaultImage status as 'Y' else update accordingly.";
        String FETCH_ADMIN_META_INFO = "Fetch active admin meta info for dropdown";
        String VERIFY_EMAIL_ADMIN = "Verify if the confirmation token sent in email (after successful email update by admin) is valid.";

        String FETCH_ADMIN_META_INFO_BY_COMPANY_ID = "Fetch active admin meta info by company for dropdown";
        String FETCH_ADMIN_META_INFO_BY_CLIENT_ID = "Fetch active admin meta info by client for dropdown";
    }

    public interface AdminFavouriteConstant {
        String BASE_API_VALUE = "This is Admin Favourite Resource";
        String SAVE_ADMIN_FAVOURITE_OPERATION = "Save new Admin Favourite";
        String UPDATE_OPERATION = "Update Admin Favourite flag of corresponding admin";
    }

    public interface AdminFeatureConstant {
        String BASE_API_VALUE = "This is Admin Feature Resource";
        String UPDATE_OPERATION = "Update 'isSideBarCollapse' flag of corresponding admin";

    }

    public interface AppointmentConstant {
        String BASE_API_VALUE = "This is Appointment Resource";
        String FETCH_APPOINTMENTS_CANCEL_APPROVALS = "Fetch  Appointment cancel approval List";
        String FETCH_REFUND_APPOINTMENTS_DETAIL = "Fetch Refund Appointment Detail By appointmentId";
        String APPROVE_REFUND_APPOINTMENT = "Approve Refund Request Appointment";
        String REJECT_REFUND_APPOINTMENT = "Reject Refund Request Appointment";
        String FETCH_PENDING_APPOINTMENT_APPROVAL = "Fetch pending appointment approvals";
        String FETCH_PENDING_APPOINTMENT_APPROVAL_DETAIL = "Fetch pending appointment approval detail by appointmentId";
        String APPROVE_APPOINTMENT = "Approve Appointment and set status as 'A'.";
        String REJECT_APPOINTMENT = "Reject Appointment and set status as 'R'.";
        String FETCH_APPOINTMENT_LOG = "Fetch Appointment Log";
        String FETCH_APPOINTMENT_QUEUE = "Fetch Appointment Queue of Today for Dashboard";
        String FETCH_APPOINTMENT_RESCHEDULE_LOG = "Fetch Appointment Reschedule Log";
        String DETAILS_APPROVAL_VISIT_OPERATION = "Fetch appointment visit approval details by its id";
        String FETCH_TRANSACTION_LOG = "Fetch Transaction Log";
    }

    public interface HospitalDepartmentAppointmentLogConstant {
        String BASE_API_VALUE = "This resource consists of APIs for  logs(transaction, appointment," +
                " reschedule, transfer) based on hospital department ";
        String FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG = "Fetch Appointment Log based on hospital department";
        String FETCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG = "Fetch Transaction Log based on hospital department";
        String FETCH_HOSPITAL_DEPARTMENT_RESCHEDULE_LOG = "Fetch Reschedule Log based on hospital department";
        String FETCH_PENDING_HOSPITAL_DEPARTMENT_APPOINTMENT = "Fetch pending hospital department appointments";

    }

    public interface AppointmentStatusConstant {
        String BASE_API_VALUE = "This is Appointment Status Resource";
        String FETCH_APPOINTMENT_STATUS = "Fetch appointment status.";
        String FETCH_DEPARTMENT_APPOINTMENT_STATUS = "Fetch department appointment status.";
        String FETCH_DEPARTMENT_APPOINTMENT_STATUS_ROOM_WISE = "Fetch department appointment status by specific roomId.";
        String FETCH_DEPARTMENT_APPOINTMENT_STATUS_COUNT = "Fetch department appointment status count.";
    }

    public interface AppointmentTransferConstant {
        String BASE_API_VALUE = "Resource used for Appointment Transfer Scenario.";
        String FETCH_AVAILABLE_DATES = "Fetch available dates by doctor id and specialization id";
        String FETCH_AVAILABLE_TIME = "Fetch available time by available date and doctor id";
        String FETCH_DOCTOR_CHARGE = "Fetch selected doctor charge by  doctor id";
        String APPOINTMENT_TRANSFER = "Transfer current appointment details(i.e. doctorId,specializationId," +
                "appointmentDate,appointmentTime,Charge)";
        String FETCH_TRANSFERRED_APPOINTMENT_LIST = "Fetch list of transferred appointments";
        String FETCH_TRANSFERRED_APPOINTMENT_DETAIL = "Fetch transferred appointment detail by id";
    }

    public interface AuthenticateConstant {
        String BASE_API_VALUE = "This is Login and SignUp Resource.";
        String LOGIN_OPERATION = "Login User";
        String LOGOUT_OPERATION = "Logout User";
    }

    public interface AppointmentModeConstant {
        String BASE_API_VALUE = "This is Appointment Mode Resource.";
        String SAVE_OPERATION = "Save new Appointment Mode";
        String UPDATE_OPERATION = "Update existing Appointment Mode";
        String DELETE_OPERATION = "Set Appointment Mode status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Appointment Mode according to given request parameters";
        String DETAILS_OPERATION = "Fetch Appointment Mode details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal Appointment Mode details for dropdown";
    }

    public interface AppointmentServiceTypeConstant {
        String BASE_API_VALUE = "This is Appointment Service Type Resource.";
        String SAVE_OPERATION = "Save new Appointment Service Type";
        String UPDATE_OPERATION = "Update existing Appointment Service Type";
        String DELETE_OPERATION = "Set Appointment Service Type status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Appointment Service Type according to given request parameters";
        String DETAILS_OPERATION = "Fetch Appointment Service Type details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal Appointment Service Type details for dropdown";
        String FETCH_NAME_AND_CODE_FOR_DROPDOWN = "Fetch Appointment Service Type name and code for dropdown";
    }

    //B
    public interface BillingModeConstant {
        String BASE_API_VALUE = "This is Billing Mode Resource.";
        String SAVE_OPERATION = "Save new Billing Mode";
        String UPDATE_OPERATION = "Update existing Billing Mode";
        String DELETE_OPERATION = "Set Billing Mode status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Billing Mode according to given request parameters";
        String DETAILS_OPERATION = "Fetch Billing Mode details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal Billing Mode details for dropdown";
    }

    //C


    public interface CompanyAdminConstant {
        String BASE_API_VALUE = "This is Company Admin Resource";
        String SAVE_OPERATION = "Save new company admin";
        String UPDATE_OPERATION = "Update existing company admin details";
        String DELETE_OPERATION = "Set company admin status as 'D' with specific remarks";
        String SEARCH_OPERATION = "Search company admin according to given request parameters";
        String DETAILS_OPERATION = "Fetch company admin details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal company admin details (id and name) for dropdown";
        String CHANGE_PASSWORD_OPERATION = "Validate the requested password with original and update it accordingly" +
                " (Change password).";
        String RESET_PASSWORD_OPERATION = "Reset password of any company admin. " +
                "This can be done only by those admin who has privilege to do so";
        String UPDATE_AVATAR_OPERATION = "Update company admin avatar and if the file size is empty " +
                "then change the isDefaultImage status as 'Y' else update accordingly.";
        String VERIFY_ADMIN = "Verify if the confirmation token sent in email (after successful save) is valid" +
                " and company admin has not been registered.";
        String SAVE_PASSWORD_OPERATION = "Save company admin password";
        String FETCH_LOGGED_IN_ADMIN_INFO = "Fetch logged in company admin information." +
                " Used to show in top-bar and to validate if the company admin updates its own profile";
        String FETCH_ADMIN_META_INFO = "Fetch active company admin meta info for dropdown";
    }


    public interface CountryConstant {
        String BASE_API_VALUE = "This is Country Resource.";
        String FETCH_ACTIVE_COUNTRY = "Fetch active country for dropdown.";
    }

    public interface CompanyConstant {
        String BASE_API_VALUE = "This is Company Resource";
        String SAVE_OPERATION = "Save new company";
        String UPDATE_OPERATION = "Update existing company";
        String DELETE_OPERATION = "Set company status as 'D' when deleted with remarks";
        String SEARCH_OPERATION = "Search company according to given request parameters";
        String DETAILS_OPERATION = "Fetch company details by id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal company details (id and name) for dropdown";
    }

    public interface CompanyProfileConstant {
        String BASE_API_VALUE = "This is Company Profile Resource";
        String SAVE_OPERATION = "Save new Company Profile";
        String UPDATE_OPERATION = "Update existing Company Profile";
        String DELETE_OPERATION = "Set company profile status as 'D' when deleted with remarks";
        String SEARCH_OPERATION = "Search company profile according to given request parameters";
        String DETAILS_OPERATION = "Fetch company profile details by id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal company profile details (id and name) for dropdown";
        String FETCH_MIN_DETAILS_BY_COMPANY_ID = "Fetch minimal company profile details (id and name) for dropdown by company id";
    }

    //D
    public interface DashboardConstant {
        String BASE_API_VALUE = "This is Dashboard Resource.";
        String FETCH_DYNAMIC_DASHBOARD_FEATURE = " Fetch Dynamic Dashboard feature";
        String GENERATE_REVENUE_OPERATION = "Fetch revenue generated.";
        String OVER_ALL_DASHBOARD_FEATURE = " Fetch Over all Dashboard feature";
        String OVER_ALL_APPOINTMENT_OPERATION = "Fetch over all appointments.";
        String COUNT_REGISTERED_PATIENTS_OPERATION = "Count no. of registered patients.";
        String REVENUE_STATISTICS_OPERATION = "Revenue statistics as per the filter.";
        String REVENUE_TREND_OPERATION = "Revenue Trend as per the filter.";
        String DOCTOR_REVENUE_OPERATION = "Doctor Revenue list.";
        String HOSPITAL_DEPARTMENT_REVENUE_OPERATION = "Hospital Department Revenue list.";
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
        String FETCH_DEPARTMENT_BY_HOSPITAL_OPERATION = "Fetch department by hospital id";
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
        String FETCH_ACTIVE_DOCTORS_BY_HOSPITAL_ID = "Fetch active doctors by hospital id";
        String FETCH_DOCTORS_BY_HOSPITAL_ID = "Fetch doctors by hospital id";
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
                " (Can save new override schedules for the same roster or update existing rosters). " +
                " Returns saved/updated override id as response.";
        String DELETE_DOCTOR_DUTY_ROSTER_OVERRIDE_OPERATION = "Set Doctor Duty Roster Override status as 'D' when deleted. " +
                " Note that doctor duty roster override can be deleted " +
                " only if there are no appointments within the selected date range";
        String FETCH_EXISTING_ROSTERS = " Fetch existing doctor duty rosters within the selected date range";
        String REVERT_DOCTOR_DUTY_ROSTER_OVERRIDE_OPERATION = "Revert doctor duty roster." +
                " In case, user 'Cancel' roster while updating doctor duty roster." +
                " If 'isOriginal' flag is true, then update all override details else simply update status and remarks.";
        String FETCH_DOCTOR_DUTY_ROSTER_STATUS_OPERATION = "Fetch doctor duty roster status (used in Appointment status)";
    }


    //E


    //F
    public interface ForgotPasswordConstant {
        String BASE_API_VALUE = "This is Forgot Password Resource";
        String FORGOT_PASSWORD_OPERATION = "Validate admin and send reset code in email";
        String VERIFY_RESET_CODE = "Verify if the reset code is valid and has not expired";
        String UPDATE_PASSWORD = "Update password of respective admin (Reset password)";
    }


    //G

    //H

    public interface Hmaconstant {
        String BASE_API_VALUE = "This resource is used to generate hmac hash for frontend use";

    }

    public interface HospitalConstant {
        String BASE_API_VALUE = "This is Hospital setup Resource";
        String SAVE_OPERATION = "Save new hospital";
        String UPDATE_OPERATION = "Update existing hospital";
        String DELETE_OPERATION = "Set hospital status as 'D' when deleted with remarks";
        String SEARCH_OPERATION = "Search hospital according to given request parameters";
        String DETAILS_OPERATION = "Fetch hospital details by id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal hospital details (id and name) for dropdown";
        String FETCH_ALIAS_BY_ID = "Fetch alias by hospital id";
        String FETCH_ASSIGNED_APPOINTMENT_SERVICE_TYPE = "Fetch assigned hospital appointment service type";
    }

    public interface HospitalDeptDutyRosterConstant {
        String BASE_API_VALUE = "This is Hospital Department Duty Roster Resource";
        String SAVE_OPERATION = "Save Hospital Department Duty Roster";
        String UPDATE_OPERATION = "Update Hospital Department Duty Roster. Note that week days time can be updated " +
                "only if there are no appointments within the selected date range";
        String DELETE_OPERATION = "Set Hospital Department Duty Roster status as 'D' when deleted. " +
                " Note that Hospital Department duty roster can be deleted " +
                " only if there are no appointments within the selected date range";
        String SEARCH_OPERATION = "Search Hospital Department Duty Roster according to given request parameters";
        String DETAILS_OPERATION = "Fetch Hospital Department Duty Roster details by its id";
        String UPDATE_OVERRIDE_OPERATION = "Update Hospital Department Duty Roster Override schedules." +
                " (Can save new override schedules for the same roster or update existing rosters). " +
                " Returns saved/updated override id as response.";
        String DELETE_OVERRIDE_OPERATION = "Set Hospital Department Duty Roster" +
                " Override status as 'D' when deleted. Note that Hospital Department duty roster override can be deleted" +
                " only if there are no appointments within the selected date range";
        String FETCH_EXISTING_ROSTERS = " Fetch existing Hospital Department duty rosters within the selected date range";
        String REVERT_OVERRIDE_OPERATION = "Revert Hospital Department duty roster.";
    }


    public interface HospitalDepartmentConstant {
        String BASE_HOSPITAL_DEPARTMENT_API_VALUE = "This is Hospital Department Resource";
        String SAVE_HOSPITAL_DEPARTMENT_OPERATION = "Save new hospital department";
        String UPDATE_HOSPITAL_DEPARTMENT_OPERATION = "Update existing hospital department";
        String DELETE_HOSPITAL_DEPARTMENT_OPERATION = "Set hospital department status as 'D' when deleted";
        String SEARCH_HOSPITAL_DEPARTMENT_OPERATION = "Search hospital department according to given request parameters";
        String HOSPITAL_DEPARTMENT_DETAILS_OPERATION = "Fetch hospital department details";
        String FETCH_HOSPITAL_DEPARTMENT_FOR_DROP_DOWN_OPERATION = "Fetch minimal hospital department details" +
                " (id and name) for dropdown by hospital id";
        String FETCH_ACTIVE_HOSPITAL_DEPARTMENT_FOR_DROP_DOWN_OPERATION = "Fetch minimal active hospital department" +
                " details (id and name) for dropdown by hospital id";
        String FETCH_AVAILABLE_ROOM_FOR_DROP_DOWN_OPERATION = "Fetch available room " +
                " details (id and name) for dropdown by hospitalId";
        String FETCH_APPOINTMENT_CHARGE = "Fetch appointment charge by hospital department and billing mode id";
        String FETCH_ASSIGNED_DOCTOR = "Fetch assigned doctor in selected hospital department";
    }


    //I

    public interface IntegrationConstant {
        String BASE_API_VALUE = "This is Client Integration Resource.";
        String SAVE_CLIENT_OPERATION = "Save new Client API Integration";
        String UPDATE_CLIENT_OPERATION = "Update Client API Integration";
        String SEARCH_CLIENT_API_INTEGRATION_OPERATION = "Search Client API Integration";
        String FETCH_CLIENT_API_INTEGRATION_DETAIL = "Fetch Client Api Integration detail by client integration id";
        String DELETE_CLIENT_INTEGRATION_FEATURE_OPERATION = "Delete client integration feature by client integration id";
        String DETAILS_FOR_UPDATE_CLIENT_INTEGRATION_MODAL_OPERATION = "Fetch Client Integration details for update modal by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal details for dropdown";
    }

    public interface IntegrationAdminModeConstant {
        String BASE_API_VALUE = "This is Admin Mode API Integration Resource.";
        String SAVE_ADMIN_MODE_INTEGRATION_OPERATION = "Save new Admin Mode API Integration";
        String UPDATE_ADMIN_MODE_INTEGRATION_OPERATION = "Update Admin Mode API Integration";
        String SEARCH_ADMIN_MODE_INTEGRATION_OPERATION = "Search Admin Mode API Integration";
        String FETCH_ADMIN_MODE_INTEGRATION_DETAIL = "Fetch Admin Mode API Integration detail by admin mode integration id";
        String DELETE_ADMIN_MODE_INTEGRATION_OPERATION = "Delete Admin Mode API Integration feature by admin mode api integration id";
        String DETAILS_FOR_UPDATE_ADMIN_MODE_INTEGRATION_MODAL_OPERATION = "Fetch Admin Mode API Integration details for update modal by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal details for dropdown";
    }

    public interface IntegrationRequestBodyConstant {
        String BASE_API_VALUE = "This is Integration Request Body Resource.";
        String DELETE_API_REQUEST_HEADER_ATTRIBUTE_OPERATION = "Delete API Request Body Attributes by request body attribute id";
        String SAVE_INTEGRATION_REQUEST_BODY_ATTRIBUTE_OPERATION = "Save new Integration Request Body Attributes";
        String UPDATE_INTEGRATION_REQUEST_BODY_ATTRIBUTE_OPERATION = "Update Request Body Attributes";
        String FETCH_API_REQUEST_HEADER_ATTRIBUTE_OPERATION = "Fetch Request Body Attributes by feature id";
        String SEARCH_API_REQUEST_BODY_ATTRIBUTE_OPERATION = "Search API Request Body Attributes";
    }

    //J
    //K
    //L
    //M

    //N


    //O

    //P
    public interface PatientConstant {
        String BASE_PATIENT_API_VALUE = "This is Patient Resource";
        String FETCH_ACTIVE_PATIENT_META_INFO_DETAILS_FOR_DROPDOWN = "Fetch minimal active patient meta info details " +
                "(id and metaInfo) for dropdown";
        String FETCH_PATIENT_META_INFO_DETAILS_FOR_DROPDOWN = "Fetch minimal patient meta info details " +
                "(id and metaInfo) for dropdown";
        String UPDATE_PATIENT_INFO_OPERATION = "Update patient info";
        String SEARCH_OPERATION = "Search Patient Info";
        String FETCH_DETAILS_BY_ID = " Fetch patient(with type 'OTHERS') details by id";
        String FETCH_PATIENT_MIN_DETAIL_BY_APPOINTMENT_ID = "Fetch min patient detail by appointment id.";
        String FETCH_PATIENT_ESEWA_ID = "Fetch patient esewa id";
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
    public interface RefundStatusConstant {
        String BASE_API_VALUE = "This is Refund Status Resource";
        String FETCH_APPOINTMENT_REFUND_DETAIL_LIST = "Fetch Appointment Refund Details List(status='PA','R','A')";
        String FETCH_REFUND_DETAILS_TO_APPROVE = "Fetch Appointment Refund Details To Approve(change status to A) ";
        String FETCH_REFUND_STATUS_APPOINTMENTS_DETAIL = "Fetch Refund Appointment Detail By appointmentId";
    }


    public interface RoomConstant {
        String BASE_API_VALUE = "This is Room Resource.";
        String SAVE_OPERATION = "Save new room number.";
        String UPDATE_OPERATION = "Update existing room number";
        String DELETE_OPERATION = "Set room number status as 'D' when deleted";
        String SEARCH_OPERATION = "Search room number according to given request parameters";
        String FETCH_ACTIVE_ROOM_FOR_DROP_DOWN = "Fetch active room numbers for drop down";
        String FETCH_ROOM_FOR_DROP_DOWN = "Fetch room numbers for drop down";
        String FETCH_ACTIVE_ROOM_FOR_DROP_DOWN_BY_HOSPITAL_DEPARTMENT_ID = "Fetch active room numbers for drop down " +
                " by hospital department Id";
        String FETCH_ROOM_FOR_DROP_DOWN_BY_HOSPITAL_DEPARTMENT_ID = "Fetch room numbers for drop down " +
                "by hospital department Id";
    }

    //S

    public interface SalutationConstant {
        String BASE_API_VALUE = "This is Salutation Resource";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal Salutation details (id and code) for dropdown";
        String DELETE_OPERATION = "Set Salutation status as 'D' when deleted";
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
        String FETCH_PREPARE_WEEK_DAYS = "Fetch PREPARE active week days.";
    }


    //X
    //Y
    //Z
}

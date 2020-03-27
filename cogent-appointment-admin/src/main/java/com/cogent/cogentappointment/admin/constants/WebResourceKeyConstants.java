package com.cogent.cogentappointment.admin.constants;

/**
 * @author smriti on 7/5/19
 */
public class WebResourceKeyConstants {

    //A
    public static final String API_V1 = "/api/v1";
    public static final String ACTIVE = "/active";

    public interface AdminConstants {
        String BASE_ADMIN = "/admin";
        String AVATAR = "/avatar";
        String ADMIN_META_INFO = "/metaInfo";
        String CHANGE_PASSWORD = "/changePassword";
        String RESET_PASSWORD = "/resetPassword";
        String INFO = "/info";
        String VERIFY = "/verify";
    }

    public interface AppointmentConstants {
        String BASE_APPOINTMENT = "/appointment";
        String APPOINTMENT_ID_PATH_VARIABLE_BASE = "/{appointmentId}";
        String REFUND = "/refund";
        String APPROVE = "/approve";
        String REJECT = "/reject";
        String STATUS = "/status";
        String LOG = "/log";
        String PENDING_APPROVAL = "/pending-approval";
        String RESCHEDULE = "/reschedule";
        String RESCHEDULE_LOG = "/reschedule-log";
        String DETAILS = "/detail";


    }

    //B
    public static final String BASE_PASSWORD = "/password";

    //C
    public interface CountryConstants {
        String BASE_COUNTRY = "/country";
    }

    public interface CompanyConstants {
        String BASE_COMPANY = "/company";
    }

    public interface CompanyAdminConstants {
        String BASE_COMPANY_ADMIN = "/CompanyAdmin";
        String AVATAR = "/avatar";
        String COMPANY_ADMIN_META_INFO = "/metaInfo";
        String CHANGE_PASSWORD = "/changePassword";
        String RESET_PASSWORD = "/resetPassword";
        String INFO = "/info";
        String VERIFY = "/verify";
    }

    public interface CompanyProfileConstants {
        String BASE_COMPANY_PROFILE = "/company-profile";
    }

    //D
    public interface DashboardConstants {
        String BASE_DASHBOARD = "/dashboard";
        String GENERATE_REVENUE = "/revenueGenerated";
        String OVER_ALL_APPOINTMENT = "/overAllAppointments";
        String REVENUE_STATISTICS = "/revenueStatistics";
        String REGISTERED = "/registeredPatients";
        String COUNT = "/count";
        String HOSPITAL_ID_PATH_VARIABLE_BASE = "/{hospitalId}";
        String APPOINTMENT_QUEUE = "/today-appointment";
        String APPOINTMENT_QUEUE_BY_TIME = "/today-appointment-timely";
        String REVENUE_TREND = "/revenueTrend";
        String TOTAL_REFUNDED_AMOUNT = "/totalRefundedAmount";
    }

    public static final String DETAIL = "/detail";

    public interface DepartmentConstants {
        String BASE_DEPARTMENT = "/department";
        String DEPARTMENT_ID_PATH_VARIABLE_BASE = "/{departmentId}";
    }

    public interface DoctorConstants {
        String BASE_DOCTOR = "/doctor";
        String UPDATE_DETAILS = "/updateDetails";
        String DOCTOR_WISE = "/doctor-wise";
        String DOCTOR_ID_PATH_VARIABLE_BASE = "/{doctorId}";
    }

    public interface DoctorDutyRosterConstants {
        String BASE_DOCTOR_DUTY_ROSTER = "/doctorDutyRoster";
        String DOCTOR_DUTY_ROSTER_OVERRIDE = "/doctorDutyRosterOverride";
        String EXISTING = "/existing";
        String REVERT_DOCTOR_DUTY_ROSTER_OVERRIDE = "/revert";
    }

    //F

    public interface ForgotPasswordConstants {
        String VERIFY = "/verify";
        String FORGOT = "/forgot";
    }


    //G

    //H
    public interface HospitalConstants {
        String BASE_HOSPITAL = "/hospital";
        String HOSPITAL_WISE = "/hospital-wise";
        String HOSPITAL_ID_PATH_VARIABLE_BASE = "/{hospitalId}";
    }

    //I
    public static final String ID_PATH_VARIABLE_BASE = "/{id}";


    //J

    //K

    //L
    public static final String LOGIN = "/login";

    //M
    public static final String META_INFO = "/metaInfo";
    public static final String MIN = "/min";

    public interface MinioFileConstants {
        String BASE_FILE = "/file";
        String FETCH_FILE = "/{subDirectory}/{object}";

    }

    //N


    //O

    //P
    public interface PatientConstant {
        String BASE_PATIENT = "/patient";
    }

    public interface ProfileSetupConstants {
        String BASE_PROFILE = "/profile";
    }

    //Q
    public interface QualificationConstants {
        String BASE_QUALIFICATION = "/qualification";
    }

    public interface QualificationAliasConstants {
        String BASE_QUALIFICATION_ALIAS = "/qualificationAlias";
    }

    //R


    //S

    public static final String SEARCH = "/search";

    public interface SidebarConstants {
        String BASE_SIDE_BAR = "/sidebar";
    }


    public interface SpecializationConstants {
        String BASE_SPECIALIZATION = "/specialization";
        String SPECIALIZATION_WISE = "/specialization-wise";
        String SPECIALIZATION_ID_PATH_VARIABLE_BASE = "/{specializationId}";
    }


    //T

    //U
    public static final String USERNAME_VARIABLE_BASE = "/{username}";

    public interface UniversityConstants {
        String BASE_UNIVERSITY = "/university";
    }


    //V

    //W
    public interface WeekDaysConstants {
        String BASE_WEEK_DAYS = "/weekDays";
        String PREPARE_WEEK_DAYS_DATA = "/prepare-weekdays-data";
    }


    //X

    //Y

    //Z
}

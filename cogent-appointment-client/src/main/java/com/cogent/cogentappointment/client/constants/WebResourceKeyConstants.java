package com.cogent.cogentappointment.client.constants;

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
        String VERIFY = "/verify";
    }

    public interface AppointmentConstants {
        String BASE_APPOINTMENT = "/appointment";
        String APPOINTMENT_ID_PATH_VARIABLE_BASE = "/{appointmentId}";
        String APPOINTMENT_RESERVATION_ID_PATH_VARIABLE_BASE = "/{appointmentReservationId}";
        String CHECK_AVAILABILITY = "/checkAvailability";
        String PENDING_APPOINTMENT = "/pending-appointment";
        String CANCEL = "/cancel";
        String RESCHEDULE = "/reschedule";
        String FOLLOW_UP = "/follow-up";
        String HISTORY = "/history";
        String PENDING_APPROVAL = "/pending-approval";
        String REFUND = "/refund";
        String APPROVE = "/approve";
        String REJECT = "/reject";
        String STATUS = "/status";
        String LOG = "/log";
        String RESCHEDULE_LOG = "/reschedule-log";
    }

    //B
    public static final String BASE_PASSWORD = "/password";

    //C
    public interface CountryConstants {
        String BASE_COUNTRY = "/country";
    }

    public interface CommonConstants {
        String BASE_COMMON = "/common";
        String DOCTOR_SPECIALIZATION = "/doctor-specialization";
    }

    //D
    public interface DashboardConstants {
        String BASE_DASHBOARD = "/dashboard";
        String GENERATE_REVENUE = "/revenueGenerated";
        String OVER_ALL_APPOINTMENT = "/overAllAppointments";
        String REVENUE_STATISTICS = "/revenueStatistics";
        String REGISTERED = "/registeredPatients";
        String COUNT = "/count";
        String APPOINTMENT_QUEUE = "/today-appointment";
        String APPOINTMENT_QUEUE_BY_TIME = "/today-appointment-timely";
        String REVENUE_TREND = "/revenueTrend";
        String TOTAL_REFUNDED_AMOUNT = "/totalRefundedAmount";
        String DOCTOR_REVENUE = "/doctorRevenue";
    }

    public static final String DETAIL = "/detail";
    public static final String DELETE = "/delete";

    public interface DepartmentConstants {
        String BASE_DEPARTMENT = "/department";
        String DEPARTMENT_ID_PATH_VARIABLE_BASE = "/{departmentId}";
    }

    public interface DoctorConstants {
        String BASE_DOCTOR = "/doctor";
        String DOCTOR_WISE = "/doctor-wise";
        String UPDATE_DETAILS = "/updateDetails";
    }

    public static final String DOCTOR_ID_PATH_VARIABLE_BASE = "/{doctorId}";

    public interface DoctorDutyRosterConstants {
        String BASE_DOCTOR_DUTY_ROSTER = "/doctorDutyRoster";
        String DOCTOR_DUTY_ROSTER_OVERRIDE = "/doctorDutyRosterOverride";
        String EXISTING = "/existing";
        String REVERT_DOCTOR_DUTY_ROSTER_OVERRIDE = "/revert";
    }


    //E
    public interface EsewaConstants {
        String BASE_ESEWA = "/esewa";
        String FETCH_DOCTOR_AVAILABLE_STATUS = "/doctor-available-status";
        String AVAILABLE_APPOINTMENT_DATES_AND_TIME = "/availableAppointmentDatesAndTime";
        String DOCTOR_AVAILABLE_DATES = "/doctorAvailableDates";
        String SPECIALIZATION_AVAILABLE_DATES = "/specializationAvailableDates";
        String DOCTOR_WITH_SPECIALIZATION_AVAILABLE_DATES = "/doctorWithSpecializationAvailableDates";
        String FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION = "/available-doctors-with-specialization";

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
    public static final String INFO = "/info";
    public static final String ID_PATH_VARIABLE_BASE = "/{id}";


    //J

    //K

    //L
    public static final String LOGIN = "/login";

    //M
    public static final String MIN = "/min";
    public static final String META_INFO = "/metaInfo";

    public interface MinioFileConstants {
        String BASE_FILE = "/file";
        String FETCH_FILE = "/{subDirectory}/{object}";
    }

    //N


    //O
    public static final String OTHERS = "/others";

    //P
    public interface PatientConstant {
        String BASE_PATIENT = "/patient";
        String HOSPITAL_PATIENT_INFO_ID_PATH_VARIABLE_BASE = "/{hospitalPatientInfoId}";
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
    public static final String SAVE = "/save";
    public static final String SEARCH = "/search";

    public static final String SELF = "/self";

    public interface SidebarConstants {
        String BASE_SIDE_BAR = "/sidebar";
    }


    public interface SpecializationConstants {
        String BASE_SPECIALIZATION = "/specialization";
        String SPECIALIZATION_WISE = "/specialization-wise";
    }

    public static final String SPECIALIZATION_ID_PATH_VARIABLE_BASE = "/{specializationId}";


    //T

    //U
    public static final String UPDATE = "/update";

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

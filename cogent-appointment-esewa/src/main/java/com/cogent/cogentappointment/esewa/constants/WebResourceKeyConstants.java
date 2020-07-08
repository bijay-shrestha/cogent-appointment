package com.cogent.cogentappointment.esewa.constants;

/**
 * @author smriti on 7/5/19
 */
public class WebResourceKeyConstants {

    //A
    public static final String API_V1 = "/api/v1";
    public static final String API_V2 = "/api/v2";

    public static final String ACTIVE = "/active";

    public interface AddressConstants {
        String BASE_ADDRESS = "/address";
        String NEW = "/new";
        String OLD = "/old";
        String ZONE = "/zone";
        String PROVINCE = "/province";
        String DISTRICT = "/district";
        String MUNICIPALITY = "/municipality";
        String STREET = "/street";
        String ZONE_ID_PATH_VARIABLE_BASE = "/{zoneId}";
        String PROVINCE_ID_PATH_VARIABLE_BASE = "/{provinceId}";
        String DISTRICT_ID_PATH_VARIABLE_BASE = "/{districtId}";
    }

    public interface AppointmentConstants {
        String BASE_APPOINTMENT = "/appointment";
        String APPOINTMENT_ID_PATH_VARIABLE_BASE = "/{appointmentId}";
        String APPOINTMENT_RESERVATION_ID_PATH_VARIABLE_BASE = "/{appointmentReservationId}";
        String FETCH_AVAILABLE_TIMESLOTS = "/availableTimeSlots";
        String FETCH_CURRENT_AVAILABLE_TIMESLOTS = "/availableTimeSlots/current";
        String PENDING_APPOINTMENT = "/pending-appointment";
        String CANCEL = "/cancel";
        String RESCHEDULE = "/reschedule";
        String FOLLOW_UP = "/follow-up";
        String HISTORY = "/history";
        String TRANSACTION_STATUS = "/transaction-status";
    }

    public interface AppointmentHospitalDepartmentConstants {
        String BASE_APPOINTMENT = "/appointment";
        String FETCH_AVAILABLE_TIMESLOTS = "/availableTimeSlots";
        String ROOM_WISE = "/roomWise";
        String AVAILABLE_DATES = "/availableDates";
    }


    public interface AppointmentServiceType {
        String APPOINTMENT_SERVICE_TYPE = "/appointmentServiceType";
    }

    //B

    //C
    public interface CommonConstants {
        String BASE_COMMON = "/commons";
        String DOCTOR_SPECIALIZATION = "/doctor-specialization";
    }


    //D


    public static final String DETAIL = "/detail";
    public static final String DELETE = "/delete";


    public static final String DOCTOR_ID_PATH_VARIABLE_BASE = "/{doctorId}";


    //E

    public static final String requestKey="data";
    public interface EsewaConstants {
        String BASE_ESEWA = "/esewa";
        String FETCH_DOCTOR_AVAILABLE_STATUS = "/doctor-available-status";
        String AVAILABLE_APPOINTMENT_DATES_AND_TIME = "/availableAppointmentDatesAndTime";
        String DOCTOR_AVAILABLE_DATES = "/doctorAvailableDates";
        String SPECIALIZATION_AVAILABLE_DATES = "/specializationAvailableDates";
        String DOCTOR_WITH_SPECIALIZATION_AVAILABLE_DATES = "/doctorWithSpecializationAvailableDates";
        String FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION = "/availableDoctorsWithSpecialization";

    }

    //F

    //G

    //H
    public interface HospitalConstants {
        String BASE_HOSPITAL = "/hospital";
        String HOSPITAL_ID_PATH_VARIABLE_BASE = "/{hospitalId}";
    }

    public interface HospitalDepartmentConstants {
        String BASE_HOSPITAL_DEPARTMENT = "/hospitalDepartment";
        String HOSPITAL_DEPARTMENT_ID = "/{hospitalDepartmentId}";
        String BILLING_MODE = "/billingMode";
    }


    //I
    public static final String INFO = "/info";
    public static final String ID_PATH_VARIABLE_BASE = "/{id}";


    //J

    //K

    //L

    //M
    public static final String MIN = "/min";

    public interface MinioResourceConstant {
        String BASE_FILE = "/file";
        String PUT_PERSIGNED_URL = "putPresignedURL";
        String GET_PERSIGNED_URL = "getPresignedURL";
    }

    //N


    //O
    public static final String OTHERS = "/others";

    //P
    public interface PatientConstant {
        String BASE_PATIENT = "/patient";
        String HOSPITAL_PATIENT_INFO_ID_PATH_VARIABLE_BASE = "/{hospitalPatientInfoId}";

    }


    //Q


    //R
    public interface RefundStatusConstants {
        String BASE_REFUND_STATUS = "/refund";
        String APPROVE = "/approve";
    }


    //S

    public static final String SEARCH = "/search";

    public static final String SELF = "/self";

    public static final String SPECIALIZATION_ID_PATH_VARIABLE_BASE = "/{specializationId}";

    public static String SCHEDULER_RUNNING = " :::: {} SCHEDULER RUNNING ::::";


    //T
    public interface TestResourceConstant {
        String BASE_TEST_RESOURCE = "/test";
    }

    //U
    public static final String UPDATE = "/update";

    //V

    //W

    //X

    //Y

    //Z
}

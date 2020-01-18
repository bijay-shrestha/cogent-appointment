package com.cogent.cogentappointment.constants;

public class SwaggerConstants {
    public static String BASE_PACKAGE = "com.cogent.cogentappointment.resource";

    public static String PATH_REGEX = "/api.*";

    //A
    public interface AppointmentConstant {
        String BASE_API_VALUE = "This is Appointment Controller";
        String CHECK_APPOINTMENT_AVAILABILITY = "Check available time schedules of doctor";
        String SAVE_OPERATION = "Save new appointment";
        String UPDATE_OPERATION = "Update existing appointment";
        String DELETE_OPERATION = "Set appointment status as 'D' when deleted";
        String SEARCH_OPERATION = "Search appointment according to given request parameters";
        String DETAILS_OPERATION = "Fetch appointment details by its id";
        String RESCHEDULE_OPERATION = "Reschedule appointment date and time.";
        String FETCH_APPOINTMENT_DATES = "Fetch booked appointment dates within requested date range" +
                " for specific doctor and specialization";
    }

    //B

    //C
    public interface CountryConstant {
        String BASE_API_VALUE = "This is Country Controller.";
        String FETCH_ACTIVE_COUNTRY = "Fetch active country for dropdown.";
    }


    //D
    public interface DoctorConstant {
        String BASE_API_VALUE = "This is Doctor Controller";
        String SAVE_OPERATION = "Save new Doctor like Dr.Sanjeeev Upreti, Dr. Daniel Shrestha, etc";
        String UPDATE_OPERATION = "Update existing Doctor";
        String DELETE_OPERATION = "Set Doctor status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Doctor according to given request parameters";
        String DETAILS_OPERATION = "Fetch Doctor details by its id";
        String DETAILS_FOR_UPDATE_MODAL_OPERATION = "Fetch Doctor details for update modal by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch Doctor details (id and name) for dropdown";
    }


    //E


    //F


    //G

    //H
    public interface HospitalConstant {
        String BASE_API_VALUE = "This is Hospital setup Controller";
        String SAVE_OPERATION = "Save new hospital";
        String UPDATE_OPERATION = "Update existing hospital";
        String DELETE_OPERATION = "Set hospital status as 'D' when deleted with remarks";
        String SEARCH_OPERATION = "Search hospital according to given request parameters";
        String DETAILS_OPERATION = "Fetch hospital details by id";
        String FETCH_BY_ID = "Fetch hospital entity by its id";
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
        String SEARCH_PATIENT_WITH_SELF_OPERATION = "Search patient info according to given " +
                "request parameters (esewa id, isSelf='Y' and hospital id";
        String SAVE_PATIENT_OPERATION = "Save new patient";
        String UPDATE_PATIENT_OPERATION = "Update existing patient";
        String DELETE_PATIENT_OPERATION = "Set patient status as 'D' when deleted";
        String PATIENT_DETAILS_OPERATION = "Fetch patient details";
        String FETCH_PATIENT_FOR_DROP_DOWN_OPERATION = "Fetch minimal patient details (id and name) for dropdown";
        String FETCH_ACTIVE_PATIENT_FOR_DROP_DOWN_OPERATION = "Fetch minimal active patient details (id and name)" +
                " for dropdown";
        String DOWNLOAD_PATIENT_EXCEL_OPERATION = "Download excelsheet of patient in byte array format";
        String FETCH_PATIENT_BY_ID = "Fetch patient by id (feign-client)";
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
    }


    //T


    //U

    //V

    //W


    //X
    //Y
    //Z
}

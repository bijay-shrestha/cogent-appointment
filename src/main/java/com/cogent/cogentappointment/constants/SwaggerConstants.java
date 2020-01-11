package com.cogent.cogentappointment.constants;

public class SwaggerConstants {
    public static String BASE_PACKAGE = "com.cogent.cogentappointment.resource";

    public static String PATH_REGEX = "/api.*";

    //A

    //B

    //C
    public interface CountryConstant {
        String BASE_API_VALUE = "This is Country Controller.";
        String FETCH_ACTIVE_COUNTRY = "Fetch active country for dropdown.";
    }


    //D


    //E


    //F


    //G

    //H


    //I

    //J
    //K
    //L
    //M

    //N


    //O

    //P

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

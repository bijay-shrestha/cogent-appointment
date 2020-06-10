package com.cogent.cogentappointment.logging.constants;

public class SwaggerConstants {
    public static String BASE_PACKAGE = "com.cogent.cogentappointment.logging.resource";

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
            "/api/v1/admin-log/test",
            "/webjars/**"
            // other public endpoints of your API may be appended to this array
    };

    public interface AdminLogConstant {
        String BASE_API_VALUE = "This is Admin Log Resource";
        String TEST_RESOURCE = "This is Test Resource";
        String SEARCH_OPERATION = "Search admin logs according to given request parameters";
        String TEST_OPERATION = "Test If logging is running.";
        String USER_MENU_STATICS_OPERATION = "Fetch user logs statics";
        String USER_MENU_LOG_DIAGRAM_OPERATION = "Fetch user logs data for user logs diagram";

    }

    //C
    public interface ClientLogConstant {
        String BASE_API_VALUE = "This is Client Log Resource";
        String SEARCH_OPERATION = "Search client logs according to given request parameters";
        String DETAILS_OPERATION = "Fetch client log details by its id";

    }

    //X
    //Y
    //Z
}

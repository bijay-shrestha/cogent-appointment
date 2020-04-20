package com.cogent.cogentappointment.admin.log.constants;

/**
 * @author smriti on 2019-08-23
 */
public class AdminLog {
    public static final String ADMIN = "ADMIN";

    public static final String ADMIN_AVATAR = "ADMIN AVATAR";

    public static final String ADMIN_META_INFO = "ADMIN META INFO";

    public static final String FORGOT_PASSWORD_PROCESS_STARTED = ":::: FORGOT PASSWORD PROCESS STARTED ::::";
    public static final String FORGOT_PASSWORD_PROCESS_COMPLETED = ":::: FORGOT PASSWORD PROCESS COMPLETED" +
            " IN :::: {} ms";

    public static final String SAVING_PASSWORD_PROCESS_STARTED = ":::: SAVING PASSWORD PROCESS STARTED ::::";
    public static final String SAVING_PASSWORD_PROCESS_COMPLETED = ":::: SAVING PASSWORD PROCESS COMPLETED" +
            " IN :::: {} ms";

    public static final String UPDATING_PASSWORD_PROCESS_STARTED = ":::: UPDATING ADMIN PASSWORD PROCESS STARTED ::::";
    public static final String UPDATING_PASSWORD_PROCESS_COMPLETED = ":::: UPDATING ADMIN PASSWORD PROCESS COMPLETED" +
            " IN :::: {} ms";

    public static final String VERIFY_CODE_PROCESS_STARTED = ":::: VERIFICATION OF PASSWORD RESET CODE PROCESS " +
            "STARTED ::::";
    public static final String VERIFY_CODE_PROCESS_COMPLETED = "::::VERIFICATION OF PASSWORD RESET CODE PROCESS " +
            "COMPLETED IN :::: {} ms";

    public static final String VERIFY_CONFIRMATION_TOKEN_PROCESS_STARTED = ":::: VERIFICATION OF CONFIRMATION TOKEN " +
            "PROCESS STARTED ::::";
    public static final String VERIFY_CONFIRMATION_TOKEN_PROCESS_COMPLETED = "::::VERIFICATION OF CONFIRMATION TOKEN" +
            " PROCESS COMPLETED IN :::: {} ms";

    public static String INVALID_CONFIRMATION_TOKEN_ERROR = ":::: INVALID CONFIRMATION TOKEN: {} ::::";

    public final static String ADMIN_NOT_ACTIVE_ERROR=":::: ADMIN WITH USERNAME/EMAIL : {} NOT ACTIVE ::::";

    public final static String ADMIN_NOT_FOUND_ERROR=":::: ADMIN WITH EMAIL : {} NOT FOUND ::::";

    public final static String CONFORMATION_TOKEN_NOT_FOUND=":::: CONFORMATION TOKEN : {} NOT FOUND  ::::";

}

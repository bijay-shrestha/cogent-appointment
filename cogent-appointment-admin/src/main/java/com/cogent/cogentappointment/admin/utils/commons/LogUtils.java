package com.cogent.cogentappointment.admin.utils.commons;

import lombok.extern.slf4j.Slf4j;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.ERROR_LOG;


@Slf4j
public class LogUtils {
    public static void logError(String name) {
        log.error(ERROR_LOG,name);
    }
}

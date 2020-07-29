package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpRequestLog;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;

/**
 * @author smriti on 29/03/20
 */
public class AppointmentFollowUpRequestLogUtils {

    public static AppointmentFollowUpRequestLog parseToAppointmentFollowUpRequestLog
            (AppointmentFollowUpTracker appointmentFollowUpTracker) {

        AppointmentFollowUpRequestLog requestLog = new AppointmentFollowUpRequestLog();
        requestLog.setAppointmentFollowUpTracker(appointmentFollowUpTracker);
        requestLog.setFollowUpRequestedCount(0);
        return requestLog;
    }
}

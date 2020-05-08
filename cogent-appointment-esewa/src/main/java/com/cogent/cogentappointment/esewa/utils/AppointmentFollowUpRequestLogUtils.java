package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpRequestLog;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;

/**
 * @author smriti on 29/03/20
 */
public class AppointmentFollowUpRequestLogUtils {

    public static void updateAppointmentFollowUpRequestLog(AppointmentFollowUpRequestLog appointmentFollowUpRequestLog) {
        appointmentFollowUpRequestLog.setFollowUpRequestedCount
                (appointmentFollowUpRequestLog.getFollowUpRequestedCount() + 1);
    }
}

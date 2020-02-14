package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpLog;

/**
 * @author smriti on 13/02/20
 */
public class AppointmentFollowUpLogUtils {

    public static AppointmentFollowUpLog parseToAppointmentFollowUpLog(Long parentAppointmentId,
                                                                       Long followUpAppointmentId) {

        AppointmentFollowUpLog appointmentFollowUpLog = new AppointmentFollowUpLog();
        appointmentFollowUpLog.setParentAppointmentId(parentAppointmentId);
        appointmentFollowUpLog.setFollowUpAppointmentId(followUpAppointmentId);
        return appointmentFollowUpLog;
    }
}

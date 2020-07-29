package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpLog;

/**
 * @author smriti on 13/02/20
 */
public class AppointmentHospitalDepartmentFollowUpLogUtils {

    public static AppointmentHospitalDepartmentFollowUpLog parseToAppointmentHospitalDepartmentFollowUpLog(
            Long parentAppointmentId,
            Long followUpAppointmentId) {

        AppointmentHospitalDepartmentFollowUpLog appointmentFollowUpLog = new AppointmentHospitalDepartmentFollowUpLog();
        appointmentFollowUpLog.setParentAppointmentId(parentAppointmentId);
        appointmentFollowUpLog.setFollowUpAppointmentId(followUpAppointmentId);
        return appointmentFollowUpLog;
    }
}

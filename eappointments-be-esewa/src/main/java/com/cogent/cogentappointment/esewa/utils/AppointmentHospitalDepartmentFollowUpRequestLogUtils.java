package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpRequestLog;

/**
 * @author smriti on 29/03/20
 */
public class AppointmentHospitalDepartmentFollowUpRequestLogUtils {

    public static void updateAppointmentHospitalDeptFollowUpRequestLog(
            AppointmentHospitalDepartmentFollowUpRequestLog appointmentFollowUpRequestLog) {

        appointmentFollowUpRequestLog.setFollowUpRequestedCount(
                appointmentFollowUpRequestLog.getFollowUpRequestedCount() + 1);
    }
}

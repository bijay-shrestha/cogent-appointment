package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpRequestLog;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;

/**
 * @author smriti on 29/03/20
 */
public class AppointmentHospitalDepartmentFollowUpRequestLogUtils {

    public static AppointmentHospitalDepartmentFollowUpRequestLog parseFollowUpRequestLogDetails
            (AppointmentHospitalDepartmentFollowUpTracker appointmentHospitalDepartmentFollowUpTracker) {

        AppointmentHospitalDepartmentFollowUpRequestLog requestLog = new AppointmentHospitalDepartmentFollowUpRequestLog();
        requestLog.setAppointmentHospitalDepartmentFollowUpTracker(appointmentHospitalDepartmentFollowUpTracker);
        requestLog.setFollowUpRequestedCount(0);
        return requestLog;
    }
}

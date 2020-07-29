package com.cogent.cogentappointment.admin.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDoctorInfo;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRoster;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRosterDoctorInfo;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;

/**
 * @author smriti on 07/06/20
 */
public class HospitalDepartmentWeekDaysDutyRosterDoctorInfoUtils {

    public static HospitalDepartmentWeekDaysDutyRosterDoctorInfo parseWeekDaysDoctorDetails(
            HospitalDepartmentWeekDaysDutyRoster weekDaysDutyRoster,
            HospitalDepartmentDoctorInfo hospitalDepartmentDoctorInfo) {

        HospitalDepartmentWeekDaysDutyRosterDoctorInfo doctorInfo = new HospitalDepartmentWeekDaysDutyRosterDoctorInfo();
        doctorInfo.setHospitalDepartmentWeekDaysDutyRoster(weekDaysDutyRoster);
        doctorInfo.setHospitalDepartmentDoctorInfo(hospitalDepartmentDoctorInfo);
        doctorInfo.setStatus(ACTIVE);
        return doctorInfo;
    }

    public static HospitalDepartmentWeekDaysDutyRosterDoctorInfo updateWeekDaysDoctorDetails(
            HospitalDepartmentWeekDaysDutyRosterDoctorInfo weekDaysDoctorInfo,
            Character status) {
        weekDaysDoctorInfo.setStatus(status);
        return weekDaysDoctorInfo;
    }
}

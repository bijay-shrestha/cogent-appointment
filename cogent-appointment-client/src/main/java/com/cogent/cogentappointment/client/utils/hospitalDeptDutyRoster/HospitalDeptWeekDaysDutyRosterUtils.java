package com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptWeekDaysDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptWeekDaysDutyRosterUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRoster;
import com.cogent.cogentappointment.persistence.model.WeekDays;

import java.util.List;

import static com.cogent.cogentappointment.client.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptWeekDaysDutyRosterUtils {

    public static HospitalDepartmentWeekDaysDutyRoster parseToHospitalDeptWeekDaysDutyRoster(
            HospitalDeptWeekDaysDutyRosterRequestDTO requestDTO,
            HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
            WeekDays weekDays) {

        HospitalDepartmentWeekDaysDutyRoster weekDaysDutyRoster = new HospitalDepartmentWeekDaysDutyRoster();
        weekDaysDutyRoster.setStartTime(requestDTO.getStartTime());
        weekDaysDutyRoster.setEndTime(requestDTO.getEndTime());
        weekDaysDutyRoster.setDayOffStatus(requestDTO.getDayOffStatus());
        weekDaysDutyRoster.setHospitalDepartmentDutyRoster(hospitalDepartmentDutyRoster);
        weekDaysDutyRoster.setWeekDays(weekDays);
        weekDaysDutyRoster.setIsDoctorAvailable(isDoctorAvailable(requestDTO.getHospitalDepartmentDoctorInfoIds()));

        return weekDaysDutyRoster;
    }

    public static Character isDoctorAvailable(List<Long> doctorIdList) {
        return doctorIdList.size() > 0 ? YES : NO;
    }

    public static HospitalDepartmentWeekDaysDutyRoster parseUpdatedWeekDaysDetails(
            HospitalDeptWeekDaysDutyRosterUpdateRequestDTO updateRequestDTO,
            HospitalDepartmentWeekDaysDutyRoster weekDaysDutyRoster) {

        weekDaysDutyRoster.setStartTime(updateRequestDTO.getStartTime());
        weekDaysDutyRoster.setEndTime(updateRequestDTO.getEndTime());
        weekDaysDutyRoster.setDayOffStatus(updateRequestDTO.getDayOffStatus());
        return weekDaysDutyRoster;
    }
}

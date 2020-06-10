package com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorWeekDaysDutyRosterUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptWeekDaysDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptWeekDaysDutyRosterUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.*;

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
        return weekDaysDutyRoster;
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

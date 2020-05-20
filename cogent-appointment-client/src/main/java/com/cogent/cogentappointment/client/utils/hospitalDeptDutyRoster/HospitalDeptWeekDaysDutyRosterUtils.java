package com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptWeekDaysDutyRosterRequestDTO;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRoster;
import com.cogent.cogentappointment.persistence.model.WeekDays;

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

}

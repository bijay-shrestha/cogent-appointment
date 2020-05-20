package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptDutyRosterOverrideRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptWeekDaysDutyRosterRequestDTO;
import com.cogent.cogentappointment.persistence.model.*;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptDutyRosterUtils {

    public static HospitalDepartmentDutyRoster parseToHospitalDepartmentDutyRoster(
            HospitalDepartmentDutyRosterRequestDTO requestDTO,
            HospitalDepartment hospitalDepartment) {

        HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster = new HospitalDepartmentDutyRoster();
        hospitalDepartmentDutyRoster.setHospitalDepartment(hospitalDepartment);
        hospitalDepartmentDutyRoster.setFromDate(requestDTO.getFromDate());
        hospitalDepartmentDutyRoster.setToDate(requestDTO.getToDate());
        hospitalDepartmentDutyRoster.setRosterGapDuration(requestDTO.getRosterGapDuration());
        hospitalDepartmentDutyRoster.setStatus(requestDTO.getStatus());
        hospitalDepartmentDutyRoster.setHasOverrideDutyRoster(requestDTO.getHasOverrideDutyRoster());
        hospitalDepartmentDutyRoster.setIsRoomEnabled(requestDTO.getIsRoomEnabled());
        return hospitalDepartmentDutyRoster;
    }

    public static HospitalDepartmentWeekDaysDutyRoster parseToSpecializationWeekDaysDutyRoster(
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

    public static HospitalDepartmentDutyRosterOverride parseToSpecializationDutyRosterOverride(
            HospitalDeptDutyRosterOverrideRequestDTO requestDTO,
            HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster) {

        HospitalDepartmentDutyRosterOverride hospitalDepartmentDutyRosterOverride = new HospitalDepartmentDutyRosterOverride();
        hospitalDepartmentDutyRosterOverride.setFromDate(requestDTO.getFromDate());
        hospitalDepartmentDutyRosterOverride.setToDate(requestDTO.getToDate());
        hospitalDepartmentDutyRosterOverride.setStartTime(requestDTO.getStartTime());
        hospitalDepartmentDutyRosterOverride.setEndTime(requestDTO.getEndTime());
        hospitalDepartmentDutyRosterOverride.setDayOffStatus(requestDTO.getDayOffStatus());
        hospitalDepartmentDutyRosterOverride.setStatus(requestDTO.getStatus());
        hospitalDepartmentDutyRosterOverride.setRemarks(requestDTO.getRemarks());
        hospitalDepartmentDutyRosterOverride.setHospitalDepartmentDutyRoster(hospitalDepartmentDutyRoster);
        return hospitalDepartmentDutyRosterOverride;
    }
}

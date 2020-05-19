package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.specializationDutyRoster.SpecializationDutyRosterOverrideRequestDTO;
import com.cogent.cogentappointment.client.dto.request.specializationDutyRoster.SpecializationDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.request.specializationDutyRoster.SpecializationWeekDaysDutyRosterRequestDTO;
import com.cogent.cogentappointment.persistence.model.*;

/**
 * @author Sauravi Thapa ON 5/19/20
 */
public class SpecializationDutyRosterUtils {

    public static SpecializationDutyRoster parseToSpecializationDutyRoster(SpecializationDutyRosterRequestDTO requestDTO,
                                                           Specialization specialization,
                                                           Hospital hospital) {

        SpecializationDutyRoster specializationDutyRoster = new SpecializationDutyRoster();
        specializationDutyRoster.setFromDate(requestDTO.getFromDate());
        specializationDutyRoster.setToDate(requestDTO.getToDate());
        specializationDutyRoster.setRosterGapDuration(requestDTO.getRosterGapDuration());
        specializationDutyRoster.setStatus(requestDTO.getStatus());
        specializationDutyRoster.setHasOverrideDutyRoster(requestDTO.getHasOverrideDutyRoster());
        specializationDutyRoster.setHospital(hospital);
        specializationDutyRoster.setSpecialization(specialization);
        return specializationDutyRoster;
    }

    public static SpecializationWeekDaysDutyRoster parseToSpecializationWeekDaysDutyRoster(
            SpecializationWeekDaysDutyRosterRequestDTO requestDTO,
            SpecializationDutyRoster specializationDutyRoster,
            WeekDays weekDays) {

        SpecializationWeekDaysDutyRoster weekDaysDutyRoster = new SpecializationWeekDaysDutyRoster();
        weekDaysDutyRoster.setStartTime(requestDTO.getStartTime());
        weekDaysDutyRoster.setEndTime(requestDTO.getEndTime());
        weekDaysDutyRoster.setDayOffStatus(requestDTO.getDayOffStatus());
        weekDaysDutyRoster.setSpecializationDutyRoster(specializationDutyRoster);
        weekDaysDutyRoster.setWeekDays(weekDays);
        return weekDaysDutyRoster;
    }

    public static SpecializationDutyRosterOverride parseToSpecializationDutyRosterOverride(
            SpecializationDutyRosterOverrideRequestDTO requestDTO,
            SpecializationDutyRoster specializationDutyRoster) {

        SpecializationDutyRosterOverride specializationDutyRosterOverride = new SpecializationDutyRosterOverride();
        specializationDutyRosterOverride.setFromDate(requestDTO.getFromDate());
        specializationDutyRosterOverride.setToDate(requestDTO.getToDate());
        specializationDutyRosterOverride.setStartTime(requestDTO.getStartTime());
        specializationDutyRosterOverride.setEndTime(requestDTO.getEndTime());
        specializationDutyRosterOverride.setDayOffStatus(requestDTO.getDayOffStatus());
        specializationDutyRosterOverride.setStatus(requestDTO.getStatus());
        specializationDutyRosterOverride.setRemarks(requestDTO.getRemarks());
        specializationDutyRosterOverride.setSpecializationDutyRoster(specializationDutyRoster);
        return specializationDutyRosterOverride;
    }
}

package com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterUpdateDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.detail.*;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;

import java.util.List;

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

    public static void parseDeletedDetails(HospitalDepartmentDutyRoster doctorDutyRoster,
                                           DeleteRequestDTO deleteRequestDTO) {
        doctorDutyRoster.setStatus(deleteRequestDTO.getStatus());
        doctorDutyRoster.setRemarks(deleteRequestDTO.getRemarks());
    }

    public static HospitalDeptDutyRosterDetailResponseDTO parseHDDRosterDetails(
            HospitalDeptDutyRosterResponseDTO dutyRosterDetail,
            HospitalDeptDutyRosterRoomResponseDTO roomInfo,
            List<HospitalDeptWeekDaysDutyRosterResponseDTO> weekDaysRosters,
            List<HospitalDeptDutyRosterOverrideResponseDTO> overrideRosters) {

        return HospitalDeptDutyRosterDetailResponseDTO.builder()
                .dutyRosterDetail(dutyRosterDetail)
                .roomInfo(roomInfo)
                .weekDaysRosters(weekDaysRosters)
                .overrideRosters(overrideRosters)
                .build();
    }

    public static void parseToUpdatedRosterDetails(HospitalDepartmentDutyRoster dutyRoster,
                                                   HospitalDeptDutyRosterUpdateDTO updateRequestDTO) {

        dutyRoster.setRosterGapDuration(updateRequestDTO.getRosterGapDuration());
        dutyRoster.setStatus(updateRequestDTO.getStatus());
        dutyRoster.setRemarks(updateRequestDTO.getRemarks());
        dutyRoster.setHasOverrideDutyRoster(updateRequestDTO.getHasOverrideDutyRoster());
        dutyRoster.setIsRoomEnabled(updateRequestDTO.getIsRoomEnabled());
    }

}

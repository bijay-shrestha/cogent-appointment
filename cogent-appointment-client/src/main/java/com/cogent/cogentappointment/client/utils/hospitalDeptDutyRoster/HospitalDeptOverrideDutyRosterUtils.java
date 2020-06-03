package com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptDutyRosterOverrideRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateResponseDTO;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterOverride;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptOverrideDutyRosterUtils {

    public static HospitalDepartmentDutyRosterOverride parseOverrideDetails(
            HospitalDeptDutyRosterOverrideRequestDTO requestDTO,
            HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
            HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo) {

        HospitalDepartmentDutyRosterOverride override = new HospitalDepartmentDutyRosterOverride();
        override.setFromDate(requestDTO.getFromDate());
        override.setToDate(requestDTO.getToDate());
        override.setStartTime(requestDTO.getStartTime());
        override.setEndTime(requestDTO.getEndTime());
        override.setDayOffStatus(requestDTO.getDayOffStatus());
        override.setStatus(requestDTO.getStatus());
        override.setRemarks(requestDTO.getRemarks());
        override.setHospitalDepartmentDutyRoster(hospitalDepartmentDutyRoster);
        override.setHospitalDepartmentRoomInfo(hospitalDepartmentRoomInfo);

        return override;
    }

    public static HospitalDepartmentDutyRosterOverride parseOverrideDetails(
            HospitalDeptDutyRosterOverrideUpdateRequestDTO updateRequestDTO,
            HospitalDepartmentDutyRosterOverride override,
            HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo) {

        override.setFromDate(updateRequestDTO.getFromDate());
        override.setToDate(updateRequestDTO.getToDate());
        override.setEndTime(updateRequestDTO.getEndTime());
        override.setStartTime(updateRequestDTO.getStartTime());
        override.setDayOffStatus(updateRequestDTO.getDayOffStatus());
        override.setStatus(updateRequestDTO.getStatus());
        override.setRemarks(updateRequestDTO.getRemarks());
        override.setHospitalDepartmentRoomInfo(hospitalDepartmentRoomInfo);
        return override;
    }

    public static HospitalDeptDutyRosterOverrideUpdateResponseDTO parseOverrideUpdateResponse(Long savedOverrideId) {
        return HospitalDeptDutyRosterOverrideUpdateResponseDTO.builder()
                .savedOverrideId(savedOverrideId)
                .build();
    }

    public static void parseDeletedOverrideDetails(HospitalDepartmentDutyRosterOverride override,
                                                   DeleteRequestDTO requestDTO) {

        override.setStatus(requestDTO.getStatus());
        override.setRemarks(requestDTO.getRemarks());
    }



}

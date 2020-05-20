package com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptDutyRosterOverrideRequestDTO;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterOverride;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptOverrideDutyRosterUtils {

    public static HospitalDepartmentDutyRosterOverride parseToSpecializationDutyRosterOverride(
            HospitalDeptDutyRosterOverrideRequestDTO requestDTO,
            HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster) {

        HospitalDepartmentDutyRosterOverride override = new HospitalDepartmentDutyRosterOverride();
        override.setFromDate(requestDTO.getFromDate());
        override.setToDate(requestDTO.getToDate());
        override.setStartTime(requestDTO.getStartTime());
        override.setEndTime(requestDTO.getEndTime());
        override.setDayOffStatus(requestDTO.getDayOffStatus());
        override.setStatus(requestDTO.getStatus());
        override.setRemarks(requestDTO.getRemarks());
        override.setHospitalDepartmentDutyRoster(hospitalDepartmentDutyRoster);

        return override;
    }

}

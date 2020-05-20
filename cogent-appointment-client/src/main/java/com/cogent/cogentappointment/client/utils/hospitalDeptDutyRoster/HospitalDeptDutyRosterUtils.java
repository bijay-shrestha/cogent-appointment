package com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;

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
}

package com.cogent.cogentappointment.repository.custom;

import com.cogent.admin.dto.request.doctorDutyRoster.DoctorDutyRosterStatusRequestDTO;
import com.cogent.admin.dto.request.doctorDutyRoster.DoctorDutyRosterTimeRequestDTO;
import com.cogent.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.admin.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Qualifier("doctorDutyRosterOverrideRepositoryCustom")
public interface DoctorDutyRosterOverrideRepositoryCustom {

    Long validateDoctorDutyRosterOverrideCount(Long doctorId, Long specializationId,
                                               Date fromDate, Date toDate);

    DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterOverrideTime(DoctorDutyRosterTimeRequestDTO requestDTO);

    List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterOverrideStatus
            (DoctorDutyRosterStatusRequestDTO requestDTO);
}

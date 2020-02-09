package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.DoctorDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.DoctorDutyRosterStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterOverride;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Qualifier("doctorDutyRosterOverrideRepositoryCustom")
public interface DoctorDutyRosterOverrideRepositoryCustom {

    Long fetchOverrideCount(Long doctorId, Long specializationId, Date fromDate, Date toDate);

    Long fetchOverrideCount(Long doctorDutyRosterOverrideId, Long doctorId,
                            Long specializationId, Date fromDate, Date toDate);

    DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterOverrideTime(Date date,
                                                                      Long doctorId,
                                                                      Long specializationId);

    List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterOverrideStatus
            (DoctorDutyRosterStatusRequestDTO requestDTO);

    List<DoctorDutyRosterOverride> fetchDoctorDutyRosterOverrides(
            List<DoctorDutyRosterOverrideUpdateRequestDTO> updateRequestDTOS);
}

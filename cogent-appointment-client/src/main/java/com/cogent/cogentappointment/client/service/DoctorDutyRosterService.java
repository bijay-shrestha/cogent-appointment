package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.*;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 26/11/2019
 */
public interface DoctorDutyRosterService {
    void save(DoctorDutyRosterRequestDTO requestDTO);

    void update(DoctorDutyRosterUpdateRequestDTO updateRequestDTO);

    DoctorRosterOverrideUpdateResponseDTO updateDoctorDutyRosterOverride(
            DoctorDutyRosterOverrideUpdateRequestDTO updateRequestDTO);

    void deleteDoctorDutyRosterOverride(DeleteRequestDTO deleteRequestDTO);

    void revertDoctorDutyRosterOverride(List<DoctorDutyRosterOverrideUpdateRequestDTO> updateInfo);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<DoctorDutyRosterMinimalResponseDTO> search(DoctorDutyRosterSearchRequestDTO searchRequestDTO,
                                                    Pageable pageable);

    DoctorDutyRosterDetailResponseDTO fetchDetailsById(Long id);

    List<DoctorExistingDutyRosterResponseDTO> fetchExistingDutyRosters(DoctorExistingDutyRosterRequestDTO requestDTO);

    DoctorExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long doctorDutyRosterId);

    List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus(DoctorDutyRosterStatusRequestDTO requestDTO);

}

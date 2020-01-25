package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.request.doctorDutyRoster.*;
import com.cogent.cogentappointment.dto.response.doctorDutyRoster.DoctorDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.dto.response.doctorDutyRoster.DoctorDutyRosterMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 26/11/2019
 */
public interface DoctorDutyRosterService {
    void save(DoctorDutyRosterRequestDTO requestDTO);

    void update(DoctorDutyRosterUpdateRequestDTO updateRequestDTO);

    void updateDoctorDutyRosterOverride(DoctorDutyRosterOverrideUpdateRequestDTO updateRequestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<DoctorDutyRosterMinimalResponseDTO> search(DoctorDutyRosterSearchRequestDTO searchRequestDTO,
                                                    Pageable pageable);

    DoctorDutyRosterDetailResponseDTO fetchDetailsById(Long id);

    List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus(DoctorDutyRosterStatusRequestDTO requestDTO);

}

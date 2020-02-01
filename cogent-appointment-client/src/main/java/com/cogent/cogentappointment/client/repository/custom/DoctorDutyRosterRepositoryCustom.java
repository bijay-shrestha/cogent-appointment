package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorExistingDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 26/11/2019
 */
@Repository
@Qualifier("doctorDutyRosterRepositoryCustom")
public interface DoctorDutyRosterRepositoryCustom {

    Long validateDoctorDutyRosterCount(Long doctorId,
                                       Long specializationId,
                                       Date fromDate,
                                       Date toDate);

    List<DoctorDutyRosterMinimalResponseDTO> search(DoctorDutyRosterSearchRequestDTO searchRequestDTO,
                                                    Pageable pageable);

    DoctorDutyRosterDetailResponseDTO fetchDetailsById(Long id);

    DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterTime(Date date,
                                                              Long doctorId,
                                                              Long specializationId);

    List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus(DoctorDutyRosterStatusRequestDTO requestDTO);

    List<DoctorExistingDutyRosterResponseDTO> fetchExistingDoctorDutyRosters(
            DoctorExistingDutyRosterRequestDTO requestDTO);

    DoctorExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long doctorDutyRosterId);
}

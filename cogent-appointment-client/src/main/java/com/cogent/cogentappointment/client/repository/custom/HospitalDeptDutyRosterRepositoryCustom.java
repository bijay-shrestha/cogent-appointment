package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptExistingDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.HospitalDeptDutyRosterMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.detail.HospitalDeptDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.existing.HospitalDeptExistingDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.existing.HospitalDeptExistingDutyRosterResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 20/05/20
 */
@Repository
@Qualifier("hospitalDeptDutyRosterRepositoryCustom")
public interface HospitalDeptDutyRosterRepositoryCustom {

    Long fetchRosterCountWithoutRoom(Long hospitalDepartmentId, Date fromDate, Date toDate);

    List<HospitalDeptDutyRosterMinResponseDTO> search(HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO,
                                                      Pageable pageable, Long hospitalId);

    HospitalDeptDutyRosterDetailResponseDTO fetchDetailsById(Long id, Long hospitalId);

    List<HospitalDeptExistingDutyRosterResponseDTO> fetchExistingDutyRosters(
            HospitalDeptExistingDutyRosterRequestDTO requestDTO, Long hospitalId);

    HospitalDeptExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long hddRosterId);
}

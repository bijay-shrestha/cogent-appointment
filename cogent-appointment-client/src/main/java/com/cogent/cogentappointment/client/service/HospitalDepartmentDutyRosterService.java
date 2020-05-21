package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.HospitalDeptDutyRosterMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.detail.HospitalDeptDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 20/05/20
 */
public interface HospitalDepartmentDutyRosterService {

    void save(HospitalDepartmentDutyRosterRequestDTO requestDTO);

    List<HospitalDeptDutyRosterMinResponseDTO> search(HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO,
                                                      Pageable pageable);

    void delete(DeleteRequestDTO deleteRequestDTO);

    HospitalDeptDutyRosterDetailResponseDTO fetchDetailsById(Long id);

    void update(HospitalDeptDutyRosterUpdateRequestDTO updateRequestDTO);

    HospitalDeptDutyRosterOverrideUpdateResponseDTO updateOverride(
            HospitalDeptDutyRosterOverrideUpdateRequestDTO updateRequestDTO);

    void deleteOverride(DeleteRequestDTO deleteRequestDTO);

    void revertOverride(List<HospitalDeptDutyRosterOverrideUpdateRequestDTO> updateInfo);

}

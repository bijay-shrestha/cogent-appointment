package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileMenuSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.profile.AssignedProfileResponseDTO;
import com.cogent.cogentappointment.client.dto.response.profile.ProfileDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.profile.ProfileMinimalResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 7/10/19
 */
@Repository
@Qualifier("profileRepositoryCustom")
public interface ProfileRepositoryCustom {

    Long validateDuplicity(String name, Long hospitalId);

    Long validateDuplicityForUpdate(Long profileId, String name, Long hospitalId);

    List<ProfileMinimalResponseDTO> search(ProfileSearchRequestDTO searchRequestDTO,
                                           Long hospitalId,
                                           Pageable pageable);

    ProfileDetailResponseDTO fetchDetailsById(Long id, Long hospitalId);

    List<DropDownResponseDTO> fetchActiveMinProfile(Long hospitalId);

    List<DropDownResponseDTO> fetchMinProfile(Long hospitalId);

    List<DropDownResponseDTO> fetchActiveProfileByDepartmentAndHospitalId(Long departmentId, Long hospitalId);

    List<DropDownResponseDTO> fetchProfileByDepartmentAndHospitalId(Long departmentId, Long hospitalId);

    AssignedProfileResponseDTO fetchAssignedProfile(ProfileMenuSearchRequestDTO profileMenuSearchRequestDTO);
}

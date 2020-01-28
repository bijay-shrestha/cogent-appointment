package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.profile.ProfileMenuSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.AssignedProfileResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.ProfileDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.ProfileMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
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

    List<ProfileMinimalResponseDTO> search(ProfileSearchRequestDTO searchRequestDTO, Pageable pageable);

    ProfileDetailResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveProfilesForDropDown();

    List<DropDownResponseDTO> fetchProfileByDepartmentId(Long departmentId);

    AssignedProfileResponseDTO fetchAssignedProfileResponseDto(ProfileMenuSearchRequestDTO profileMenuSearchRequestDTO);
}

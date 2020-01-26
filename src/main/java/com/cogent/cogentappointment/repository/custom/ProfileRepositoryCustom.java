package com.cogent.cogentappointment.repository.custom;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.profile.ProfileMenuSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.dto.response.profile.AssignedProfileResponseDTO;
import com.cogent.cogentappointment.dto.response.profile.ProfileDetailResponseDTO;
import com.cogent.cogentappointment.dto.response.profile.ProfileMinimalResponseDTO;
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

    Long findProfileCountByName(String name);

    Long findProfileCountByIdAndName(Long id, String name);

    List<ProfileMinimalResponseDTO> search(ProfileSearchRequestDTO searchRequestDTO, Pageable pageable);

    ProfileDetailResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveProfilesForDropDown();

    List<DropDownResponseDTO> fetchProfileByDepartmentId(Long departmentId);

    AssignedProfileResponseDTO fetchAssignedProfileResponseDto(ProfileMenuSearchRequestDTO profileMenuSearchRequestDTO);
}

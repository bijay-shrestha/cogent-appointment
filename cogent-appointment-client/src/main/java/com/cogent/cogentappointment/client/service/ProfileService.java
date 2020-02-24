package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileMenuSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.profile.AssignedProfileResponseDTO;
import com.cogent.cogentappointment.client.dto.response.profile.ProfileDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.profile.ProfileMinimalResponseDTO;
import com.cogent.cogentappointment.persistence.model.Profile;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 7/2/19
 */
public interface ProfileService {

    void save(ProfileRequestDTO requestDTO);

    void update(ProfileUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<ProfileMinimalResponseDTO> search(ProfileSearchRequestDTO searchRequestDTO, Pageable pageable);

    ProfileDetailResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveMinProfile();

    Profile findActiveProfileByIdAndHospitalId(Long id, Long hospitalId);

    List<DropDownResponseDTO> fetchProfileByDepartmentId(Long departmentId);

    AssignedProfileResponseDTO fetchAssignedProfile(ProfileMenuSearchRequestDTO profileMenuSearchRequestDTO);
}

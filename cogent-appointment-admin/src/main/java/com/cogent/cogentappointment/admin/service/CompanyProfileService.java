package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileMenuSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.AssignedProfileResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.ProfileDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.ProfileMinimalResponseDTO;
import com.cogent.cogentappointment.persistence.model.Profile;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 7/2/19
 */
public interface CompanyProfileService {

    void save(CompanyProfileRequestDTO requestDTO);

    void update(CompanyProfileUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<ProfileMinimalResponseDTO> search(ProfileSearchRequestDTO searchRequestDTO, Pageable pageable);

    ProfileDetailResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveProfilesForDropdown();

    Profile fetchActiveProfileById(Long id);
}

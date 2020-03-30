package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.companyProfile.CompanyProfileDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyProfile.CompanyProfileMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.ProfileDetailResponseDTO;
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

    List<CompanyProfileMinimalResponseDTO> search(CompanyProfileSearchRequestDTO searchRequestDTO,
                                                  Pageable pageable);

    CompanyProfileDetailResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchMinActiveCompanyProfile();

    List<DropDownResponseDTO> fetchMinActiveCompanyProfileByCompanyId(Long companyId);

}

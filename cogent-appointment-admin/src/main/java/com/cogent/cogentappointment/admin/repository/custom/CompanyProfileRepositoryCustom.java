package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.companyProfile.CompanyProfileDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyProfile.CompanyProfileMinimalResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 7/10/19
 */
@Repository
@Qualifier("companyRepositoryCustom")
public interface CompanyProfileRepositoryCustom {

    Long validateDuplicity(String name, Long companyId);

    Long validateDuplicityForUpdate(Long profileId, String name, Long companyId);

    List<CompanyProfileMinimalResponseDTO> search(CompanyProfileSearchRequestDTO searchRequestDTO,
                                                  Pageable pageable);

    CompanyProfileDetailResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchMinActiveCompanyProfile();

    List<DropDownResponseDTO> fetchMinActiveCompanyProfileByCompanyId(Long companyId);
}

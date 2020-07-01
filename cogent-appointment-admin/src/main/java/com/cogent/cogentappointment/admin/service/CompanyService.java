package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanyRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanyUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyDropdownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyResponseDTO;
import com.cogent.cogentappointment.persistence.model.Hospital;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface CompanyService {

    void save(CompanyRequestDTO requestDTO) throws NoSuchAlgorithmException;

    void update(CompanyUpdateRequestDTO updateRequestDTO)
            throws NoSuchAlgorithmException;

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<CompanyMinimalResponseDTO> search(CompanySearchRequestDTO hospitalSearchRequestDTO,
                                           Pageable pageable);

    List<CompanyDropdownResponseDTO> fetchCompanyForDropDown();

    CompanyResponseDTO fetchDetailsById(Long companyId);

    String fetchAliasById(Long companyId);

    Hospital findActiveCompanyById(Long companyById);

}

package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminInfoRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminChangePasswordRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminPasswordRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminResetPasswordRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminLoggedInInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminMinimalResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author smriti on 2019-08-05
 */
public interface CompanyAdminService {

    void save(CompanyAdminRequestDTO adminRequestDTO, MultipartFile files, HttpServletRequest httpServletRequest);

    List<DropDownResponseDTO> fetchActiveCompanyAdminsForDropdown();

    List<CompanyAdminMinimalResponseDTO> search(CompanyAdminSearchRequestDTO searchRequestDTO, Pageable pageable);

    CompanyAdminDetailResponseDTO fetchCompanyAdminDetailsById(Long id);

    void delete(DeleteRequestDTO deleteRequestDTO);

    void changePassword(AdminChangePasswordRequestDTO requestDTO);

    void resetPassword(AdminResetPasswordRequestDTO resetPasswordRequestDTO);

    void updateAvatar(MultipartFile files, Long adminId);

    void update(CompanyAdminUpdateRequestDTO updateRequestDTO, MultipartFile files);

    void verifyConfirmationToken(String token);

    void savePassword(AdminPasswordRequestDTO requestDTO);

    CompanyAdminLoggedInInfoResponseDTO fetchLoggedInCompanyAdminInfo(CompanyAdminInfoRequestDTO requestDTO);

    List<CompanyAdminMetaInfoResponseDTO> fetchCompanyAdminMetaInfoResponseDto();
}





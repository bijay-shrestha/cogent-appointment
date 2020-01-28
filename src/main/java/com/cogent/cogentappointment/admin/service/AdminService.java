package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.admin.*;
import com.cogent.cogentappointment.admin.dto.response.admin.*;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminLoggedInInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminSubDepartmentResponseDTO;
import com.cogent.cogentappointment.dto.request.admin.*;
import com.cogent.cogentappointment.dto.response.admin.*;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminMinimalResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author smriti on 2019-08-05
 */
public interface AdminService {

    void save(AdminRequestDTO adminRequestDTO, MultipartFile files, HttpServletRequest httpServletRequest);

    List<AdminDropdownDTO> fetchActiveAdminsForDropdown();

    List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable);

    AdminDetailResponseDTO fetchDetailsById(Long id);

    void delete(DeleteRequestDTO deleteRequestDTO);

    void changePassword(AdminChangePasswordRequestDTO requestDTO);

    void resetPassword(AdminResetPasswordRequestDTO resetPasswordRequestDTO);

    void updateAvatar(MultipartFile files, Long adminId);

    void update(AdminUpdateRequestDTO updateRequestDTO, MultipartFile files);

    void verifyConfirmationToken(String token);

    void savePassword(AdminPasswordRequestDTO requestDTO);

    AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO);

    AdminInfoByUsernameResponseDTO fetchAdminInfoByUsername(String username);

    List<AdminSubDepartmentResponseDTO> fetchLoggedInAdminSubDepartmentList(String username);

    List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoResponseDto();
}





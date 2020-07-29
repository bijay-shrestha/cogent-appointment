package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.*;
import com.cogent.cogentappointment.admin.dto.response.admin.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 2019-08-05
 */
public interface AdminService {

    void save(AdminRequestDTO adminRequestDTO);

    List<AdminDropdownDTO> fetchActiveAdminsForDropdown();

    List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable);

    AdminDetailResponseDTO fetchDetailsById(Long id);

    void delete(DeleteRequestDTO deleteRequestDTO);

    void resetPassword(AdminResetPasswordRequestDTO resetPasswordRequestDTO);

    AdminAvatarUpdateResponse updateAvatar(AdminAvatarUpdateRequestDTO requestDTO);

    void update(AdminUpdateRequestDTO updateRequestDTO);

    List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoResponseDto();

    List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoByCompanyIdResponseDto(Long id);

    List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoByClientIdResponseDto(Long id);
}





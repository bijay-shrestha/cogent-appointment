package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.admin.*;
import com.cogent.cogentappointment.client.dto.response.admin.*;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * @author smriti on 2019-08-05
 */
public interface AdminService {

    void save(AdminRequestDTO adminRequestDTO);

    List<DropDownResponseDTO> fetchActiveMinAdmin();

    List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable);

    AdminDetailResponseDTO fetchDetailsById(Long id);

    void delete(DeleteRequestDTO deleteRequestDTO);

    void changePassword(AdminChangePasswordRequestDTO requestDTO);

    void resetPassword(AdminResetPasswordRequestDTO resetPasswordRequestDTO);

    AdminAvatarUpdateResponse updateAvatar(AdminAvatarUpdateRequestDTO requestDTO);

    void update(AdminUpdateRequestDTO updateRequestDTO);

    void verifyConfirmationToken(String token);

    void verifyConfirmationTokenForEmail(String token);

    void savePassword(AdminPasswordRequestDTO requestDTO);

    AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO);

    List<AdminMetaInfoResponseDTO> fetchAdminMetaInfo();
}





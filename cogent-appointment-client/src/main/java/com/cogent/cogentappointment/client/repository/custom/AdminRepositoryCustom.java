package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.admin.AdminInfoRequestDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminDropdownDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminLoggedInInfoResponseDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminMinimalResponseDTO;
import com.cogent.cogentappointment.client.model.Admin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 7/21/19
 */
@Repository
@Qualifier("adminRepositoryCustom")
public interface AdminRepositoryCustom {

    List<Object[]> validateDuplicity(String username, String email, String mobileNumber,
                                     Long hospitalId);

    List<AdminDropdownDTO> fetchActiveAdminsForDropDown();

    List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable);

    AdminDetailResponseDTO fetchDetailsById(Long id);

    List<Object[]> validateDuplicity(AdminUpdateRequestDTO updateRequestDTO);

    Admin fetchAdminByUsernameOrEmail(String username);

    AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO);

    AdminMinDetails getAdminInfoByUsernameAndHospitalCodeAndApikey(String username, String hospitalCode, String apiKey);

    AdminMinDetails verifyLoggedInAdmin(String username, String hospitalCode);

}


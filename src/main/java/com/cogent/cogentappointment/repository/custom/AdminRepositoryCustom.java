package com.cogent.cogentappointment.repository.custom;

import com.cogent.cogentappointment.dto.request.admin.AdminInfoRequestDTO;
import com.cogent.cogentappointment.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.admin.AdminUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.admin.*;
import com.cogent.cogentappointment.model.Admin;
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

    List<Object[]> fetchAdminForValidation(String username, String email, String mobileNumber);

    List<AdminDropdownDTO> fetchActiveAdminsForDropDown();

    List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable);

    AdminDetailResponseDTO fetchDetailsById(Long id);

    List<Object[]> fetchAdmin(AdminUpdateRequestDTO updateRequestDTO);

    Admin fetchAdminByUsernameOrEmail(String username);

    AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO);

    AdminInfoByUsernameResponseDTO fetchAdminInfoByUsername(String username);

    List<AdminSubDepartmentResponseDTO> fetchLoggedInAdminSubDepartmentList(String username);
}


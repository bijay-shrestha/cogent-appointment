package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.admin.AdminInfoRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.*;
import com.cogent.cogentappointment.admin.model.Admin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    AdminInfoByUsernameResponseDTO fetchAdminInfoByUsername(String username);

    List<AdminSubDepartmentResponseDTO> fetchLoggedInAdminSubDepartmentList(String username);
}


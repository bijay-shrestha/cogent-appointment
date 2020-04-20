package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminInfoRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminInfoRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.*;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminLoggedInInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.DashboardFeatureResponseDTO;
import com.cogent.cogentappointment.persistence.model.Admin;
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

    Object[] validateAdminCount(Long hospitalId);

    List<Object[]> validateDuplicity(String email, String mobileNumber,
                                     Long hospitalId);

    List<Object[]> validateDuplicityForCompanyAdmin(String email, String mobileNumber);

    List<AdminDropdownDTO> fetchActiveAdminsForDropDown();

    List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable);

    AdminDetailResponseDTO fetchDetailsById(Long id);

    List<Object[]> validateDuplicity(AdminUpdateRequestDTO updateRequestDTO);

    List<Object[]> validateCompanyAdminDuplicity(CompanyAdminUpdateRequestDTO updateRequestDTO);

    Admin fetchAdminByEmail(String email);

    LoggedInAdminDTO getLoggedInAdmin(String email);

    List<DashboardFeatureResponseDTO> fetchDashboardFeaturesByAdmin(Long adminId);

    List<DashboardFeatureResponseDTO> fetchOverAllDashboardFeature();

    List<DropDownResponseDTO> fetchActiveCompanyAdminsForDropDown();

    List<CompanyAdminMinimalResponseDTO> searchCompanyAdmin(CompanyAdminSearchRequestDTO searchRequestDTO,
                                                            Pageable pageable);

    CompanyAdminDetailResponseDTO fetchCompanyAdminDetailsById(Long id);

    CompanyAdminLoggedInInfoResponseDTO fetchLoggedInCompanyAdminInfo(CompanyAdminInfoRequestDTO requestDTO);
}


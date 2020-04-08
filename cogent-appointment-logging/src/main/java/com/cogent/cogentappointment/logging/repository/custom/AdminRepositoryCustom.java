package com.cogent.cogentappointment.logging.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 7/21/19
 */
@Repository
@Qualifier("adminRepositoryCustom")
public interface AdminRepositoryCustom {

    Object[] validateAdminCount(Long hospitalId);

//    List<Object[]> validateDuplicity(String username, String email, String mobileNumber, Long hospitalId);
//
//    List<DropDownResponseDTO> fetchActiveMinAdmin(Long hospitalId);
//
//    List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Long hospitalId, Pageable pageable);
//
//    AdminDetailResponseDTO fetchDetailsById(Long id, Long hospitalId);
//
//    List<Object[]> validateDuplicity(AdminUpdateRequestDTO updateRequestDTO, Long hospitalId);
//
//    Admin fetchAdminByUsernameOrEmail(String username, String hospitalCode);
//
//    AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO, Long hospitalId);
//
//    List<DashboardFeatureResponseDTO> fetchDashboardFeaturesByAdmin(Long adminId);
//
//    List<DashboardFeatureResponseDTO> fetchOverAllDashboardFeature();
}


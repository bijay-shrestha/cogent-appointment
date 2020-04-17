package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminInfoRequestDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminLoggedInInfoResponseDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.DashboardFeatureResponseDTO;
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

    List<Object[]> validateDuplicity(String email, String mobileNumber, Long hospitalId);

    List<DropDownResponseDTO> fetchActiveMinAdmin(Long hospitalId);

    List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Long hospitalId, Pageable pageable);

    AdminDetailResponseDTO fetchDetailsById(Long id, Long hospitalId);

    List<Object[]> validateDuplicity(AdminUpdateRequestDTO updateRequestDTO, Long hospitalId);

    Admin fetchAdminByEmail(String email, String hospitalCode);

    AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO, Long hospitalId);

    List<DashboardFeatureResponseDTO> fetchDashboardFeaturesByAdmin(Long adminId);

    List<DashboardFeatureResponseDTO> fetchOverAllDashboardFeature();
}


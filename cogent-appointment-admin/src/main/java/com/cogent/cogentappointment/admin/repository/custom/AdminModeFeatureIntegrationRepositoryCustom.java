package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminModeApiIntegrationResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminModeIntegrationSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rupak ON 2020/06/03-1:58 PM
 */
@Repository
@Qualifier("adminModeFeatureIntegrationRepositoryCustom")
public interface AdminModeFeatureIntegrationRepositoryCustom {

    AdminModeIntegrationSearchDTO search(AdminModeApiIntegrationSearchRequestDTO searchRequestDTO,
                                         Pageable pageable);

    List<AdminFeatureIntegrationResponse> fetchAdminModeIntegrationResponseDTO();

    AdminModeApiIntegrationResponseDTO findAdminModeFeatureIntegration(Long id);

    Long findAppointmentModeWiseFeatureAndRequestMethod(Long appointmentModeId,
                                                        Long featureTypeId,
                                                        Long requestMethodId);
}

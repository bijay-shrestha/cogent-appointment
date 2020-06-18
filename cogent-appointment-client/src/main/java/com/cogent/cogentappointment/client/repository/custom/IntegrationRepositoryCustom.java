package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.FeatureIntegrationResponse;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author rupak on 2020-05-20
 */
@Repository
@Qualifier("integrationRepositoryCustom")
public interface IntegrationRepositoryCustom {

    List<FeatureIntegrationResponse> fetchClientIntegrationResponseDTO(Long hospitalId);

    Map<String,String> findApiRequestHeaders(Long apiIntegrationFormatId);

    Map<String,String> findApiQueryParameters(Long apiIntegrationFormatId);

    Map<String, String> findAdminModeApiRequestHeaders(Long apiIntegrationFormatId);

    Map<String, String> findAdminModeApiQueryParameters(Long apiIntegrationFormatId);

    FeatureIntegrationResponse fetchClientIntegrationResponseDTOforBackendIntegration(IntegrationBackendRequestDTO integrationBackendRequestDTO);

    AdminFeatureIntegrationResponse fetchAppointmentModeIntegrationResponseDTOforBackendIntegration(IntegrationRefundRequestDTO refundRequestDTO,
                                                                                                    Long appointmentModeId);
}

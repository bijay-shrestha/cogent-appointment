package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.ApiQueryParametersResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiQueryParametersUpdateResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
@Qualifier("adminModeQueryParametersRepositoryCustom")
public interface AdminModeQueryParametersRepositoryCustom {

    List<ApiQueryParametersResponseDTO> findAdminModeApiQueryParameters(Long featureId);

    List<ApiQueryParametersUpdateResponseDTO> findAdminModeApiQueryParameterForUpdate(Long featureId);
}

package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ApiIntegrationFormatRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiQueryParametersRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.ApiInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.*;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.repository.custom.AdminModeFeatureIntegrationRepository;
import com.cogent.cogentappointment.admin.service.AdminModeFeatureIntegrationService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.IntegrationApiConstants.BACK_END_CODE;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.ADMIN_MODE_FEATURE_INTEGRATION;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.API_INTEGRATION;
import static com.cogent.cogentappointment.admin.utils.IntegrationAdminModeFeatureUtils.*;
import static com.cogent.cogentappointment.admin.utils.IntegrationUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak on 2020-05-21
 */
@Service
@Transactional
@Slf4j
public class AdminModeFeatureIntegrationImpl implements AdminModeFeatureIntegrationService {

    private final AdminModeFeatureIntegrationRepository adminModeFeatureIntegrationRepository;
    private final AppointmentModeRepository appointmentModeRepository;
    private final ApiQueryParametersRepository apiQueryParametersRepository;
    private final ApiRequestHeaderRepository apiRequestHeaderRepository;
    private final AdminModeQueryParametersRepository adminModeQueryParametersRepository;
    private final AdminModeRequestHeaderRepository adminModeRequestHeaderRepository;
    private final ApiFeatureIntegrationRepository apiFeatureIntegrationRepository;
    private final AdminModeApiFeatureIntegrationRepository featureIntegrationRepository;
    private final ApiIntegrationFormatRespository apiIntegrationFormatRespository;
    private final FeatureRepository featureRepository;
    private final HttpRequestMethodRepository httpRequestMethodRepository;
    private final IntegrationChannelRepository integrationChannelRepository;

    public AdminModeFeatureIntegrationImpl(
            AdminModeFeatureIntegrationRepository adminModeFeatureIntegrationRepository,
            AppointmentModeRepository appointmentModeRepository,
            AdminModeRequestHeaderRepository adminModeRequestHeaderRepository,
            HttpRequestMethodRepository httpRequestMethodRepository,
            ApiQueryParametersRepository apiQueryParametersRepository,
            ApiRequestHeaderRepository apiRequestHeaderRepository,
            AdminModeQueryParametersRepository adminModeQueryParametersRepository,
            ApiFeatureIntegrationRepository apiFeatureIntegrationRepository,
            AdminModeApiFeatureIntegrationRepository featureIntegrationRepository,
            ApiIntegrationFormatRespository apiIntegrationFormatRespository,
            FeatureRepository featureRepository,
            IntegrationChannelRepository integrationChannelRepository) {
        this.adminModeFeatureIntegrationRepository = adminModeFeatureIntegrationRepository;
        this.appointmentModeRepository = appointmentModeRepository;
        this.adminModeRequestHeaderRepository = adminModeRequestHeaderRepository;
        this.httpRequestMethodRepository = httpRequestMethodRepository;
        this.apiQueryParametersRepository = apiQueryParametersRepository;
        this.apiRequestHeaderRepository = apiRequestHeaderRepository;
        this.adminModeQueryParametersRepository = adminModeQueryParametersRepository;
        this.apiFeatureIntegrationRepository = apiFeatureIntegrationRepository;
        this.featureIntegrationRepository = featureIntegrationRepository;
        this.apiIntegrationFormatRespository = apiIntegrationFormatRespository;
        this.featureRepository = featureRepository;
        this.integrationChannelRepository = integrationChannelRepository;
    }

    @Override
    public void save(AdminModeApiIntegrationRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, ADMIN_MODE_FEATURE_INTEGRATION);

        validateFeatureAndHttpRequestMethod(requestDTO.getFeatureTypeId(),
                requestDTO.getRequestMethodId());

        AppointmentMode appointmentMode = findAppointmentMode(requestDTO.getAppointmentModeId());

        IntegrationChannel integrationChannel = integrationChannelRepository.
                findActiveIntegrationChannel(requestDTO.getIntegrationChannelId())
                .orElseThrow(() -> INTEGRATION_CHANNEL_NOT_FOUND.apply(requestDTO.getIntegrationChannelId()));

        AdminModeFeatureIntegration adminModeFeatureIntegration =
                parseToAdminModeFeatureIntegration(appointmentMode,
                        requestDTO.getFeatureTypeId(),
                        integrationChannel);

        saveAdminModeFeatureIntegration(adminModeFeatureIntegration);

        ApiIntegrationFormatRequestDTO apiIntegrationFormatRequestDTO = ApiIntegrationFormatRequestDTO.builder()
                .apiUrl(requestDTO.getApiUrl())
                .requestMethodId(requestDTO.getRequestMethodId())
                .build();


        ApiIntegrationFormat apiIntegrationFormat =
                parseToClientApiIntegrationFormat(apiIntegrationFormatRequestDTO);

        saveApiIntegrationFormat(apiIntegrationFormat);

        AdminModeApiFeatureIntegration adminModeApiFeatureIntegration =
                parseToAdminModeApiFeatureIntegration(
                        adminModeFeatureIntegration,
                        apiIntegrationFormat);

        saveAdminModeApiFeatureIntegration(adminModeApiFeatureIntegration);

        saveApiFeatureIntegration(adminModeFeatureIntegration.getId(), adminModeApiFeatureIntegration.getId());

        saveAdminModeQueryParameters(requestDTO.getParametersRequestDTOS(), adminModeApiFeatureIntegration.getId());

        saveAdminModeRequestHeaders(requestDTO.getClientApiRequestHeaders(), adminModeApiFeatureIntegration.getId());

        log.info(SAVING_PROCESS_COMPLETED, ADMIN_MODE_FEATURE_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

    }

    @Override
    public AdminModeIntegrationSearchDTO search(AdminModeApiIntegrationSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, ADMIN_MODE_FEATURE_INTEGRATION);

        AdminModeIntegrationSearchDTO clientApiIntegration =
                adminModeFeatureIntegrationRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, ADMIN_MODE_FEATURE_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

        return clientApiIntegration;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, ADMIN_MODE_FEATURE_INTEGRATION);

        AdminModeFeatureIntegration adminModeFeatureIntegration = adminModeFeatureIntegrationRepository
                .findAdminModeFeatureIntegrationById(deleteRequestDTO.getId())
                .orElseThrow(() -> ADMIN_MODE_FEATURE_INTEGRATION_NOT_FOUND.apply(deleteRequestDTO.getId()));

        parseToDeletedAdminModeFeatureIntegration(adminModeFeatureIntegration, deleteRequestDTO);

        List<AdminModeApiFeatureIntegration> adminModeApiFeatureIntegrationList = featureIntegrationRepository
                .findAdminModeApiFeatureIntegrationbyAdminModeFeatureId(adminModeFeatureIntegration.getId())
                .orElseThrow(() -> ADMIN_MODE_FEATURE_INTEGRATION_NOT_FOUND.apply(adminModeFeatureIntegration.getId()));


        List<ApiRequestHeader> apiRequestHeaderListToDelete = new ArrayList<>();
        List<ApiQueryParameters> apiQueryParameterToDelete = new ArrayList<>();

        adminModeApiFeatureIntegrationList.forEach(adminModeApiFeatureIntegration -> {
            List<ApiRequestHeader> apiRequestHeaderList = apiRequestHeaderRepository.
                    findApiRequestHeaderByApiFeatureIntegrationId(adminModeApiFeatureIntegration.getId())
                    .orElseThrow(() -> API_REQUEST_HEADER_NOT_FOUND.apply(adminModeApiFeatureIntegration.getId()));

            apiRequestHeaderListToDelete.addAll(apiRequestHeaderList);

            List<ApiQueryParameters> apiQueryParametersList = apiQueryParametersRepository.
                    findApiRequestHeaderByApiFeatureIntegrationId(adminModeApiFeatureIntegration.getId())
                    .orElseThrow(() -> API_QUERY_PARAMETER_NOT_FOUND.apply(adminModeApiFeatureIntegration.getId()));

            apiQueryParameterToDelete.addAll(apiQueryParametersList);


        });

        parseToDeletedApiRequestHeaders(apiRequestHeaderListToDelete);

        parseToDeletedApiQueryParameters(apiQueryParameterToDelete);

        parseToDeletedAdminModeApiFeatureIntegration(adminModeApiFeatureIntegrationList);

        log.info(DELETING_PROCESS_COMPLETED, ADMIN_MODE_FEATURE_INTEGRATION, getDifferenceBetweenTwoTime(startTime));


    }

    @Override
    public AdminModeIntegrationDetailResponseDTO fetchClientApiIntegrationById(Long id)
    {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, API_INTEGRATION);

        AdminModeIntegrationDetailResponseDTO clientApiIntegration =
                getAdminModeApiIntegration(id);

        log.info(SEARCHING_PROCESS_COMPLETED, API_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

        return clientApiIntegration;
    }

    private AdminModeIntegrationDetailResponseDTO getAdminModeApiIntegration(Long id) {

        AdminModeApiIntegrationResponseDTO featureIntegrationResponse = adminModeFeatureIntegrationRepository.
                findAdminModeFeatureIntegration(id);

        Map<String, String> requestHeaderResponseDTO = adminModeRequestHeaderRepository.
                findAdminModeApiRequestHeaders(featureIntegrationResponse.getFeatureId());

        Map<String, String> queryParametersResponseDTO = adminModeQueryParametersRepository.
                findAdminModeApiQueryParameters(featureIntegrationResponse.getFeatureId());

        AdminModeIntegrationDetailResponseDTO responseDTO = new AdminModeIntegrationDetailResponseDTO();
        responseDTO.setFeatureId(featureIntegrationResponse.getFeatureId());
        responseDTO.setFeatureCode(featureIntegrationResponse.getFeatureCode());
        responseDTO.setRequestMethodName(featureIntegrationResponse.getRequestMethodName());
        responseDTO.setRequestMethodId(featureIntegrationResponse.getRequestMethodId());
        responseDTO.setIntegrationChannel(featureIntegrationResponse.getIntegrationChannel());
        responseDTO.setIntegrationType(featureIntegrationResponse.getIntegrationType());
        responseDTO.setAppointmentMode(featureIntegrationResponse.getAppointmentName());
        responseDTO.setUrl(featureIntegrationResponse.getUrl());
        responseDTO.setHeaders(requestHeaderResponseDTO);
        responseDTO.setQueryParameters(queryParametersResponseDTO);

        //autitable data
        responseDTO.setCreatedBy(featureIntegrationResponse.getCreatedBy());
        responseDTO.setCreatedDate(featureIntegrationResponse.getCreatedDate());
        responseDTO.setLastModifiedBy(featureIntegrationResponse.getLastModifiedBy());
        responseDTO.setLastModifiedDate(featureIntegrationResponse.getLastModifiedDate());

        return responseDTO;

    }



    private void saveApiIntegrationFormat(ApiIntegrationFormat apiIntegrationFormat) {
        apiIntegrationFormatRespository.save(apiIntegrationFormat);
    }

    private void saveAdminModeApiFeatureIntegration(AdminModeApiFeatureIntegration apiFeatureIntegration) {
        featureIntegrationRepository.save(apiFeatureIntegration);
    }


    private void saveAdminModeFeatureIntegration(AdminModeFeatureIntegration adminModeFeatureIntegration) {
        adminModeFeatureIntegrationRepository.save(adminModeFeatureIntegration);
    }

    private AppointmentMode findAppointmentMode(Long appointmentModeId) {

        return appointmentModeRepository.fetchAppointmentModeById(appointmentModeId)
                .orElseThrow(() -> APPOINTMENT_MODE_NOT_FOUND.apply(appointmentModeId));
    }

    private void validateFeatureAndHttpRequestMethod(Long featureTypeId, Long requestMethodId) {

        featureRepository.findActiveFeatureById(featureTypeId)
                .orElseThrow(() -> FEATURE_NOT_FOUND.apply(featureTypeId));

        httpRequestMethodRepository.httpRequestMethodById(requestMethodId)
                .orElseThrow(() -> HTTP_REQUEST_METHOD_WITH_GIVEN_ID_NOT_FOUND.apply(requestMethodId));


    }

    private void saveAdminModeQueryParameters(List<ClientApiQueryParametersRequestDTO> queryParametersRequestDTOS,
                                              Long id) {


        adminModeQueryParametersRepository.saveAll(parseToAdminModeQueryParameters(queryParametersRequestDTOS,
                id));
    }

    private void saveAdminModeRequestHeaders(List<ClientApiHeadersRequestDTO> parametersRequestDTOS,
                                             Long id) {

        adminModeRequestHeaderRepository.saveAll(parseToAdminModeRequestHeaders(parametersRequestDTOS, id));


    }

    private void saveApiFeatureIntegration(Long clientFeatureIntegrationId,
                                           Long apiIntegrationFormatId) {

        apiFeatureIntegrationRepository.save(parseToClientApiFeatureIntegration(clientFeatureIntegrationId,
                apiIntegrationFormatId));

    }

    private void saveApiIntegrationFormat(AdminModeApiFeatureIntegration adminModeApiFeatureIntegration) {

        featureIntegrationRepository.save(adminModeApiFeatureIntegration);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_MODE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(AppointmentMode.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> API_QUERY_PARAMETER_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(ApiQueryParameters.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> API_REQUEST_HEADER_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(ApiRequestHeader.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> HTTP_REQUEST_METHOD_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(HttpRequestMethod.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> FEATURE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Feature.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> ADMIN_MODE_FEATURE_INTEGRATION_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(AdminModeFeatureIntegration.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> INTEGRATION_CHANNEL_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(IntegrationChannel.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> INTEGRATION_TYPE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(ApiIntegrationType.class, "id", id.toString());
    };

}

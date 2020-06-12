package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.integrationAdminModeUpdate.AdminModeIntegrationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ApiIntegrationFormatRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiQueryParametersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.clientIntegrationUpdate.ClientApiQueryParamtersUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.clientIntegrationUpdate.ClientApiRequestHeadersUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.ApiQueryParametersDetailResponse;
import com.cogent.cogentappointment.admin.dto.response.integration.ApiRequestHeaderDetailResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.*;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiQueryParametersUpdateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiRequestHeaderUpdateResponseDTO;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
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

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.ADMIN_MODE_FEATURE_INTEGRATION;
import static com.cogent.cogentappointment.admin.utils.IntegrationAdminModeFeatureUtils.*;
import static com.cogent.cogentappointment.admin.utils.IntegrationAdminModeFeatureUtils.parseToDeletedApiQueryParameters;
import static com.cogent.cogentappointment.admin.utils.IntegrationAdminModeFeatureUtils.parseToDeletedApiRequestHeaders;
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
    private final AdminModeApiFeatureIntegrationRepository adminModeApiFeatureIntegrationRepository;
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
            AdminModeApiFeatureIntegrationRepository adminModeApiFeatureIntegrationRepository,
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
        this.adminModeApiFeatureIntegrationRepository = adminModeApiFeatureIntegrationRepository;
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

        checkAdminModeFeatureIntegrationDuplicity(requestDTO.getAppointmentModeId(),
                requestDTO.getFeatureTypeId(),
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

        saveAdminModeQueryParameters(requestDTO.getParametersRequestDTOS(), apiIntegrationFormat.getId());

        saveAdminModeRequestHeaders(requestDTO.getClientApiRequestHeaders(), apiIntegrationFormat.getId());

        log.info(SAVING_PROCESS_COMPLETED, ADMIN_MODE_FEATURE_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

    }

    private void checkAdminModeFeatureIntegrationDuplicity(Long appointmentModeId, Long featureTypeId, Long requestMethodId) {


        Long count = adminModeFeatureIntegrationRepository.findAppointmentModeWiseFeatureAndRequestMethod(appointmentModeId,
                featureTypeId, requestMethodId);

        if (count > 0) {

            throw new DataDuplicationException("Admin Mode Feature Integration Already Exist");
        }


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


        List<AdminModeApiFeatureIntegration> adminModeApiFeatureIntegrationList = adminModeApiFeatureIntegrationRepository
                .findAdminModeApiFeatureIntegrationbyAdminModeFeatureId(adminModeFeatureIntegration.getId())
                .orElseThrow(() -> ADMIN_MODE_FEATURE_INTEGRATION_NOT_FOUND.apply(adminModeFeatureIntegration.getId()));


        List<AdminModeRequestHeader> apiRequestHeaderListToDelete = new ArrayList<>();
        List<AdminModeQueryParameters> apiQueryParameterToDelete = new ArrayList<>();

        adminModeApiFeatureIntegrationList.forEach(adminModeApiFeatureIntegration -> {

            ApiIntegrationFormat apiIntegrationFormat = apiIntegrationFormatRespository.
                    findByIntegrationFormatId(adminModeApiFeatureIntegration.getApiIntegrationFormatId().getId())
                    .orElseThrow(() -> API_INTEGRATION_FORMAT_NOT_FOUND.apply(adminModeApiFeatureIntegration.getId()));


            List<AdminModeRequestHeader> apiRequestHeaderList = adminModeRequestHeaderRepository.
                    findAdminModeApiRequestHeaderByApiFeatureIntegrationId(adminModeApiFeatureIntegration.getApiIntegrationFormatId().getId())
                    .orElse(null);

            if (apiRequestHeaderList != null) {
                apiRequestHeaderListToDelete.addAll(apiRequestHeaderList);
            }

            List<AdminModeQueryParameters> apiQueryParametersList = adminModeQueryParametersRepository.
                    findApiRequestHeaderByApiIntegrationFormatId(adminModeApiFeatureIntegration.getApiIntegrationFormatId().getId())
                    .orElse(null);

            if (apiQueryParametersList != null) {
                apiQueryParameterToDelete.addAll(apiQueryParametersList);
            }

            parseToDeletedApiIntegrationFormat(apiIntegrationFormat);


        });

        if (apiRequestHeaderListToDelete.size() > 0) {
            parseToDeletedApiRequestHeaders(apiRequestHeaderListToDelete);

        }

        if (apiQueryParameterToDelete.size() > 0) {
            parseToDeletedApiQueryParameters(apiQueryParameterToDelete);

        }

        parseToDeletedAdminModeApiFeatureIntegration(adminModeApiFeatureIntegrationList);

        log.info(DELETING_PROCESS_COMPLETED, ADMIN_MODE_FEATURE_INTEGRATION, getDifferenceBetweenTwoTime(startTime));


    }

    @Override
    public AdminModeIntegrationDetailResponseDTO fetchAdminModeIntegrationById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, ADMIN_MODE_FEATURE_INTEGRATION);

        AdminModeIntegrationDetailResponseDTO adminModeApiIntegration =
                getAdminModeApiIntegration(id);

        log.info(SEARCHING_PROCESS_COMPLETED, ADMIN_MODE_FEATURE_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

        return adminModeApiIntegration;
    }

    @Override
    public void update(AdminModeIntegrationUpdateRequestDTO requestDTO) {


        log.info(UPDATING_PROCESS_STARTED, ADMIN_MODE_FEATURE_INTEGRATION);

        Long startTime = getTimeInMillisecondsFromLocalDate();

        AdminModeFeatureIntegration adminModeFeatureIntegration =
                adminModeFeatureIntegrationRepository.
                        findAdminModeFeatureIntegrationById(requestDTO.getAdminModeIntegrationId())
                        .orElseThrow(() -> ADMIN_MODE_FEATURE_INTEGRATION_NOT_FOUND.apply(requestDTO.getAdminModeIntegrationId()));

        AppointmentMode appointmentMode = appointmentModeRepository.fetchAppointmentModeById(requestDTO.getAppointmentModeId())
                .orElseThrow(() -> APPOINTMENT_MODE_NOT_FOUND.apply(requestDTO.getAppointmentModeId()));


        IntegrationChannel integrationChannel = integrationChannelRepository.
                findActiveIntegrationChannel(requestDTO.getIntegrationChannelId())
                .orElseThrow(() -> INTEGRATION_CHANNEL_NOT_FOUND.apply(requestDTO.getIntegrationChannelId()));


        parseToUpdateAdminModeFeatureIntegration(appointmentMode,
                integrationChannel,
                requestDTO, adminModeFeatureIntegration);

        List<AdminModeApiFeatureIntegration> adminModeApiFeatureIntegrationList =
                adminModeApiFeatureIntegrationRepository.
                        findAdminModeApiFeatureIntegrationbyAdminModeFeatureId(adminModeFeatureIntegration.getId())
                        .orElseThrow(() -> ADMIN_MODE_API_FEATURE_INTEGRATION_NOT_FOUND.apply(adminModeFeatureIntegration.getId()));


        adminModeApiFeatureIntegrationList.forEach(adminModeApiFeatureIntegration -> {

            updateAdminModeApiIntegrationFormat(requestDTO,
                    adminModeApiFeatureIntegration.getApiIntegrationFormatId());

            updateAdminModeQueryParameters(requestDTO.getQueryParametersRequestDTOS(),
                    adminModeApiFeatureIntegration.getApiIntegrationFormatId());

            updateAdminModeRequestHeaders(requestDTO.getClientApiRequestHeaders(),
                    adminModeApiFeatureIntegration.getApiIntegrationFormatId());
        });

        log.info(UPDATING_PROCESS_COMPLETED, ADMIN_MODE_FEATURE_INTEGRATION, getDifferenceBetweenTwoTime(startTime));


    }

    @Override
    public AdminModeIntegrationUpdateResponseDTO fetchDetailsForUpdate(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN_MODE_FEATURE_INTEGRATION);

        AdminModeIntegrationUpdateResponseDTO updateResponseDTO =
                getAdminModeIntegrationDetailForUpdate(id);

        log.info(FETCHING_PROCESS_COMPLETED, ADMIN_MODE_FEATURE_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

        return updateResponseDTO;
    }

    private AdminModeIntegrationUpdateResponseDTO getAdminModeIntegrationDetailForUpdate(Long id) {

        AdminModeApiIntegrationResponseDTO featureIntegrationResponse = adminModeFeatureIntegrationRepository.
                findAdminModeFeatureIntegration(id);

        List<ApiRequestHeaderUpdateResponseDTO> requestHeaderResponseDTO = adminModeRequestHeaderRepository.
                findAdminModeApiRequestHeaderForUpdate(featureIntegrationResponse.getFeatureId());

        List<ApiQueryParametersUpdateResponseDTO> queryParametersResponseDTO = adminModeQueryParametersRepository.
                findAdminModeApiQueryParameterForUpdate(featureIntegrationResponse.getFeatureId());

        AdminModeIntegrationUpdateResponseDTO responseDTO = new AdminModeIntegrationUpdateResponseDTO();
        responseDTO.setFeatureName(featureIntegrationResponse.getFeatureName());
        responseDTO.setFeatureId(featureIntegrationResponse.getFeatureId());
        responseDTO.setRequestMethodName(featureIntegrationResponse.getRequestMethodName());
        responseDTO.setRequestMethodId(featureIntegrationResponse.getRequestMethodId());
        responseDTO.setIntegrationTypeId(featureIntegrationResponse.getIntegrationTypeId());
        responseDTO.setIntegrationType(featureIntegrationResponse.getIntegrationType());
        responseDTO.setIntegrationChannelId(featureIntegrationResponse.getIntegrationChannelId());
        responseDTO.setIntegrationChannel(featureIntegrationResponse.getIntegrationChannel());
        responseDTO.setUrl(featureIntegrationResponse.getUrl());
        responseDTO.setAppointmentMode(featureIntegrationResponse.getAppointmentModeName());
        responseDTO.setAppointmentModeId(featureIntegrationResponse.getAppointmentModeId());
        responseDTO.setHeaders(requestHeaderResponseDTO);
        responseDTO.setQueryParameters(queryParametersResponseDTO);

        return responseDTO;


    }


    private void updateAdminModeApiIntegrationFormat(AdminModeIntegrationUpdateRequestDTO requestDTO,
                                                     ApiIntegrationFormat apiIntegrationFormatId) {

        ApiIntegrationFormat apiIntegrationFormat = apiIntegrationFormatRespository.
                findByIntegrationFormatId(apiIntegrationFormatId.getId())
                .orElseThrow(() -> API_INTEGRATION_FORMAT_NOT_FOUND.apply(apiIntegrationFormatId.getId()));

        apiIntegrationFormat.setUrl(requestDTO.getApiUrl());
        apiIntegrationFormat.setHttpRequestMethodId(requestDTO.getRequestMethodId());

        apiIntegrationFormatRespository.save(apiIntegrationFormat);

    }

    private void updateAdminModeRequestHeaders(List<ClientApiRequestHeadersUpdateRequestDTO> clientApiRequestHeaders,
                                               ApiIntegrationFormat apiIntegrationFormatId) {

        clientApiRequestHeaders.forEach(requestDTO -> {

            AdminModeRequestHeader adminModeRequestHeader =
                    adminModeRequestHeaderRepository.findAdminModeRequestHeaderById(requestDTO.getId())
                            .orElse(null);

            if (adminModeRequestHeader == null) {

                AdminModeRequestHeader requestHeader = new AdminModeRequestHeader();
                requestHeader.setApiIntegrationFormatId(apiIntegrationFormatId.getId());
                requestHeader.setKeyName(requestDTO.getKeyParam());
                requestHeader.setValue(requestDTO.getValueParam());
                requestHeader.setDescription(requestDTO.getDescription());
                requestHeader.setStatus(StatusConstants.ACTIVE);


                adminModeRequestHeaderRepository.save(requestHeader);

            }

            if (adminModeRequestHeader != null) {
                adminModeRequestHeader.setKeyName(requestDTO.getKeyParam());
                adminModeRequestHeader.setValue(requestDTO.getValueParam());
                adminModeRequestHeader.setStatus(requestDTO.getStatus());
                adminModeRequestHeader.setDescription(requestDTO.getDescription());
                adminModeRequestHeader.setApiIntegrationFormatId(apiIntegrationFormatId.getId());

            }

        });

    }

    private void updateAdminModeQueryParameters(List<ClientApiQueryParamtersUpdateRequestDTO> queryParametersRequestDTOS,
                                                ApiIntegrationFormat apiIntegrationFormatId) {

        queryParametersRequestDTOS.forEach(requestDTO -> {

            AdminModeQueryParameters adminModeQueryParameters =
                    adminModeQueryParametersRepository.findAdminModeQueryParametersById(requestDTO.getId()).orElse(null);

            if (adminModeQueryParameters == null) {

                AdminModeQueryParameters queryParameters = new AdminModeQueryParameters();
                queryParameters.setApiIntegrationFormatId(apiIntegrationFormatId.getId());
                queryParameters.setParam(requestDTO.getKeyParam());
                queryParameters.setValue(requestDTO.getValueParam());
                queryParameters.setStatus(StatusConstants.ACTIVE);

                adminModeQueryParametersRepository.save(queryParameters);

            }

            if (adminModeQueryParameters != null) {
                adminModeQueryParameters.setParam(requestDTO.getKeyParam());
                adminModeQueryParameters.setValue(requestDTO.getValueParam());
                adminModeQueryParameters.setStatus(requestDTO.getStatus());
                adminModeQueryParameters.setApiIntegrationFormatId(apiIntegrationFormatId.getId());

            }
        });

    }


    private AdminModeIntegrationDetailResponseDTO getAdminModeApiIntegration(Long id) {

        AdminModeApiIntegrationResponseDTO featureIntegrationResponse = adminModeFeatureIntegrationRepository.
                findAdminModeFeatureIntegration(id);

        List<ApiRequestHeaderDetailResponse> requestHeaderResponseDTO = adminModeRequestHeaderRepository.
                findAdminModeApiRequestHeaders(featureIntegrationResponse.getApiIntegrationFormatId());

        List<ApiQueryParametersDetailResponse> queryParametersResponseDTO = adminModeQueryParametersRepository.
                findAdminModeApiQueryParameters(featureIntegrationResponse.getApiIntegrationFormatId());

        AdminModeIntegrationDetailResponseDTO responseDTO = new AdminModeIntegrationDetailResponseDTO();
        responseDTO.setFeatureId(featureIntegrationResponse.getFeatureId());
        responseDTO.setFeatureName(featureIntegrationResponse.getFeatureName());
        responseDTO.setRequestMethodName(featureIntegrationResponse.getRequestMethodName());
        responseDTO.setRequestMethodId(featureIntegrationResponse.getRequestMethodId());
        responseDTO.setIntegrationChannel(featureIntegrationResponse.getIntegrationChannel());
        responseDTO.setIntegrationType(featureIntegrationResponse.getIntegrationType());
        responseDTO.setAppointmentMode(featureIntegrationResponse.getAppointmentModeName());
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
        adminModeApiFeatureIntegrationRepository.save(apiFeatureIntegration);
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

        adminModeApiFeatureIntegrationRepository.save(adminModeApiFeatureIntegration);
    }


    private Function<Long, NoContentFoundException> ADMIN_MODE_FEATURE_INTEGRATION_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(AdminModeFeatureIntegration.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> ADMIN_MODE_API_FEATURE_INTEGRATION_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(AdminModeApiFeatureIntegration.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> APPOINTMENT_MODE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(AppointmentMode.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> API_INTEGRATION_FORMAT_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(ApiIntegrationFormat.class, "id", id.toString());
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

    private Function<Long, NoContentFoundException> INTEGRATION_CHANNEL_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(IntegrationChannel.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> INTEGRATION_TYPE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(ApiIntegrationType.class, "id", id.toString());
    };

}

package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ApiIntegrationFormatRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiQueryParametersRequestDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.AdminModeFeatureIntegrationService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.ADMIN_MODE_FEATURE_INTEGRATION;
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
    private final ApiFeatureIntegrationRepository apiFeatureIntegrationRepository;
    private final AdminModeApiFeatureIntegrationRepository featureIntegrationRepository;
    private final ApiIntegrationFormatRespository apiIntegrationFormatRespository;
    private final FeatureRepository featureRepository;
    private final HttpRequestMethodRepository httpRequestMethodRepository;
    private final IntegrationChannelRepository integrationChannelRepository;

    public AdminModeFeatureIntegrationImpl(AdminModeFeatureIntegrationRepository adminModeFeatureIntegrationRepository,
                                           AppointmentModeRepository appointmentModeRepository,
                                           HttpRequestMethodRepository httpRequestMethodRepository,
                                           ApiQueryParametersRepository apiQueryParametersRepository,
                                           ApiRequestHeaderRepository apiRequestHeaderRepository,
                                           ApiFeatureIntegrationRepository apiFeatureIntegrationRepository,
                                           AdminModeApiFeatureIntegrationRepository apiIntegrationFormatRepository,
                                           ApiIntegrationFormatRespository apiIntegrationFormatRespository,
                                           FeatureRepository featureRepository,
                                           IntegrationChannelRepository integrationChannelRepository) {
        this.adminModeFeatureIntegrationRepository = adminModeFeatureIntegrationRepository;
        this.appointmentModeRepository = appointmentModeRepository;
        this.httpRequestMethodRepository = httpRequestMethodRepository;
        this.apiQueryParametersRepository = apiQueryParametersRepository;
        this.apiRequestHeaderRepository = apiRequestHeaderRepository;
        this.apiFeatureIntegrationRepository = apiFeatureIntegrationRepository;
        this.featureIntegrationRepository = apiIntegrationFormatRepository;
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

        AdminModeFeatureIntegration adminModeFeatureIntegration = parseToAdminModeFeatureIntegration(appointmentMode,
                requestDTO.getFeatureTypeId(), integrationChannel);

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

        saveApiFeatureIntegration(adminModeFeatureIntegration.getId(), adminModeApiFeatureIntegration.getId(),
                null);

        saveApiQueryParameters(requestDTO.getParametersRequestDTOS(), adminModeApiFeatureIntegration.getId());

        saveApiRequestHeaders(requestDTO.getClientApiRequestHeaders(), adminModeApiFeatureIntegration.getId());

        log.info(SAVING_PROCESS_COMPLETED, ADMIN_MODE_FEATURE_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

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

    private void saveApiRequestHeaders(List<ClientApiHeadersRequestDTO> clientApiRequestHeaders,
                                       Long id) {

        apiRequestHeaderRepository.saveAll(parseToClientApiRequestHeaders(clientApiRequestHeaders, id));

    }

    private void saveApiQueryParameters(List<ClientApiQueryParametersRequestDTO> parametersRequestDTOS,
                                        Long id) {

        apiQueryParametersRepository.saveAll(parseToClientApiQueryParameters(parametersRequestDTOS,
                id));

    }

    private void saveApiFeatureIntegration(Long clientFeatureIntegrationId,
                                           Long apiIntegrationFormatId,
                                           Long integrationTypeId) {

        apiFeatureIntegrationRepository.save(parseToClientApiFeatureIntegration(clientFeatureIntegrationId,
                apiIntegrationFormatId, integrationTypeId));

    }

    private void saveApiIntegrationFormat(AdminModeApiFeatureIntegration adminModeApiFeatureIntegration) {

        featureIntegrationRepository.save(adminModeApiFeatureIntegration);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_MODE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(AppointmentMode.class, "id", id.toString());
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

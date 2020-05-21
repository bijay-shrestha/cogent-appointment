package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.adminModeIntegration.AdminModeFeatureIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ApiIntegrationFormatRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiQueryParametersRequestDTO;
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
    private final ClientApiIntegrationFormatRespository clientApiIntegrationFormatRespository;
    private final FeatureRepository featureRepository;
    private final HttpRequestMethodRepository httpRequestMethodRepository;

    public AdminModeFeatureIntegrationImpl(AdminModeFeatureIntegrationRepository adminModeFeatureIntegrationRepository,
                                           AppointmentModeRepository appointmentModeRepository,
                                           HttpRequestMethodRepository httpRequestMethodRepository,
                                           ApiQueryParametersRepository apiQueryParametersRepository,
                                           ApiRequestHeaderRepository apiRequestHeaderRepository,
                                           ApiFeatureIntegrationRepository apiFeatureIntegrationRepository,
                                           ClientApiIntegrationFormatRespository clientApiIntegrationFormatRespository, FeatureRepository featureRepository) {
        this.adminModeFeatureIntegrationRepository = adminModeFeatureIntegrationRepository;
        this.appointmentModeRepository = appointmentModeRepository;
        this.httpRequestMethodRepository = httpRequestMethodRepository;
        this.apiQueryParametersRepository = apiQueryParametersRepository;
        this.apiRequestHeaderRepository = apiRequestHeaderRepository;
        this.apiFeatureIntegrationRepository = apiFeatureIntegrationRepository;
        this.clientApiIntegrationFormatRespository = clientApiIntegrationFormatRespository;
        this.featureRepository = featureRepository;
    }

    @Override
    public void save(AdminModeFeatureIntegrationRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, ADMIN_MODE_FEATURE_INTEGRATION);

        validateFeatureAndHttpRequestMethod(requestDTO.getFeatureTypeId(),
                requestDTO.getRequestMethodId());

        AppointmentMode appointmentMode = findAppointmentMode(requestDTO.getAppointmentModeId());

        AdminModeFeatureIntegration adminModeFeatureIntegration = parseToAdminModeFeatureIntegration(appointmentMode,
                requestDTO.getFeatureTypeId());

        saveAdminModeFeatureIntegration(adminModeFeatureIntegration);

        ApiIntegrationFormatRequestDTO apiIntegrationFormatRequestDTO = ApiIntegrationFormatRequestDTO.builder()
                .apiUrl(requestDTO.getApiUrl())
                .requestMethodId(requestDTO.getRequestMethodId())
                .requestBodyAttrribute(requestDTO.getRequestBodyAttrribute())
                .build();

        ApiIntegrationFormat apiIntegrationFormat =
                parseToClientApiIntegrationFormat(apiIntegrationFormatRequestDTO);

        saveApiIntegrationFormat(apiIntegrationFormat);

        saveApiFeatureIntegration(adminModeFeatureIntegration.getId(), apiIntegrationFormat.getId());

        saveApiQueryParameters(requestDTO.getParametersRequestDTOS(), apiIntegrationFormat);

        saveApiRequestHeaders(requestDTO.getClientApiRequestHeaders(), apiIntegrationFormat);

        log.info(SAVING_PROCESS_COMPLETED, ADMIN_MODE_FEATURE_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

    }

    private void saveAdminModeFeatureIntegration(AdminModeFeatureIntegration adminModeFeatureIntegration) {
        adminModeFeatureIntegrationRepository.save(adminModeFeatureIntegration);
    }

    private AppointmentMode findAppointmentMode(Long appointmentModeId) {

        return appointmentModeRepository.fetchAppointmentModeById(appointmentModeId)
                .orElseThrow(() -> APPOINTMENT_MODE_NOT_FOUND.apply(appointmentModeId));
    }

    private void validateFeatureAndHttpRequestMethod(Long featureTypeId, Long requestMethodId) {

        featureRepository.findFeatureById(featureTypeId)
                .orElseThrow(() -> FEATURE_NOT_FOUND.apply(featureTypeId));

        httpRequestMethodRepository.httpRequestMethodById(requestMethodId)
                .orElseThrow(() -> HTTP_REQUEST_METHOD_WITH_GIVEN_ID_NOT_FOUND.apply(requestMethodId));


    }

    private void saveApiRequestHeaders(List<ClientApiHeadersRequestDTO> clientApiRequestHeaders,
                                       ApiIntegrationFormat apiIntegrationFormat) {

        apiRequestHeaderRepository.saveAll(parseToClientApiRequestHeaders(clientApiRequestHeaders, apiIntegrationFormat));

    }

    private void saveApiQueryParameters(List<ClientApiQueryParametersRequestDTO> parametersRequestDTOS,
                                        ApiIntegrationFormat apiIntegrationFormat) {

        apiQueryParametersRepository.saveAll(parseToClientApiQueryParameters(parametersRequestDTOS,
                apiIntegrationFormat.getId()));

    }

    private void saveApiFeatureIntegration(Long clientFeatureIntegrationId, Long apiIntegrationFormatId) {

        apiFeatureIntegrationRepository.save(parseToClientApiFeatureIntegration(clientFeatureIntegrationId,
                apiIntegrationFormatId));

    }

    private void saveApiIntegrationFormat(ApiIntegrationFormat apiIntegrationFormat) {

        clientApiIntegrationFormatRespository.save(apiIntegrationFormat);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_MODE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(AppointmentMode.class);
    };

    private Function<Long, NoContentFoundException> HTTP_REQUEST_METHOD_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(HttpRequestMethod.class);
    };

    private Function<Long, NoContentFoundException> FEATURE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Feature.class);
    };
}

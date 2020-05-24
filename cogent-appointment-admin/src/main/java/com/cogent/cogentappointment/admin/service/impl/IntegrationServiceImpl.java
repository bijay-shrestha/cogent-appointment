package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.clientIntegration.*;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.IntegrationService;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationFormat;
import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
import com.cogent.cogentappointment.persistence.model.Feature;
import com.cogent.cogentappointment.persistence.model.HttpRequestMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.API_INTEGRATIONS;
import static com.cogent.cogentappointment.admin.utils.IntegrationUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak on 2020-05-19
 */
@Service
@Transactional
@Slf4j
public class IntegrationServiceImpl implements IntegrationService {

    private final ClientFeatureIntegrationRepository clientFeatureIntegrationRepository;
    private final ClientApiIntegrationFormatRespository clientApiIntegrationFormatRespository;
    private final HttpRequestMethodRepository httpRequestMethodRepository;
    private final ApiQueryParametersRepository apiQueryParametersRepository;
    private final ApiRequestHeaderRepository apiRequestHeaderRepository;
    private final ApiFeatureIntegrationRepository apiFeatureIntegrationRepository;
    private final FeatureRepository featureRepository;

    public IntegrationServiceImpl(ClientFeatureIntegrationRepository clientFeatureIntegrationRepository, ClientApiIntegrationFormatRespository clientApiIntegrationFormatRespository, HttpRequestMethodRepository httpRequestMethodRepository, ApiQueryParametersRepository apiQueryParametersRepository, ApiRequestHeaderRepository apiRequestHeaderRepository, ApiFeatureIntegrationRepository apiFeatureIntegrationRepository, FeatureRepository featureRepository) {

        this.clientFeatureIntegrationRepository = clientFeatureIntegrationRepository;
        this.clientApiIntegrationFormatRespository = clientApiIntegrationFormatRespository;
        this.httpRequestMethodRepository = httpRequestMethodRepository;
        this.apiQueryParametersRepository = apiQueryParametersRepository;
        this.apiRequestHeaderRepository = apiRequestHeaderRepository;
        this.apiFeatureIntegrationRepository = apiFeatureIntegrationRepository;
        this.featureRepository = featureRepository;
    }

    @Override
    public void save(ClientApiIntegrationRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, API_INTEGRATIONS);

        validateFeatureAndHttpRequestMethod(requestDTO.getFeatureTypeId(),
                requestDTO.getRequestMethodId());

        ClientFeatureIntegration clientFeatureIntegration = parseToClientFeatureIntegration(requestDTO.getHospitalId(),
                requestDTO.getFeatureTypeId());

        saveClientFeatureIntegration(clientFeatureIntegration);

        ApiIntegrationFormatRequestDTO apiIntegrationFormatRequestDTO = ApiIntegrationFormatRequestDTO.builder()
                .apiUrl(requestDTO.getApiUrl())
                .requestMethodId(requestDTO.getRequestMethodId())
                .requestBodyAttrribute(requestDTO.getRequestBodyAttrribute())
                .build();

        ApiIntegrationFormat apiIntegrationFormat =
                parseToClientApiIntegrationFormat(apiIntegrationFormatRequestDTO);

        saveApiIntegrationFormat(apiIntegrationFormat);

        saveApiFeatureIntegration(clientFeatureIntegration.getId(), apiIntegrationFormat.getId());

        saveApiQueryParameters(requestDTO.getParametersRequestDTOS(), apiIntegrationFormat);

        saveApiRequestHeaders(requestDTO.getClientApiRequestHeaders(), apiIntegrationFormat);

        log.info(SAVING_PROCESS_COMPLETED, API_INTEGRATIONS, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(ClientApiIntegrationUpdateRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        validateFeatureAndHttpRequestMethod(requestDTO.getFeatureTypeId(),
                requestDTO.getRequestMethodId());



        log.info(UPDATING_PROCESS_STARTED, API_INTEGRATIONS);

        log.info(UPDATING_PROCESS_COMPLETED, API_INTEGRATIONS, getDifferenceBetweenTwoTime(startTime));

    }

    private void validateFeatureAndHttpRequestMethod(Long featureTypeId, Long requestMethodId) {

        featureRepository.findFeatureById(featureTypeId)
                .orElseThrow(() -> FEATURE_NOT_FOUND.apply(featureTypeId));

        httpRequestMethodRepository.httpRequestMethodById(requestMethodId)
                .orElseThrow(() -> HTTP_REQUEST_METHOD_WITH_GIVEN_ID_NOT_FOUND.apply(requestMethodId));


    }

    private void saveApiRequestHeaders(List<ClientApiHeadersRequestDTO> clientApiRequestHeaders,
                                       ApiIntegrationFormat apiIntegrationFormat) {

        apiRequestHeaderRepository.saveAll(parseToClientApiRequestHeaders(clientApiRequestHeaders, apiIntegrationFormat.getId()));

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

    private void saveClientFeatureIntegration(ClientFeatureIntegration clientFeatureIntegration) {

        clientFeatureIntegrationRepository.save(clientFeatureIntegration);
    }

    private Function<Long, NoContentFoundException> HTTP_REQUEST_METHOD_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(HttpRequestMethod.class);
    };

    private Function<Long, NoContentFoundException> FEATURE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Feature.class);
    };
}

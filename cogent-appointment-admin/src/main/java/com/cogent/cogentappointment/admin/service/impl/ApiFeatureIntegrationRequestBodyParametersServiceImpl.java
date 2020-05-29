package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.integration.ApiFeatureIntegrationRequestBodyRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationRequestBodyAttributeResponse;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.ApiFeatureIntegrationRequestBodyParametersRepository;
import com.cogent.cogentappointment.admin.repository.FeatureRepository;
import com.cogent.cogentappointment.admin.repository.IntegrationRequestBodyParametersRepository;
import com.cogent.cogentappointment.admin.service.ApiFeatureIntegrationRequestBodyParametersService;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationRequestBodyParameters;
import com.cogent.cogentappointment.persistence.model.Feature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SEARCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SEARCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.API_REQUEST_BODY_PARAMETERS;
import static com.cogent.cogentappointment.admin.utils.IntegrationUtils.parseToClientApiFeatureRequestBodyParameters;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak ON 2020/05/29-12:41 PM
 */
@Service
@Transactional
@Slf4j
public class ApiFeatureIntegrationRequestBodyParametersServiceImpl implements
        ApiFeatureIntegrationRequestBodyParametersService {

    private final ApiFeatureIntegrationRequestBodyParametersRepository
            integrationRequestBodyParametersRepository;
    private final IntegrationRequestBodyParametersRepository requestBodyParametersRepository;
    private final FeatureRepository featureRepository;

    public ApiFeatureIntegrationRequestBodyParametersServiceImpl(ApiFeatureIntegrationRequestBodyParametersRepository integrationRequestBodyParametersRepository, IntegrationRequestBodyParametersRepository requestBodyParametersRepository, FeatureRepository featureRepository) {
        this.integrationRequestBodyParametersRepository = integrationRequestBodyParametersRepository;

        this.requestBodyParametersRepository = requestBodyParametersRepository;
        this.featureRepository = featureRepository;
    }


    @Override
    public void save(@Valid ApiFeatureIntegrationRequestBodyRequestDTO requestDTO) {


        validateFeature(requestDTO.getFeatureId());

        List<ApiIntegrationRequestBodyParameters> requestBodyParameters =
                validateRequestBodyAttributes(requestDTO.getRequestBodyAttributes());

        integrationRequestBodyParametersRepository.saveAll(parseToClientApiFeatureRequestBodyParameters(requestDTO.getFeatureId(), requestBodyParameters));

    }

    @Override
    public List<IntegrationRequestBodyAttributeResponse> fetchRequestBodyAttributeByFeatureId(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, API_REQUEST_BODY_PARAMETERS);

        List<IntegrationRequestBodyAttributeResponse> responses =
                requestBodyParametersRepository.fetchRequestBodyAttributeByFeatureId(id);

        log.info(SEARCHING_PROCESS_COMPLETED, API_REQUEST_BODY_PARAMETERS, getDifferenceBetweenTwoTime(startTime));

        return responses;
    }

    private void validateFeature(Long featureId) {

        featureRepository.findActiveFeatureById(featureId)
                .orElseThrow(() -> FEATURE_NOT_FOUND.apply(featureId));
    }

    private List<ApiIntegrationRequestBodyParameters> validateRequestBodyAttributes(List<Long> bodyAttributeIds) {


        String ids = bodyAttributeIds.stream()
                .map(request -> request.toString())
                .collect(Collectors.joining(","));

        List<ApiIntegrationRequestBodyParameters> requestBodyParameters =
                requestBodyParametersRepository.findActiveRequestBodyParameterByIds(ids);

        return requestBodyParameters;

    }


    private Function<Long, NoContentFoundException> FEATURE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException
                (Feature.class, "id", id.toString());
    };
}

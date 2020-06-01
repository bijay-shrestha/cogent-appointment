package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.ApiFeatureIntegrationRequestBodyRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute.ApiFeatureIntegrationRequestBodyUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute.ApiIntegrationRequestBodySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationRequestBodyAttributeResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationRequestBodyAttribute.ApiRequestBodySearchDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.ApiFeatureIntegrationRequestBodyParametersRepository;
import com.cogent.cogentappointment.admin.repository.FeatureRepository;
import com.cogent.cogentappointment.admin.repository.IntegrationRequestBodyParametersRepository;
import com.cogent.cogentappointment.admin.service.ApiRequestBodyAttributeService;
import com.cogent.cogentappointment.admin.utils.IntegrationUtils;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.API_REQUEST_BODY_ATTRIBUTES;
import static com.cogent.cogentappointment.admin.utils.IntegrationUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak ON 2020/05/29-12:41 PM
 */
@Service
@Transactional
@Slf4j
public class ApiFeatureIntegrationRequestBodyParametersServiceImpl implements
        ApiRequestBodyAttributeService {

    private final ApiFeatureIntegrationRequestBodyParametersRepository
            integrationRequestBodyParametersRepository;
    private final IntegrationRequestBodyParametersRepository requestBodyParametersRepository;
    private final FeatureRepository featureRepository;

    public ApiFeatureIntegrationRequestBodyParametersServiceImpl
            (ApiFeatureIntegrationRequestBodyParametersRepository integrationRequestBodyParametersRepository,
             IntegrationRequestBodyParametersRepository requestBodyParametersRepository,
             FeatureRepository featureRepository) {
        this.integrationRequestBodyParametersRepository = integrationRequestBodyParametersRepository;

        this.requestBodyParametersRepository = requestBodyParametersRepository;
        this.featureRepository = featureRepository;
    }


    @Override
    public void save(ApiFeatureIntegrationRequestBodyRequestDTO requestDTO) {


        validateFeature(requestDTO.getFeatureId());

        List<ApiIntegrationRequestBodyParameters> requestBodyParameters =
                validateRequestBodyAttributes(requestDTO.getRequestBodyAttributes());

        integrationRequestBodyParametersRepository.
                saveAll(parseToClientApiFeatureRequestBodyParameters(requestDTO.getFeatureId(),
                        requestBodyParameters));

    }

    @Override
    public List<IntegrationRequestBodyAttributeResponse> fetchRequestBodyAttributeByFeatureId(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, API_REQUEST_BODY_ATTRIBUTES);

        List<IntegrationRequestBodyAttributeResponse> responses =
                requestBodyParametersRepository.fetchRequestBodyAttributeByFeatureId(id);

        log.info(SEARCHING_PROCESS_COMPLETED, API_REQUEST_BODY_ATTRIBUTES, getDifferenceBetweenTwoTime(startTime));

        return responses;
    }

    @Override
    public ApiRequestBodySearchDTO search(ApiIntegrationRequestBodySearchRequestDTO searchRequestDTO,
                                          Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, API_REQUEST_BODY_ATTRIBUTES);

        ApiRequestBodySearchDTO searchClientApiIntegration =
                requestBodyParametersRepository.searchApiRequestBodyAtrributes(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, API_REQUEST_BODY_ATTRIBUTES, getDifferenceBetweenTwoTime(startTime));

        return searchClientApiIntegration;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, API_REQUEST_BODY_ATTRIBUTES);

        List<ApiFeatureIntegrationRequestBodyParameters> featureIntegrationRequestBodyParameters =
                integrationRequestBodyParametersRepository
                .findApiFeatureRequestBodyParameterByFeatureId(deleteRequestDTO.getId())
                .orElseThrow(() -> API_REQUEST_BODY_ATTRIBUTE_NOT_FOUND.apply(deleteRequestDTO.getId()));

        parseToDeletedApiFeatureIntegrationRequestBodyParameters(featureIntegrationRequestBodyParameters,
                deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, API_REQUEST_BODY_ATTRIBUTES, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(ApiFeatureIntegrationRequestBodyUpdateRequestDTO requestDTO) {

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

    private Function<Long, NoContentFoundException> API_REQUEST_BODY_ATTRIBUTE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException
                (ApiFeatureIntegrationRequestBodyParameters.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> FEATURE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException
                (Feature.class, "id", id.toString());
    };
}

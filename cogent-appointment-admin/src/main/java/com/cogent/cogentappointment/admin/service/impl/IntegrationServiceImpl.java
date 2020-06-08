package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.*;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.clientIntegrationUpdate.ClientApiIntegrationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.clientIntegrationUpdate.ClientApiQueryParamtersUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.clientIntegrationUpdate.ClientApiRequestHeadersUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiQueryParametersUpdateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiRequestHeaderUpdateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ClientApiIntegrationUpdateResponseDTO;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.IntegrationService;
import com.cogent.cogentappointment.admin.utils.IntegrationUtils;
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
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.API_INTEGRATION;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.CLIENT_API_INTEGRATION;
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
    private final ApiIntegrationFormatRespository apiIntegrationFormatRespository;
    private final HttpRequestMethodRepository httpRequestMethodRepository;
    private final ApiQueryParametersRepository apiQueryParametersRepository;
    private final ApiRequestHeaderRepository apiRequestHeaderRepository;
    private final ApiFeatureIntegrationRepository apiFeatureIntegrationRepository;
    private final FeatureRepository featureRepository;
    private final IntegrationRepository integrationRepository;
    private final ApiFeatureIntegrationRequestBodyParametersRepository featureBodyParametersRepository;
    private final IntegrationRequestBodyParametersRepository requestBodyParametersRepository;
    private final IntegrationChannelRepository integrationChannelRepository;
    private final ApiIntegrationTypeRepository apiIntegrationTypeRepository;

    public IntegrationServiceImpl(ClientFeatureIntegrationRepository clientFeatureIntegrationRepository,
                                  ApiIntegrationFormatRespository apiIntegrationFormatRespository,
                                  HttpRequestMethodRepository httpRequestMethodRepository,
                                  ApiQueryParametersRepository apiQueryParametersRepository,
                                  ApiRequestHeaderRepository apiRequestHeaderRepository,
                                  ApiFeatureIntegrationRepository apiFeatureIntegrationRepository,
                                  FeatureRepository featureRepository,
                                  IntegrationRepository integrationRepository,
                                  ApiFeatureIntegrationRequestBodyParametersRepository featureBodyParametersRepository,
                                  IntegrationRequestBodyParametersRepository requestBodyParametersRepository,
                                  IntegrationChannelRepository integrationChannelRepository,
                                  ApiIntegrationTypeRepository apiIntegrationTypeRepository) {

        this.clientFeatureIntegrationRepository = clientFeatureIntegrationRepository;
        this.apiIntegrationFormatRespository = apiIntegrationFormatRespository;
        this.httpRequestMethodRepository = httpRequestMethodRepository;
        this.apiQueryParametersRepository = apiQueryParametersRepository;
        this.apiRequestHeaderRepository = apiRequestHeaderRepository;
        this.apiFeatureIntegrationRepository = apiFeatureIntegrationRepository;
        this.featureRepository = featureRepository;
        this.integrationRepository = integrationRepository;
        this.featureBodyParametersRepository = featureBodyParametersRepository;
        this.requestBodyParametersRepository = requestBodyParametersRepository;
        this.integrationChannelRepository = integrationChannelRepository;
        this.apiIntegrationTypeRepository = apiIntegrationTypeRepository;
    }

    @Override
    public void save(ClientApiIntegrationRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, CLIENT_API_INTEGRATION);

        validateFeatureAndHttpRequestMethod(requestDTO.getFeatureTypeId(),
                requestDTO.getRequestMethodId());

        checkClientFeatureIntegrationDuplicity(requestDTO.getHospitalId(),
                requestDTO.getFeatureTypeId(),
                requestDTO.getRequestMethodId());

        IntegrationChannel integrationChannel = integrationChannelRepository.
                findActiveIntegrationChannel(requestDTO.getIntegrationChannelId())
                .orElseThrow(() -> INTEGRATION_CHANNEL_NOT_FOUND.apply(requestDTO.getIntegrationChannelId()));

        ClientFeatureIntegration clientFeatureIntegration = parseToClientFeatureIntegration(requestDTO.getHospitalId(),
                requestDTO.getFeatureTypeId());
        clientFeatureIntegration.setIntegrationChannelId(integrationChannel);

        saveClientFeatureIntegration(clientFeatureIntegration);

        ApiIntegrationFormatRequestDTO apiIntegrationFormatRequestDTO = ApiIntegrationFormatRequestDTO.builder()
                .apiUrl(requestDTO.getApiUrl())
                .requestMethodId(requestDTO.getRequestMethodId())
                .build();

        ApiIntegrationFormat apiIntegrationFormat =
                parseToClientApiIntegrationFormat(apiIntegrationFormatRequestDTO);

        saveApiIntegrationFormat(apiIntegrationFormat);

        saveApiFeatureIntegration(clientFeatureIntegration.getId(),
                apiIntegrationFormat.getId(),
                requestDTO.getIntegrationChannelId());

        saveApiQueryParameters(requestDTO.getParametersRequestDTOS(),
                apiIntegrationFormat);

        saveApiRequestHeaders(requestDTO.getClientApiRequestHeaders(),
                apiIntegrationFormat);

        log.info(SAVING_PROCESS_COMPLETED, CLIENT_API_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

    }

    private void checkClientFeatureIntegrationDuplicity(Long hospitalId,
                                                        Long featureTypeId,
                                                        Long requestMethodId) {

        Long count = clientFeatureIntegrationRepository.
                findHospitalFeatureAndRequestMethod(hospitalId, featureTypeId, requestMethodId);

        if (count > 0) {

            throw new DataDuplicationException("Client Feature Integration Already Exist");
        }


    }


    @Override
    public void update(ClientApiIntegrationUpdateRequestDTO requestDTO) {

        log.info(UPDATING_PROCESS_STARTED, CLIENT_API_INTEGRATION);

        Long startTime = getTimeInMillisecondsFromLocalDate();

        ClientFeatureIntegration clientFeatureIntegration =
                clientFeatureIntegrationRepository.
                        findClientFeatureIntegrationById(requestDTO.getClientFeatureIntegrationId())
                        .orElseThrow(() -> CLIENT_FEATURE_NOT_FOUND.apply(requestDTO.getClientFeatureIntegrationId()));

        IntegrationChannel integrationChannel = integrationChannelRepository.
                findActiveIntegrationChannel(requestDTO.getIntegrationChannelId())
                .orElseThrow(() -> INTEGRATION_CHANNEL_NOT_FOUND.apply(requestDTO.getIntegrationChannelId()));


        clientFeatureIntegration.setFeatureId(requestDTO.getFeatureId());
        clientFeatureIntegration.setIntegrationChannelId(integrationChannel);

        List<ApiFeatureIntegration> apiFeatureIntegration =
                apiFeatureIntegrationRepository.findApiFeatureIntegrationbyClientFeatureId(clientFeatureIntegration.getId())
                        .orElseThrow(() -> API_FEATURE_INTEGRATION_NOT_FOUND.apply(requestDTO.getClientFeatureIntegrationId()));


        apiFeatureIntegration.forEach(featureIntegration -> {

            updateApiIntegrationFormat(requestDTO, featureIntegration.getApiIntegrationFormatId());

            updateApiQueryParameters(requestDTO.getQueryParametersRequestDTOS(), featureIntegration.getApiIntegrationFormatId());

            updateApiRequestHeaders(requestDTO.getClientApiRequestHeaders(), featureIntegration.getApiIntegrationFormatId());
        });

        log.info(UPDATING_PROCESS_COMPLETED, CLIENT_API_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

    }

    private void updateApiIntegrationFormat(ClientApiIntegrationUpdateRequestDTO requestDTO, Long apiIntegrationFormatId) {

        ApiIntegrationFormat apiIntegrationFormat = apiIntegrationFormatRespository.
                findByIntegrationFormatId(apiIntegrationFormatId)
                .orElseThrow(() -> API_INTEGRATION_FORMAT_NOT_FOUND.apply(apiIntegrationFormatId));

        apiIntegrationFormat.setUrl(requestDTO.getApiUrl());
        apiIntegrationFormat.setHttpRequestMethodId(requestDTO.getRequestMethodId());

        apiIntegrationFormatRespository.save(apiIntegrationFormat);
    }

    private void updateApiRequestHeaders(List<ClientApiRequestHeadersUpdateRequestDTO> queryParametersRequestDTOS,
                                         Long integrationFormatId) {

        queryParametersRequestDTOS.forEach(requestDTO -> {

            ApiRequestHeader apiRequestHeader =
                    apiRequestHeaderRepository.findApiRequestHeaderById(requestDTO.getId())
                            .orElse(null);

            if (apiRequestHeader == null) {

                ApiRequestHeader requestHeader = new ApiRequestHeader();
                requestHeader.setApiIntegrationFormatId(integrationFormatId);
                requestHeader.setKeyName(requestDTO.getKeyParam());
                requestHeader.setValue(requestDTO.getValueParam());
                requestHeader.setDescription(requestDTO.getDescription());
                requestHeader.setStatus(requestDTO.getStatus());

                apiRequestHeaderRepository.save(requestHeader);

            }

            if (apiRequestHeader != null) {
                apiRequestHeader.setKeyName(requestDTO.getKeyParam());
                apiRequestHeader.setValue(requestDTO.getValueParam());
                apiRequestHeader.setStatus(requestDTO.getStatus());
                apiRequestHeader.setDescription(requestDTO.getDescription());
                apiRequestHeader.setApiIntegrationFormatId(integrationFormatId);

            }

        });

    }

    private void updateApiQueryParameters(List<ClientApiQueryParamtersUpdateRequestDTO> queryParametersRequestDTOS,
                                          Long integrationFormatId) {

        queryParametersRequestDTOS.forEach(requestDTO -> {

            ApiQueryParameters apiQueryParameters =
                    apiQueryParametersRepository.findApiQueryParameterById(requestDTO.getId()).orElse(null);

            if (apiQueryParameters == null) {

                ApiQueryParameters queryParameters = new ApiQueryParameters();
                queryParameters.setApiIntegrationFormatId(integrationFormatId);
                queryParameters.setParam(requestDTO.getKeyParam());
                queryParameters.setValue(requestDTO.getValueParam());
                queryParameters.setStatus(requestDTO.getStatus());

                apiQueryParametersRepository.save(queryParameters);

            }

            if (apiQueryParameters != null) {
                apiQueryParameters.setParam(requestDTO.getKeyParam());
                apiQueryParameters.setValue(requestDTO.getValueParam());
                apiQueryParameters.setStatus(requestDTO.getStatus());
                apiQueryParameters.setApiIntegrationFormatId(integrationFormatId);

            }
        });


    }


    @Override
    public ClientApiIntegrationSearchDTO search(ClientApiIntegrationSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, API_INTEGRATION);

        ClientApiIntegrationSearchDTO clientApiIntegration =
                integrationRepository.searchClientApiIntegration(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, API_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

        return clientApiIntegration;
    }

    @Override
    public ClientApiIntegrationDetailResponseDTO fetchClientApiIntegrationById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, API_INTEGRATION);

        ClientApiIntegrationDetailResponseDTO clientApiIntegration =
                getClientApiIntegrationDetail(id);

        log.info(SEARCHING_PROCESS_COMPLETED, API_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

        return clientApiIntegration;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, API_INTEGRATION);

        ClientFeatureIntegration clientFeatureIntegration = clientFeatureIntegrationRepository
                .findClientFeatureIntegrationById(deleteRequestDTO.getId())
                .orElseThrow(() -> CLIENT_FEATURE_NOT_FOUND.apply(deleteRequestDTO.getId()));

        parseToDeletedClientFeatureIntegration(clientFeatureIntegration, deleteRequestDTO);

        List<ApiFeatureIntegration> apiFeatureIntegrationList = apiFeatureIntegrationRepository
                .findApiFeatureIntegrationbyClientFeatureId(clientFeatureIntegration.getId())
                .orElseThrow(() -> CLIENT_FEATURE_NOT_FOUND.apply(clientFeatureIntegration.getId()));

        List<ApiRequestHeader> apiRequestHeaderListToDelete = new ArrayList<>();
        List<ApiQueryParameters> apiQueryParameterToDelete = new ArrayList<>();

        apiFeatureIntegrationList.forEach(apiFeatureIntegration -> {

            ApiIntegrationFormat apiIntegrationFormat = apiIntegrationFormatRespository.
                    findByIntegrationFormatId(apiFeatureIntegration.getApiIntegrationFormatId())
                    .orElseThrow(() -> API_INTEGRATION_FORMAT_NOT_FOUND.apply(apiFeatureIntegration.getId()));


            List<ApiRequestHeader> apiRequestHeaderList = apiRequestHeaderRepository.
                    findApiRequestHeaderByApiFeatureIntegrationId(apiFeatureIntegration.getApiIntegrationFormatId()).
                    orElse(null);
            if (apiRequestHeaderList != null) {
                apiRequestHeaderListToDelete.addAll(apiRequestHeaderList);

            }

            List<ApiQueryParameters> apiQueryParametersList = apiQueryParametersRepository.
                    findApiRequestHeaderByApiFeatureIntegrationId(apiFeatureIntegration.getApiIntegrationFormatId())
                    .orElse(null);

            if (apiQueryParametersList != null) {
                apiQueryParameterToDelete.addAll(apiQueryParametersList);
            }

            IntegrationUtils.parseToDeletedApiIntegrationFormat(apiIntegrationFormat);


        });

        if (apiRequestHeaderListToDelete.size() > 0) {
            parseToDeletedApiRequestHeaders(apiRequestHeaderListToDelete);
        }

        if (apiQueryParameterToDelete.size() > 0) {
            parseToDeletedApiQueryParameters(apiQueryParameterToDelete);

        }

        parseToDeletedApiFeatureIntegration(apiFeatureIntegrationList);

        log.info(DELETING_PROCESS_COMPLETED, API_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

    }

    private ClientApiIntegrationDetailResponseDTO getClientApiIntegrationDetail(Long id) {

        ClientApiIntegrationResponseDTO featureIntegrationResponse = integrationRepository.
                findClientApiIntegration(id);

        Map<String, String> requestHeaderResponseDTO = integrationRepository.
                findApiRequestHeaders(featureIntegrationResponse.getFeatureId());

        Map<String, String> queryParametersResponseDTO = integrationRepository.
                findApiQueryParameters(featureIntegrationResponse.getFeatureId());

        ClientApiIntegrationDetailResponseDTO responseDTO = new ClientApiIntegrationDetailResponseDTO();
        responseDTO.setFeatureId(featureIntegrationResponse.getFeatureId());
        responseDTO.setFeatureCode(featureIntegrationResponse.getFeatureCode());
        responseDTO.setRequestMethodName(featureIntegrationResponse.getRequestMethodName());
        responseDTO.setRequestMethodId(featureIntegrationResponse.getRequestMethodId());
        responseDTO.setIntegrationChannel(featureIntegrationResponse.getIntegrationChannel());
        responseDTO.setIntegrationType(featureIntegrationResponse.getIntegrationType());
        responseDTO.setHospitalName(featureIntegrationResponse.getHospitalName());
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

    @Override
    public ClientApiIntegrationUpdateResponseDTO fetchDetailsForUpdate(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, API_INTEGRATION);

        ClientApiIntegrationUpdateResponseDTO updateResponseDTO =
                getClientApiIntegrationDetailForUpdate(id);

        log.info(FETCHING_PROCESS_COMPLETED, API_INTEGRATION, getDifferenceBetweenTwoTime(startTime));

        return updateResponseDTO;
    }

    private ClientApiIntegrationUpdateResponseDTO getClientApiIntegrationDetailForUpdate(Long id) {

        ClientApiIntegrationResponseDTO featureIntegrationResponse = integrationRepository.
                findClientApiIntegration(id);

        List<ApiRequestHeaderUpdateResponseDTO> requestHeaderResponseDTO = integrationRepository.
                findApiRequestHeadersForUpdate(featureIntegrationResponse.getFeatureId());

        List<ApiQueryParametersUpdateResponseDTO> queryParametersResponseDTO = integrationRepository.
                findApiQueryParametersForUpdate(featureIntegrationResponse.getFeatureId());

        ClientApiIntegrationUpdateResponseDTO responseDTO = new ClientApiIntegrationUpdateResponseDTO();
        responseDTO.setFeatureCode(featureIntegrationResponse.getFeatureCode());
        responseDTO.setFeatureId(featureIntegrationResponse.getFeatureId());
        responseDTO.setRequestMethodName(featureIntegrationResponse.getRequestMethodName());
        responseDTO.setRequestMethodId(featureIntegrationResponse.getRequestMethodId());
        responseDTO.setIntegrationTypeId(featureIntegrationResponse.getIntegrationTypeId());
        responseDTO.setIntegrationType(featureIntegrationResponse.getIntegrationType());
        responseDTO.setIntegrationChannelId(featureIntegrationResponse.getIntegrationChannelId());
        responseDTO.setIntegrationChannel(featureIntegrationResponse.getIntegrationChannel());
        responseDTO.setUrl(featureIntegrationResponse.getUrl());
        responseDTO.setHospitalName(featureIntegrationResponse.getHospitalName());
        responseDTO.setHeaders(requestHeaderResponseDTO);
        responseDTO.setQueryParameters(queryParametersResponseDTO);

        return responseDTO;
    }

    private void validateFeatureAndHttpRequestMethod(Long featureTypeId, Long requestMethodId) {

        featureRepository.findActiveFeatureById(featureTypeId)
                .orElseThrow(() -> FEATURE_NOT_FOUND.apply(featureTypeId));

        httpRequestMethodRepository.httpRequestMethodById(requestMethodId)
                .orElseThrow(() -> HTTP_REQUEST_METHOD_WITH_GIVEN_ID_NOT_FOUND.apply(requestMethodId));


    }

    private void saveApiRequestHeaders(List<ClientApiHeadersRequestDTO> clientApiRequestHeaders,
                                       ApiIntegrationFormat apiIntegrationFormat) {

        apiRequestHeaderRepository.saveAll(parseToClientApiRequestHeaders(clientApiRequestHeaders,
                apiIntegrationFormat.getId()));

    }

    private void saveApiQueryParameters(List<ClientApiQueryParametersRequestDTO> parametersRequestDTOS,
                                        ApiIntegrationFormat apiIntegrationFormat) {

        apiQueryParametersRepository.saveAll(parseToClientApiQueryParameters(parametersRequestDTOS,
                apiIntegrationFormat.getId()));

    }

    private void saveApiFeatureIntegration(Long clientFeatureIntegrationId,
                                           Long apiIntegrationFormatId,
                                           Long integrationChannelId) {

        apiFeatureIntegrationRepository.save(parseToClientApiFeatureIntegration(clientFeatureIntegrationId,
                apiIntegrationFormatId));

    }

    private void saveApiIntegrationFormat(ApiIntegrationFormat apiIntegrationFormat) {

        apiIntegrationFormatRespository.save(apiIntegrationFormat);
    }

    private void saveClientFeatureIntegration(ClientFeatureIntegration clientFeatureIntegration) {

        clientFeatureIntegrationRepository.save(clientFeatureIntegration);
    }

    private Function<Long, NoContentFoundException> API_REQUEST_HEADER_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(ApiRequestHeader.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> API_FEATURE_INTEGRATION_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(ApiFeatureIntegration.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> API_INTEGRATION_FORMAT_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(ApiIntegrationFormat.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> API_QUERY_PARAMETER_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(ApiQueryParameters.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> CLIENT_FEATURE_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(ClientFeatureIntegration.class, "id", id.toString());
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

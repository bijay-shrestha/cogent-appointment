package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.FeatureIntegrationResponse;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.repository.HospitalPatientInfoRepository;
import com.cogent.cogentappointment.client.repository.IntegrationRepository;
import com.cogent.cogentappointment.client.service.IntegrationCheckPointService;
import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.ThirdPartyHospitalResponse;
import com.cogent.cogentthirdpartyconnector.service.ThirdPartyConnectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.IntegrationApiMessages.*;
import static com.cogent.cogentappointment.client.constants.IntegrationApiConstants.BACK_END_CODE;
import static com.cogent.cogentappointment.client.constants.IntegrationApiConstants.FRONT_END_CODE;
import static com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils.generateRandomNumber;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;
import static com.cogent.cogentthirdpartyconnector.utils.ObjectMapperUtils.map;
import static java.lang.Integer.parseInt;

/**
 * @author rupak ON 2020/06/17-5:40 PM
 */
@Service
@Transactional
@Slf4j
public class IntegrationCheckPointServiceImpl implements IntegrationCheckPointService {

    private final IntegrationRepository integrationRepository;

    private final ThirdPartyConnectorService thirdPartyConnectorService;

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    private final AppointmentRepository appointmentRepository;

    public IntegrationCheckPointServiceImpl(IntegrationRepository integrationRepository,
                                            ThirdPartyConnectorService thirdPartyConnectorService,
                                            HospitalPatientInfoRepository hospitalPatientInfoRepository,
                                            AppointmentRepository appointmentRepository) {
        this.integrationRepository = integrationRepository;
        this.thirdPartyConnectorService = thirdPartyConnectorService;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void apiIntegrationCheckpointDoctorWise(Appointment appointment,
                                                   IntegrationBackendRequestDTO integrationRequestDTO) {

        String integrationChannelCode = integrationRequestDTO.getIntegrationChannelCode().trim().toUpperCase();

        switch (integrationChannelCode) {

            case FRONT_END_CODE:
                /*FRONT-END INTEGRATION*/
                if (integrationRequestDTO.getIsPatientNew())
                    updateHospitalPatientInfo(appointment, integrationRequestDTO.getHospitalNumber());

                break;

            case BACK_END_CODE:
                 /*BACK-END INTEGRATION*/
                ThirdPartyHospitalResponse thirdPartyHospitalResponse =
                        fetchThirdPartyHospitalResponseDoctorWise(integrationRequestDTO);

                if (integrationRequestDTO.getIsPatientNew())
                    updateHospitalPatientInfo(appointment, thirdPartyHospitalResponse.getResponseData());

                break;

            default:
                throw new BadRequestException(String.format(INVALID_INTEGRATION_CHANNEL_CODE, integrationChannelCode));
        }
    }

    @Override
    public void apiIntegrationCheckpointDepartmentWise(Appointment appointment,
                                                       IntegrationBackendRequestDTO integrationRequestDTO) {

        String integrationChannelCode = integrationRequestDTO.getIntegrationChannelCode().trim().toUpperCase();

        switch (integrationChannelCode) {

            case FRONT_END_CODE:
                /*FRONT-END INTEGRATION*/
                if (integrationRequestDTO.getIsPatientNew())
                    updateHospitalPatientInfo(appointment, integrationRequestDTO.getHospitalNumber());

                break;

            case BACK_END_CODE:
                 /*BACK-END INTEGRATION*/
                ThirdPartyHospitalResponse thirdPartyHospitalResponse =
                        fetchThirdPartyHospitalResponseDepartmentWise(integrationRequestDTO);

                if (integrationRequestDTO.getIsPatientNew())
                    updateHospitalPatientInfo(appointment, thirdPartyHospitalResponse.getResponseData());

                break;

            default:
                throw new BadRequestException(String.format(INVALID_INTEGRATION_CHANNEL_CODE, integrationChannelCode));
        }
    }

    @Override
    public BackendIntegrationApiInfo getAppointmentModeApiIntegration(IntegrationRefundRequestDTO integrationRefundRequestDTO, Long id, String generatedEsewaHmac) {
        return null;
    }

    private void updateHospitalPatientInfo(Appointment appointment, String hospitalNumber) {

        HospitalPatientInfo hospitalPatientInfo = fetchHospitalPatientInfo(
                appointment.getPatientId().getId(), appointment.getHospitalId().getId());

        hospitalPatientInfo.setHospitalNumber(hospitalNumber);
    }

    private ThirdPartyHospitalResponse fetchThirdPartyHospitalResponseDoctorWise(
            IntegrationBackendRequestDTO requestDTO) {

        BackendIntegrationApiInfo backendIntegrationApiInfo = getBackendIntegrationApiInfo(requestDTO);

        if (!Objects.isNull(backendIntegrationApiInfo)) {

            //todo : in case of doc wise info
//            ThirdPartyDoctorWiseAppointmentCheckInDTO thirdPartyCheckInDetails =
//                    appointmentRepository.fetchAppointmentDetailForDoctorWiseApptCheckIn(requestDTO.getAppointmentId(),
//                            getLoggedInHospitalId());
//
//            thirdPartyCheckInDetails.setSex(toNormalCase(thirdPartyCheckInDetails.getGender().name()));

            ResponseEntity<?> responseEntity =
                    thirdPartyConnectorService.callThirdPartyDoctorCheckInService(backendIntegrationApiInfo);

            return fetchThirdPartyHospitalResponse(responseEntity);
        } else {
            return new ThirdPartyHospitalResponse("400", "Third party hospital information Not found",
                    "Third party hospital information Not found");
        }
    }

    private ThirdPartyHospitalResponse fetchThirdPartyHospitalResponseDepartmentWise(
            IntegrationBackendRequestDTO requestDTO) {

        BackendIntegrationApiInfo backendIntegrationApiInfo = getBackendIntegrationApiInfo(requestDTO);

        if (!Objects.isNull(backendIntegrationApiInfo)) {

            ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO thirdPartyCheckInDetails =
                    appointmentRepository.fetchAppointmentDetailForHospitalDeptCheckIn(requestDTO.getAppointmentId(),
                            getLoggedInHospitalId());

            thirdPartyCheckInDetails.setName(generateRandomNumber(3));
            thirdPartyCheckInDetails.setSex(toNormalCase(thirdPartyCheckInDetails.getGender().name()));

            //todo : room no and dept name is static in bheri api
            thirdPartyCheckInDetails.setSection("ENT");

            ResponseEntity<?> responseEntity =
                    thirdPartyConnectorService.callThirdPartyHospitalDepartmentCheckInService(backendIntegrationApiInfo,
                            thirdPartyCheckInDetails);

            return fetchThirdPartyHospitalResponse(responseEntity);
        } else {
            return new ThirdPartyHospitalResponse("400", "Third party hospital information Not found",
                    "Third party hospital information Not found");
        }
    }

    private BackendIntegrationApiInfo getBackendIntegrationApiInfo(
            IntegrationBackendRequestDTO integrationBackendRequestDTO) {

        FeatureIntegrationResponse featureIntegrationResponse = integrationRepository.
                fetchClientIntegrationResponseDTOforBackendIntegration(integrationBackendRequestDTO);

        if (!Objects.isNull(featureIntegrationResponse)) {

            Map<String, String> requestHeaderResponse = integrationRepository.
                    findApiRequestHeaders(featureIntegrationResponse.getApiIntegrationFormatId());

            Map<String, String> queryParametersResponse = integrationRepository.
                    findApiQueryParameters(featureIntegrationResponse.getApiIntegrationFormatId());

            //headers
            HttpHeaders headers = addHeaderDetails(requestHeaderResponse);

            BackendIntegrationApiInfo backendIntegrationApiInfo = new BackendIntegrationApiInfo();

            backendIntegrationApiInfo.setApiUri(featureIntegrationResponse.getUrl());
            backendIntegrationApiInfo.setHttpHeaders(headers);

            if (!queryParametersResponse.isEmpty())
                backendIntegrationApiInfo.setQueryParameters(queryParametersResponse);

            backendIntegrationApiInfo.setHttpMethod(featureIntegrationResponse.getRequestMethod());

            return backendIntegrationApiInfo;
        }

        return null;
    }

    private ThirdPartyHospitalResponse fetchThirdPartyHospitalResponse(ResponseEntity<?> responseEntity) {

        if (responseEntity.getStatusCode().value() == 403)
            throw new OperationUnsuccessfulException(INTEGRATION_BHERI_HOSPITAL_FORBIDDEN_ERROR);

        ThirdPartyHospitalResponse thirdPartyHospitalResponse = null;
        try {
            thirdPartyHospitalResponse = map((String) responseEntity.getBody(),
                    ThirdPartyHospitalResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        validateThirdPartyHospitalResponseStatus(thirdPartyHospitalResponse);

        return thirdPartyHospitalResponse;
    }

    private void validateThirdPartyHospitalResponseStatus(ThirdPartyHospitalResponse thirdPartyHospitalResponse) {

        if (parseInt(thirdPartyHospitalResponse.getStatusCode()) == 500) {
            throw new OperationUnsuccessfulException(INTEGRATION_BHERI_HOSPITAL_ERROR);
        }

        if (parseInt(thirdPartyHospitalResponse.getStatusCode()) == 403) {
            throw new OperationUnsuccessfulException(INTEGRATION_BHERI_HOSPITAL_FORBIDDEN_ERROR);
        }

        if (parseInt(thirdPartyHospitalResponse.getStatusCode()) == 400) {
            throw new OperationUnsuccessfulException(INTEGRATION_API_BAD_REQUEST);
        }
    }

    private HospitalPatientInfo fetchHospitalPatientInfo(Long patientId, Long hospitalId) {
        return hospitalPatientInfoRepository.findByPatientAndHospitalId(patientId, hospitalId)
                .orElseThrow(() -> HOSPITAL_PATIENT_INFO_NOT_FOUND.apply(patientId));
    }

    private Function<Long, NoContentFoundException> HOSPITAL_PATIENT_INFO_NOT_FOUND = (patientId) -> {
        throw new NoContentFoundException(HospitalPatientInfo.class, "patientId", patientId.toString());
    };

    private HttpHeaders addHeaderDetails(Map<String, String> requestHeaderResponse) {

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/54.0.2840.99 Safari/537.36");

        requestHeaderResponse.forEach(headers::add);

        return headers;
    }


}

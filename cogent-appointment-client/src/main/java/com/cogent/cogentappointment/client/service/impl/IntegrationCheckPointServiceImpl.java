package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.FeatureIntegrationResponse;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.AppointmentService;
import com.cogent.cogentappointment.client.service.IntegrationCheckPointService;
import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.ThirdPartyHospitalResponse;
import com.cogent.cogentthirdpartyconnector.response.integrationThirdParty.ThirdPartyResponse;
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
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_ESEWA_CODE;
import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.RefundResponseConstant.*;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.*;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.INVALID_INTEGRATION_CHANNEL_CODE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.IntegrationApiMessages.*;
import static com.cogent.cogentappointment.client.constants.IntegrationApiConstants.BACK_END_CODE;
import static com.cogent.cogentappointment.client.constants.IntegrationApiConstants.FRONT_END_CODE;
import static com.cogent.cogentappointment.client.security.hmac.HMACUtils.getSigatureForEsewa;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.parseRefundRejectDetails;
import static com.cogent.cogentappointment.client.utils.RefundStatusUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils.generateRandomNumber;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;
import static com.cogent.cogentappointment.commons.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentthirdpartyconnector.utils.ApiUriUtils.parseApiUri;
import static com.cogent.cogentthirdpartyconnector.utils.HttpHeaderUtils.generateApiHeaders;
import static com.cogent.cogentthirdpartyconnector.utils.ObjectMapperUtils.map;
import static com.cogent.cogentthirdpartyconnector.utils.RequestBodyUtils.getEsewaRequestBody;
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

    private final AppointmentEsewaRequestRepository appointmentEsewaRequestRepository;

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    private final AppointmentService appointmentService;

    public IntegrationCheckPointServiceImpl(IntegrationRepository integrationRepository,
                                            ThirdPartyConnectorService thirdPartyConnectorService,
                                            HospitalPatientInfoRepository hospitalPatientInfoRepository,
                                            AppointmentRepository appointmentRepository,
                                            AppointmentEsewaRequestRepository appointmentEsewaRequestRepository,
                                            AppointmentRefundDetailRepository appointmentRefundDetailRepository,
                                            AppointmentService appointmentService) {
        this.integrationRepository = integrationRepository;
        this.thirdPartyConnectorService = thirdPartyConnectorService;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentEsewaRequestRepository = appointmentEsewaRequestRepository;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.appointmentService = appointmentService;
    }

    @Override
    public void apiIntegrationCheckpointForDoctorAppointment(Appointment appointment,
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
                        fetchThirdPartyHospitalResponseForDoctorAppointment(integrationRequestDTO);

                if (integrationRequestDTO.getIsPatientNew())
                    updateHospitalPatientInfo(appointment, thirdPartyHospitalResponse.getResponseData());

                break;

            default:
                throw new BadRequestException(String.format(INVALID_INTEGRATION_CHANNEL_CODE, integrationChannelCode));
        }
    }

    @Override
    public void apiIntegrationCheckpointForDepartmentAppointment(Appointment appointment,
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

                if (integrationRequestDTO.getIsPatientNew()) {
                    ThirdPartyHospitalResponse thirdPartyHospitalResponse =
                            fetchThirdPartyHospitalResponseForDepartmentAppointment(integrationRequestDTO);

                    updateHospitalPatientInfo(appointment, thirdPartyHospitalResponse.getResponseData());
                }

                break;

            default:
                throw new BadRequestException(String.format(INVALID_INTEGRATION_CHANNEL_CODE, integrationChannelCode));
        }
    }

    @Override
    public BackendIntegrationApiInfo getAppointmentModeApiIntegration(IntegrationRefundRequestDTO refundRequestDTO,
                                                                      Long appointmentModeId,
                                                                      String generatedHmacKey) {

        AdminFeatureIntegrationResponse featureIntegrationResponse = integrationRepository.
                fetchAppointmentModeIntegrationResponseDTOforBackendIntegration(refundRequestDTO, appointmentModeId);

        Map<String, String> requestHeaderResponse = integrationRepository.
                findAdminModeApiRequestHeaders(featureIntegrationResponse.getApiIntegrationFormatId());

        Map<String, String> queryParametersResponse = integrationRepository.
                findAdminModeApiQueryParameters(featureIntegrationResponse.getApiIntegrationFormatId());

//        List<String> requestBody = getRequestBodyByFeature(featureIntegrationResponse.getFeatureId(),
//                featureIntegrationResponse.getRequestMethod());

        BackendIntegrationApiInfo integrationApiInfo = new BackendIntegrationApiInfo();
        integrationApiInfo.setApiUri(featureIntegrationResponse.getUrl());
        integrationApiInfo.setHttpHeaders(generateApiHeaders(requestHeaderResponse, generatedHmacKey));
//        integrationApiInfo.setRequestBody(requestBody);

        if (!queryParametersResponse.isEmpty()) {
            integrationApiInfo.setQueryParameters(queryParametersResponse);
        }
        integrationApiInfo.setHttpMethod(featureIntegrationResponse.getRequestMethod());

        return integrationApiInfo;

    }

    @Override
    public void apiIntegrationCheckpointForRefundAppointment(Appointment appointment,
                                                             AppointmentTransactionDetail appointmentTransactionDetail,
                                                             AppointmentRefundDetail refundAppointmentDetail,
                                                             IntegrationRefundRequestDTO refundRequestDTO) {

        if (refundRequestDTO.getIntegrationChannelCode() == null) {
            throw new BadRequestException(INTEGRATION_CHANNEL_CODE_IS_NULL);
        }

        //condition to check transaction number for follow up case.
        //for free follow up case we don't have to hit third party API for refund and its status is changed to REFUNDED in database.
        //Both FrontEnd Refund Remarks and Esewa Remarks are saved into Appointment & RefundAppointmentDetail Tables Respectively.
        if (appointmentTransactionDetail.getTransactionNumber().equals(null) ||
                appointmentTransactionDetail.getTransactionNumber().equalsIgnoreCase("N/A")) {

            changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                    refundAppointmentDetail,
                    refundRequestDTO.getRemarks(), null);
        } else {

            switch (refundRequestDTO.getIntegrationChannelCode()) {

                case BACK_END_CODE:

                    ThirdPartyResponse response = processRefundRequest(refundRequestDTO,
                            appointment,
                            appointmentTransactionDetail,
                            refundAppointmentDetail,
                            true);

                    if (!Objects.isNull(response.getCode())) {
                        throw new BadRequestException(response.getMessage(), response.getMessage());
                    }

                    updateAppointmentAndAppointmentRefundDetails(response.getStatus(),
                            refundRequestDTO.getRemarks(),
                            appointment,
                            refundAppointmentDetail,
                            null);

                    break;

                case FRONT_END_CODE:

                    updateAppointmentAndAppointmentRefundDetails(refundRequestDTO.getStatus(),
                            refundRequestDTO.getRemarks(),
                            appointment,
                            refundAppointmentDetail,
                            null);
                    break;

                default:
                    throw new BadRequestException(INVALID_INTEGRATION_CHANNEL_CODE);
            }

        }
    }

    @Override
    public void apiIntegrationCheckpointForRejectAppointment(Appointment appointment,
                                                             AppointmentTransactionDetail appointmentTransactionDetail,
                                                             AppointmentRefundDetail refundAppointmentDetail,
                                                             IntegrationRefundRequestDTO refundRequestDTO) {

        if (refundRequestDTO.getIntegrationChannelCode().equalsIgnoreCase(BACK_END_CODE)) {

            //condition to check transaction number for follow up case.
            //for free follow up case we don't have to hit third party API for refund and its status is changed to REFUNDED in database.
            //Both FrontEnd Refund Remarks and Esewa Remarks are saved into Appointment & RefundAppointmentDetail Tables Respectively.
            if (appointmentTransactionDetail.getTransactionNumber().equals(null) ||
                    appointmentTransactionDetail.getTransactionNumber().equalsIgnoreCase("N/A")) {

                saveAppointmentRefundDetail(parseRefundRejectDetails(refundRequestDTO.getRemarks(),
                        refundAppointmentDetail));
            } else {

                ThirdPartyResponse response = processRefundRequest(refundRequestDTO,
                        appointment,
                        appointmentTransactionDetail,
                        refundAppointmentDetail,
                        false);

                if (!Objects.isNull(response.getCode())) {
                    throw new BadRequestException(response.getMessage(), response.getMessage());
                }

                updateAppointmentAndAppointmentRefundDetails(response.getStatus(),
                        refundRequestDTO.getRemarks(),
                        appointment,
                        refundAppointmentDetail,
                        refundRequestDTO);
            }

        }

        if (refundRequestDTO.getIntegrationChannelCode().equalsIgnoreCase(FRONT_END_CODE)) {

            //condition to check transaction number for follow up case.
            //for free follow up case we don't have to hit third party API for refund and its status is changed to REFUNDED in database.
            //Both FrontEnd Refund Remarks and Esewa Remarks are saved into Appointment & RefundAppointmentDetail Tables Respectively.
            if (appointmentTransactionDetail.getTransactionNumber().equals(null) ||
                    appointmentTransactionDetail.getTransactionNumber().equalsIgnoreCase("N/A")) {

                saveAppointmentRefundDetail(parseRefundRejectDetails(refundRequestDTO.getRemarks(),
                        refundAppointmentDetail));
            } else {

                updateAppointmentAndAppointmentRefundDetails(refundRequestDTO.getStatus(),
                        refundRequestDTO.getRemarks(),
                        appointment,
                        refundAppointmentDetail,
                        null);
            }
        }


    }

    @Override
    public void apiIntegrationCheckpointForRefundStatus(Appointment appointment,
                                                        AppointmentRefundDetail appointmentRefundDetail,
                                                        AppointmentTransactionDetail appointmentTransactionDetail,
                                                        RefundStatusRequestDTO requestDTO) {

        IntegrationRefundRequestDTO refundRequestDTO = requestDTO.getRefundRequestDTO();

        if (refundRequestDTO.getIntegrationChannelCode().equalsIgnoreCase(BACK_END_CODE)) {

            //condition to check transaction number for follow up case.
            //for free follow up case we don't have to hit third party API for refund and its status is changed to REFUNDED in database.
            //Both FrontEnd Refund Remarks and Esewa Remarks are saved into Appointment & RefundAppointmentDetail Tables Respectively.
            if (appointmentTransactionDetail.getTransactionNumber().equals(null) ||
                    appointmentTransactionDetail.getTransactionNumber().equalsIgnoreCase("N/A")) {

                //todo
            } else {

                ThirdPartyResponse thirdPartyResponse = processRefundRequest(refundRequestDTO,
                        appointment,
                        appointmentTransactionDetail,
                        appointmentRefundDetail,
                        false);//tp be ask what is boolean in this case.

                updateRefundStatusDetails(appointment,
                        appointmentRefundDetail,
                        appointmentTransactionDetail,
                        refundRequestDTO,
                        thirdPartyResponse.getStatus());

            }

        }

        if (refundRequestDTO.getIntegrationChannelCode().equalsIgnoreCase(FRONT_END_CODE)) {

            //condition to check transaction number for follow up case.
            //for free follow up case we don't have to hit third party API for refund and its status is changed to REFUNDED in database.
            //Both FrontEnd Refund Remarks and Esewa Remarks are saved into Appointment & RefundAppointmentDetail Tables Respectively.
            if (appointmentTransactionDetail.getTransactionNumber().equals(null) ||
                    appointmentTransactionDetail.getTransactionNumber().equalsIgnoreCase("N/A")) {
//todo

            } else {
//todo

            }
        }

    }

    private ThirdPartyResponse processRefundRequest(IntegrationRefundRequestDTO integrationRefundRequestDTO,
                                                    Appointment appointment,
                                                    AppointmentTransactionDetail transactionDetail,
                                                    AppointmentRefundDetail appointmentRefundDetail,
                                                    Boolean isRefund) {

        ThirdPartyResponse thirdPartyResponse = null;

        switch (appointment.getAppointmentModeId().getCode()) {

            case APPOINTMENT_MODE_ESEWA_CODE:
                thirdPartyResponse = requestEsewaForRefund(integrationRefundRequestDTO,
                        appointment,
                        transactionDetail,
                        appointmentRefundDetail,
                        isRefund);
                break;

            default:
                throw new BadRequestException("APPOINTMENT MODE NOT VALID");
        }


        return thirdPartyResponse;
    }

    private void updateRefundStatusDetails(Appointment appointment,
                                           AppointmentRefundDetail appointmentRefundDetail,
                                           AppointmentTransactionDetail appointmentTransactionDetail,
                                           IntegrationRefundRequestDTO refundRequestDTO,
                                           String response) {

        switch (response) {

            case PARTIAL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                        appointmentRefundDetail,
                        refundRequestDTO.getRemarks(),
                        response);
                break;

            case FULL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                        appointmentRefundDetail,
                        refundRequestDTO.getRemarks(),
                        response);
                break;

            case AMBIGIOUS:
                appointmentService.approveRefundAppointment(refundRequestDTO);

            default:
                appointmentService.approveRefundAppointment(refundRequestDTO);
        }
    }

    private void updateAppointmentAndAppointmentRefundDetails(String response,
                                                              String frontEndRemarks,
                                                              Appointment appointment,
                                                              AppointmentRefundDetail refundAppointmentDetail,
                                                              IntegrationRefundRequestDTO refundRequestDTO) {
        switch (response) {

            case PARTIAL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                        refundAppointmentDetail,
                        frontEndRemarks,
                        response);
                break;

            case FULL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                        refundAppointmentDetail,
                        frontEndRemarks,
                        response);
                break;

            case SUCCESS:
                saveAppointmentRefundDetail(parseRefundRejectDetails(refundRequestDTO.getRemarks(),
                        refundAppointmentDetail));
                break;

            case AMBIGIOUS:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment,
                        refundAppointmentDetail,
                        response);
                break;

            default:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment,
                        refundAppointmentDetail,
                        response);
                break;

        }
    }


    public void changeAppointmentAndAppointmentRefundDetailStatus(Appointment appointment,
                                                                  AppointmentRefundDetail refundAppointmentDetail,
                                                                  String frontEndRemarks, String esewaRemarks) {

        save(changeAppointmentStatus.apply(appointment, frontEndRemarks));

        saveAppointmentRefundDetail(changeAppointmentRefundDetailStatus.apply(refundAppointmentDetail, esewaRemarks));

    }

    public void defaultAppointmentAndAppointmentRefundDetailStatusChanges(Appointment appointment,
                                                                          AppointmentRefundDetail refundAppointmentDetail,
                                                                          String remarks) {

        save(defaultAppointmentStatusChange.apply(appointment, remarks));

        saveAppointmentRefundDetail(defaultAppointmentRefundDetailStatusChange.apply(refundAppointmentDetail, remarks));

    }

    private void saveAppointmentRefundDetail(AppointmentRefundDetail appointmentRefundDetail) {
        appointmentRefundDetailRepository.save(appointmentRefundDetail);
    }

    private void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }


    private ThirdPartyResponse requestEsewaForRefund(IntegrationRefundRequestDTO integrationRefundRequestDTO,
                                                     Appointment appointment,
                                                     AppointmentTransactionDetail transactionDetail,
                                                     AppointmentRefundDetail appointmentRefundDetail,
                                                     Boolean isRefund) {

        String esewaId = getEsewaId(appointment.getId());
        String generatedEsewaHmac = getSigatureForEsewa.apply(esewaId,
                appointment.getHospitalId().getEsewaMerchantCode());

        BackendIntegrationApiInfo integrationApiInfo = getAppointmentModeApiIntegration(integrationRefundRequestDTO,
                appointment.getAppointmentModeId().getId(),
                generatedEsewaHmac);

        if (!Objects.isNull(integrationApiInfo)) {

            EsewaRefundRequestDTO esewaRefundRequestDTO = getEsewaRequestBody(appointment,
                    transactionDetail,
                    appointmentRefundDetail,
                    isRefund, integrationRefundRequestDTO.getRemarks());
            esewaRefundRequestDTO.setEsewa_id(esewaId);

            integrationApiInfo.setApiUri(parseApiUri(integrationApiInfo.getApiUri(), transactionDetail.getTransactionNumber()));

            ResponseEntity<?> responseEntity = thirdPartyConnectorService.callEsewaRefundService(integrationApiInfo,
                    esewaRefundRequestDTO);

            if (responseEntity.getBody() == null) {
                throw new OperationUnsuccessfulException("ThirdParty API response is null");
            }


            ThirdPartyResponse thirdPartyResponse = null;
            try {
                thirdPartyResponse = map(responseEntity.getBody().toString(),
                        ThirdPartyResponse.class);
            } catch (IOException e)

            {
                e.printStackTrace();
                throw new OperationUnsuccessfulException("ThirdParty API response is null");
            }

            if (thirdPartyResponse.getCode() != null) {
                validateThirdPartyException(thirdPartyResponse);
            }


            return thirdPartyResponse;

        } else {
            return new ThirdPartyResponse("400", "Third party API information Not found",
                    "Third party API information Not found");
        }


    }

    private void validateThirdPartyException(ThirdPartyResponse thirdPartyResponse) {

        if (thirdPartyResponse == null) {
            throw new OperationUnsuccessfulException("Third Party API Returns null");

        }

        if (thirdPartyResponse.getCode().equals(400)) {
            throw new OperationUnsuccessfulException(ESEWA_REFUND_API_BAD_REQUEST_MESSAGE);

        }

        if (thirdPartyResponse.getCode().equals(404)) {
            throw new OperationUnsuccessfulException(ESEWA_REFUND_API_NOT_FOUND_MESSAGE);

        }

        if (thirdPartyResponse.getCode().equals(403)) {
            throw new OperationUnsuccessfulException(ESEWA_REFUND_API_FORBIDDEN_MESSAGE);

        }

        if (thirdPartyResponse.getCode().equals(403)) {
            throw new OperationUnsuccessfulException(ESEWA_REFUND_API_FORBIDDEN_MESSAGE);

        }
    }

    private String getEsewaId(Long appointmentId) {

        return appointmentEsewaRequestRepository.fetchEsewaIdByAppointmentId(appointmentId).
                orElseThrow(() -> ESEWA_ID_NOT_FOUND.get());

    }


    private void updateHospitalPatientInfo(Appointment appointment, String hospitalNumber) {

        HospitalPatientInfo hospitalPatientInfo = fetchHospitalPatientInfo(
                appointment.getPatientId().getId(), appointment.getHospitalId().getId());

        hospitalPatientInfo.setHospitalNumber(hospitalNumber);
    }

    private ThirdPartyHospitalResponse fetchThirdPartyHospitalResponseForDoctorAppointment(
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
                    thirdPartyConnectorService.callThirdPartyDoctorAppointmentCheckInService(backendIntegrationApiInfo);

            return fetchThirdPartyHospitalResponse(responseEntity);
        } else {
            return new ThirdPartyHospitalResponse("400", "Third party hospital information Not found",
                    "Third party hospital information Not found");
        }
    }

    private ThirdPartyHospitalResponse fetchThirdPartyHospitalResponseForDepartmentAppointment(
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
                    thirdPartyConnectorService.callThirdPartyHospitalDepartmentAppointmentCheckInService(
                            backendIntegrationApiInfo, thirdPartyCheckInDetails);

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

    private Supplier<NoContentFoundException> ESEWA_ID_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, "eSewa Id");
        throw new NoContentFoundException("eSewa Id not Found", "eSewa Id not Found");
    };


}

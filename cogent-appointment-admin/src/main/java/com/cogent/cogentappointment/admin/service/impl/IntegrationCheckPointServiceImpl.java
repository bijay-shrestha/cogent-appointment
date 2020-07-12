package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationRequestBodyAttributeResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.IntegrationCheckPointService;
import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentthirdpartyconnector.request.EsewaPaymentStatus;
import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.ThirdPartyHospitalResponse;
import com.cogent.cogentthirdpartyconnector.response.integrationThirdParty.ThirdPartyResponse;
import com.cogent.cogentthirdpartyconnector.service.ThirdPartyConnectorService;
import com.cogent.cogentthirdpartyconnector.utils.ObjectMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_ESEWA_CODE;
import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_FONEPAY_CODE;
import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.RefundResponseConstant.*;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.INTEGRATION_CHANNEL_CODE_IS_NULL;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.INVALID_APPOINTMENT_MODE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.IntegrationApiMessages.*;
import static com.cogent.cogentappointment.admin.constants.IntegrationApiConstants.BACK_END_CODE;
import static com.cogent.cogentappointment.admin.constants.IntegrationApiConstants.FRONT_END_CODE;
import static com.cogent.cogentappointment.admin.constants.StringConstant.DATA;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.admin.security.hmac.HMACUtils.getSignatureForEsewa;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.parseRefundRejectDetails;
import static com.cogent.cogentappointment.admin.utils.RefundStatusUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.NumberFormatterUtils.generateRandomNumber;
import static com.cogent.cogentappointment.commons.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.commons.security.jwt.JwtUtils.generateToken;
import static com.cogent.cogentappointment.commons.utils.StringUtil.toNormalCase;
import static com.cogent.cogentthirdpartyconnector.utils.ApiUriUtils.parseApiUri;
import static com.cogent.cogentthirdpartyconnector.utils.HMACUtils.getSigatureForEsewa;
import static com.cogent.cogentthirdpartyconnector.utils.HttpHeaderUtils.generateApiHeaders;
import static com.cogent.cogentthirdpartyconnector.utils.ObjectMapperUtils.map;
import static com.cogent.cogentthirdpartyconnector.utils.RequestBodyUtils.getEsewaPayementStatusRequestBody;
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

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    private final AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    private final AppointmentEsewaRequestRepository appointmentEsewaRequestRepository;

    public IntegrationCheckPointServiceImpl(IntegrationRepository integrationRepository,
                                            ThirdPartyConnectorService thirdPartyConnectorService,
                                            HospitalPatientInfoRepository hospitalPatientInfoRepository,
                                            AppointmentRepository appointmentRepository,
                                            AppointmentRefundDetailRepository appointmentRefundDetailRepository,
                                            AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                            AppointmentEsewaRequestRepository appointmentEsewaRequestRepository) {
        this.integrationRepository = integrationRepository;
        this.thirdPartyConnectorService = thirdPartyConnectorService;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.appointmentEsewaRequestRepository = appointmentEsewaRequestRepository;
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
    public BackendIntegrationApiInfo getAppointmentModeApiIntegration(IntegrationBackendRequestDTO backendRequestDTO,
                                                                      Long appointmentModeId,
                                                                      String generatedHmacKey) {

        AdminFeatureIntegrationResponse featureIntegrationResponse = integrationRepository.
                fetchAppointmentModeIntegrationResponseDTOforBackendIntegration(backendRequestDTO,
                        appointmentModeId);

        Map<String, String> requestHeaderResponse = integrationRepository.
                findAdminModeApiRequestHeaders(featureIntegrationResponse.getApiIntegrationFormatId());

        Map<String, String> queryParametersResponse = integrationRepository.
                findAdminModeApiQueryParameters(featureIntegrationResponse.getApiIntegrationFormatId());

        List<String> requestBody = getRequestBodyByFeature(featureIntegrationResponse.getFeatureId(),
                featureIntegrationResponse.getRequestMethod());

        BackendIntegrationApiInfo integrationApiInfo = new BackendIntegrationApiInfo();
        integrationApiInfo.setApiUri(featureIntegrationResponse.getUrl());
        integrationApiInfo.setHttpHeaders(generateApiHeaders(requestHeaderResponse, generatedHmacKey));
        integrationApiInfo.setRequestBody(requestBody);

        if (!queryParametersResponse.isEmpty()) {
            integrationApiInfo.setQueryParameters(queryParametersResponse);
        }
        integrationApiInfo.setHttpMethod(featureIntegrationResponse.getRequestMethod());

        return integrationApiInfo;
    }

    @Override
    public ThirdPartyResponse processEsewaRefundRequest(Appointment appointment,
                                                        AppointmentTransactionDetail transactionDetail,
                                                        AppointmentRefundDetail appointmentRefundDetail,
                                                        Boolean isRefund,
                                                        IntegrationRefundRequestDTO refundRequestDTO) {


        String esewaId = getEsewaId(appointment.getId());
        String generatedEsewaHmac = getSignatureForEsewa.apply(esewaId,
                appointment.getHospitalId().getEsewaMerchantCode());

        IntegrationBackendRequestDTO integrationBackendRequestDTO = IntegrationBackendRequestDTO.builder()
                .appointmentId(refundRequestDTO.getAppointmentId())
                .featureCode(refundRequestDTO.getFeatureCode())
                .integrationChannelCode(refundRequestDTO.getIntegrationChannelCode())
                .hospitalId(refundRequestDTO.getHospitalId())
                .build();

        BackendIntegrationApiInfo integrationApiInfo = getAppointmentModeApiIntegration(integrationBackendRequestDTO,
                appointment.getAppointmentModeId().getId(),
                generatedEsewaHmac);

        EsewaRefundRequestDTO esewaRefundRequestDTO = getEsewaRequestBody(appointment,
                transactionDetail,
                appointmentRefundDetail,
                isRefund, refundRequestDTO.getRemarks());

        esewaRefundRequestDTO.setEsewa_id(esewaId);

        String encryptedRequestBody = convertObjectToEncryptedEsewaRequestBody(esewaRefundRequestDTO);

        integrationApiInfo.setApiUri(parseApiUri(integrationApiInfo.getApiUri(), transactionDetail.getTransactionNumber()));

        Map<String, Object> map = new HashMap<>();

        map.put(DATA, encryptedRequestBody);

        ResponseEntity<?> responseEntity = thirdPartyConnectorService.callEsewaRefundService(integrationApiInfo,
                map);

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
        }


        return thirdPartyResponse;
    }

    private String getEsewaId(Long appointmentId) {

        return appointmentEsewaRequestRepository.fetchEsewaIdByAppointmentId(appointmentId).
                orElseThrow(() -> ESEWA_ID_NOT_FOUND.get());

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
                    throw new BadRequestException(INVALID_INTEGRATION_CHANNEL_CODE,
                            refundRequestDTO.getIntegrationChannelCode());
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

                saveAppointmentRefundDetail(parseRefundRejectDetails(refundRequestDTO,
                        refundAppointmentDetail));

            } else {

                ThirdPartyResponse response = processRefundRequest(refundRequestDTO,
                        appointment,
                        appointmentTransactionDetail,
                        refundAppointmentDetail,
                        false);

                if (!Objects.isNull(response.getCode())) {
                    throw new BadRequestException(response.getMessage());
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

                saveAppointmentRefundDetail(parseRefundRejectDetails(refundRequestDTO,
                        refundAppointmentDetail));

            } else {

                updateAppointmentAndAppointmentRefundDetails(refundRequestDTO.getStatus(),
                        refundRequestDTO.getRemarks(),
                        appointment,
                        refundAppointmentDetail,
                        refundRequestDTO);

            }

        }


    }

    @Override
    public void apiIntegrationCheckpointForRefundStatus(Appointment appointment,
                                                        AppointmentRefundDetail appointmentRefundDetail,
                                                        AppointmentTransactionDetail appointmentTransactionDetail,
                                                        RefundStatusRequestDTO refundRequestDTO) {

        if (refundRequestDTO.getIntegrationChannelCode() == null) {
            throw new BadRequestException(INTEGRATION_CHANNEL_CODE_IS_NULL);
        }

        IntegrationRefundRequestDTO integrationRefundRequestDTO = IntegrationRefundRequestDTO.builder()
                .featureCode(refundRequestDTO.getFeatureCode())
                .integrationChannelCode(refundRequestDTO.getIntegrationChannelCode())
                .appointmentModeId(refundRequestDTO.getAppointmentModeId())
                .appointmentId(refundRequestDTO.getAppointmentId())
                .status(refundRequestDTO.getStatus())
                .remarks(refundRequestDTO.getRemarks())
                .build();


        if (refundRequestDTO.getIntegrationChannelCode().equalsIgnoreCase(BACK_END_CODE)) {

            //condition to check transaction number for follow up case.
            //for free follow up case we don't have to hit third party API for refund and its status is changed to REFUNDED in database.
            //Both FrontEnd Refund Remarks and Esewa Remarks are saved into Appointment & RefundAppointmentDetail Tables Respectively.
            if (appointmentTransactionDetail.getTransactionNumber().equals(null) ||
                    appointmentTransactionDetail.getTransactionNumber().equalsIgnoreCase("N/A")) {

                changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                        appointmentRefundDetail,
                        refundRequestDTO.getRemarks(), null);

            } else {

                ThirdPartyResponse thirdPartyResponse = processRefundStatusRequest(integrationRefundRequestDTO,
                        appointment,
                        appointmentTransactionDetail);

                updateRefundStatusDetails(appointment,
                        appointmentRefundDetail,
                        appointmentTransactionDetail,
                        refundRequestDTO,
                        integrationRefundRequestDTO,
                        thirdPartyResponse.getStatus());

            }

        }

        if (refundRequestDTO.getIntegrationChannelCode().equalsIgnoreCase(FRONT_END_CODE)) {

            //condition to check transaction number for follow up case.
            //for free follow up case we don't have to hit third party API for refund and its status is changed to REFUNDED in database.
            //Both FrontEnd Refund Remarks and Esewa Remarks are saved into Appointment & RefundAppointmentDetail Tables Respectively.
            if (appointmentTransactionDetail.getTransactionNumber().equals(null) ||
                    appointmentTransactionDetail.getTransactionNumber().equalsIgnoreCase("N/A")) {

                changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                        appointmentRefundDetail,
                        refundRequestDTO.getRemarks(), null);

            } else {

                updateRefundStatusDetails(appointment,
                        appointmentRefundDetail,
                        appointmentTransactionDetail,
                        refundRequestDTO,
                        integrationRefundRequestDTO,
                        refundRequestDTO.getStatus());

            }
        }


    }


    private ThirdPartyResponse processRefundStatusRequest(IntegrationRefundRequestDTO integrationRefundRequestDTO,
                                                          Appointment appointment,
                                                          AppointmentTransactionDetail transactionDetail) {


        ThirdPartyResponse thirdPartyResponse = null;

        switch (appointment.getAppointmentModeId().getCode()) {

            case APPOINTMENT_MODE_ESEWA_CODE:
                thirdPartyResponse = requestEsewaForRefundStatus(integrationRefundRequestDTO,
                        appointment,
                        transactionDetail);
                break;

            default:
                throw new BadRequestException(APPOINTMENT_MODE_NOT_VALID);
        }


        return thirdPartyResponse;
    }

    private ThirdPartyResponse requestEsewaForRefundStatus(IntegrationRefundRequestDTO refundRequestDTO,
                                                           Appointment appointment,
                                                           AppointmentTransactionDetail transactionDetail) {


        IntegrationBackendRequestDTO integrationBackendRequestDTO = IntegrationBackendRequestDTO.builder()
                .featureCode(refundRequestDTO.getFeatureCode())
                .integrationChannelCode(refundRequestDTO.getIntegrationChannelCode())
                .hospitalId(refundRequestDTO.getHospitalId())
                .appointmentId(refundRequestDTO.getAppointmentId())
                .build();

        String esewaId = getEsewaId(appointment.getId());

        String generatedEsewaHmac = getSigatureForEsewa.apply(esewaId,
                appointment.getHospitalId().getEsewaMerchantCode());

        BackendIntegrationApiInfo integrationApiInfo = getAppointmentModeApiIntegration(integrationBackendRequestDTO,
                appointment.getAppointmentModeId().getId(),
                generatedEsewaHmac);

        if (!Objects.isNull(integrationApiInfo)) {

            EsewaPaymentStatus esewaPaymentStatus = getEsewaPayementStatusRequestBody(esewaId,
                    appointment.getHospitalId().getEsewaMerchantCode(),
                    transactionDetail.getTransactionNumber());

            integrationApiInfo.setApiUri(parseApiUri(integrationApiInfo.getApiUri(),
                    transactionDetail.getTransactionNumber()));

            String encryptedRequestBody = convertObjectToEncryptedEsewaRequestBody(esewaPaymentStatus);

            Map<String, Object> map = new HashMap<>();

            map.put(DATA, encryptedRequestBody);

            ResponseEntity<?> responseEntity = thirdPartyConnectorService.
                    callEsewaRefundStatusService(integrationApiInfo,
                            map);

            if (responseEntity.getBody() == null) {
                throw new OperationUnsuccessfulException(THIRD_PARTY_API_RESPONSE_IS_NULL);
            }


            ThirdPartyResponse thirdPartyResponse = null;
            try {
                thirdPartyResponse = map(responseEntity.getBody().toString(),
                        ThirdPartyResponse.class);
            } catch (IOException e)

            {
                e.printStackTrace();
                throw new OperationUnsuccessfulException(THIRD_PARTY_API_RESPONSE_IS_NULL);
            }

            if (thirdPartyResponse.getCode() != null) {
//                validateThirdPartyException(thirdPartyResponse);
            }


            return thirdPartyResponse;

        } else {
            return new ThirdPartyResponse("400", THIRD_PARTY_API_INFORMATION_NOT_FOUND,
                    THIRD_PARTY_API_INFORMATION_NOT_FOUND);
        }


    }


    private void updateRefundStatusDetails(Appointment appointment,
                                           AppointmentRefundDetail appointmentRefundDetail,
                                           AppointmentTransactionDetail appointmentTransactionDetail,
                                           RefundStatusRequestDTO refundStatusRequestDTO,
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


            case COMPLETE:
                IntegrationRefundRequestDTO integrationRefundRequestDTO=IntegrationRefundRequestDTO.builder()
                        .featureCode("REFUND")
                        .integrationChannelCode("BACK")
                        .appointmentId(appointment.getId())
                        .appointmentModeId(appointment.getAppointmentModeId().getId())
                        .build();

                apiIntegrationCheckpointForRefundAppointment(appointment,
                        appointmentTransactionDetail,
                        appointmentRefundDetail,
                        integrationRefundRequestDTO);
                break;

            case AMBIGIOUS:
                throw new BadRequestException("Communicate with Esewa");


            case PENDING:
                throw new BadRequestException("Communicate with Esewa");

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

                //esewa integration
                thirdPartyResponse = processEsewaRefundRequest(appointment,
                        transactionDetail,
                        appointmentRefundDetail,
                        isRefund, integrationRefundRequestDTO);
                break;
            case APPOINTMENT_MODE_FONEPAY_CODE:
                break;

            default:
                throw new BadRequestException(INVALID_APPOINTMENT_MODE);
        }


        return thirdPartyResponse;
    }

    private void updateAppointmentAndAppointmentRefundDetails(String response,
                                                              String frontEndRemarks,
                                                              Appointment appointment,
                                                              AppointmentRefundDetail refundAppointmentDetail,
                                                              IntegrationRefundRequestDTO integrationRefundRequestDTO) {

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
                saveAppointmentRefundDetail(parseRefundRejectDetails(integrationRefundRequestDTO,
                        refundAppointmentDetail));
                break;

            case AMBIGIOUS:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment,
                        refundAppointmentDetail,
                        response);
                throw new BadRequestException(response, response);

            default:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment,
                        refundAppointmentDetail,
                        response);
                throw new BadRequestException(response, response);
        }

    }

    private void changeAppointmentAndAppointmentRefundDetailStatus(Appointment appointment,
                                                                   AppointmentRefundDetail refundAppointmentDetail,
                                                                   String frontEndRemarks, String eSewaRemarks) {

        save(changeAppointmentStatus.apply(appointment, frontEndRemarks));

        saveAppointmentRefundDetail(changeAppointmentRefundDetailStatus.apply(refundAppointmentDetail, eSewaRemarks));
    }

    private void defaultAppointmentAndAppointmentRefundDetailStatusChanges(Appointment appointment,
                                                                           AppointmentRefundDetail refundAppointmentDetail,
                                                                           String remarks) {

        save(defaultAppointmentStatusChange.apply(appointment, remarks));

        saveAppointmentRefundDetail(defaultAppointmentRefundDetailStatusChange.apply(refundAppointmentDetail, remarks));

    }

    private void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    private void saveAppointmentRefundDetail(AppointmentRefundDetail appointmentRefundDetail) {
        appointmentRefundDetailRepository.save(appointmentRefundDetail);
    }

    private AppointmentTransactionDetail fetchAppointmentTransactionDetail(Long appointmentId) {
        return appointmentTransactionDetailRepository.fetchByAppointmentId(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
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
                    appointmentRepository.fetchAppointmentDetailForHospitalDeptCheckIn(requestDTO.getAppointmentId());

            thirdPartyCheckInDetails.setName(generateRandomNumber(3));
            thirdPartyCheckInDetails.setSex(toNormalCase(thirdPartyCheckInDetails.getGender().name()));

            //todo : room no and dept name is static in bheri api
            thirdPartyCheckInDetails.setSection("ENT");

            ResponseEntity<?> responseEntity =
                    thirdPartyConnectorService.callThirdPartyHospitalDepartmentAppointmentCheckInService(backendIntegrationApiInfo,
                            thirdPartyCheckInDetails);

            return fetchThirdPartyHospitalResponse(responseEntity);
        } else {
            return new ThirdPartyHospitalResponse("400", "Third party hospital information Not found",
                    "Third party hospital information Not found");
        }
    }

    private BackendIntegrationApiInfo getBackendIntegrationApiInfo(
            IntegrationBackendRequestDTO integrationBackendRequestDTO) {

        ClientFeatureIntegrationResponse featureIntegrationResponse = integrationRepository.
                fetchClientIntegrationResponseDTOForBackendIntegration(integrationBackendRequestDTO);

        if (!Objects.isNull(featureIntegrationResponse)) {

            Map<String, String> requestHeaderResponse = integrationRepository.
                    findApiRequestHeadersResponse(featureIntegrationResponse.getApiIntegrationFormatId());

            Map<String, String> queryParametersResponse = integrationRepository.
                    findApiQueryParametersResponse(featureIntegrationResponse.getApiIntegrationFormatId());

            BackendIntegrationApiInfo backendIntegrationApiInfo = new BackendIntegrationApiInfo();

            backendIntegrationApiInfo.setApiUri(featureIntegrationResponse.getUrl());
            backendIntegrationApiInfo.setHttpHeaders(generateApiHeaders(requestHeaderResponse, null));

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
            thirdPartyHospitalResponse = ObjectMapperUtils.map((String) responseEntity.getBody(),
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

    private void updateHospitalPatientInfo(Appointment appointment, String hospitalNumber) {

        HospitalPatientInfo hospitalPatientInfo = fetchHospitalPatientInfo(
                appointment.getPatientId().getId(), appointment.getHospitalId().getId());

        hospitalPatientInfo.setHospitalNumber(hospitalNumber);
    }

    private HospitalPatientInfo fetchHospitalPatientInfo(Long patientId, Long hospitalId) {
        return hospitalPatientInfoRepository.findByPatientAndHospitalId(patientId, hospitalId)
                .orElseThrow(() -> HOSPITAL_PATIENT_INFO_NOT_FOUND.apply(patientId));
    }

    private Function<Long, NoContentFoundException> HOSPITAL_PATIENT_INFO_NOT_FOUND = (patientId) -> {
        throw new NoContentFoundException(HospitalPatientInfo.class, "patientId", patientId.toString());
    };

    private List<String> getRequestBodyByFeature(Long featureId, String requestMethod) {

        List<String> requestBody = null;
        if (requestMethod.equalsIgnoreCase("POST")) {
            List<IntegrationRequestBodyAttributeResponse> responses = integrationRepository.
                    fetchRequestBodyAttributeByFeatureId(featureId);

            if (responses != null) {
                requestBody = responses.stream()
                        .map(request -> request.getName())
                        .collect(Collectors.toList());
            }

        }

        return requestBody;
    }

    private String convertObjectToEncryptedEsewaRequestBody(Object obj) {
        return generateToken(obj);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, appointmentId);
        throw new NoContentFoundException(Appointment.class, "appointmentId", appointmentId.toString());
    };

    private Supplier<NoContentFoundException> ESEWA_ID_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, "eSewa Id");
        throw new NoContentFoundException("eSewa Id not Found", "eSewa Id not Found");
    };

}

package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.EsewaPayementStatus;
import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.EsewaResponseDTO;
import com.cogent.cogentappointment.client.utils.resttempalte.RestTemplateUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import static com.cogent.cogentappointment.client.utils.RefundStatusUtils.parseToEsewaPayementStatus;
import static com.cogent.cogentappointment.client.utils.resttempalte.IntegrationRequestHeaders.getEsewaPaymentStatusAPIHeaders;
import static com.cogent.cogentappointment.client.utils.resttempalte.IntegrationRequestURI.ESEWA_API_PAYMENT_STATUS;

/**
 * @author Sauravi Thapa ON 5/26/20
 */
public class esewaIntegrationUtils {

    private final RestTemplateUtils restTemplateUtils;

    public esewaIntegrationUtils(RestTemplateUtils restTemplateUtils) {
        this.restTemplateUtils = restTemplateUtils;
    }

    private String requestEsewaApi(RefundStatusRequestDTO requestDTO){
        EsewaPayementStatus esewaPayementStatus=parseToEsewaPayementStatus(requestDTO);

        HttpEntity<?> request = new HttpEntity<>(esewaPayementStatus, getEsewaPaymentStatusAPIHeaders());

        ResponseEntity<EsewaResponseDTO> response = (ResponseEntity<EsewaResponseDTO>) restTemplateUtils.
                postRequest(ESEWA_API_PAYMENT_STATUS,
                        request,EsewaResponseDTO.class);

        return response.getBody().getStatus();
    }
}

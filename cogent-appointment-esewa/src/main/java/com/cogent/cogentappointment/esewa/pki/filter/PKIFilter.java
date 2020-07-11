package com.cogent.cogentappointment.esewa.pki.filter;

import com.cogent.cogentappointment.esewa.dto.response.pki.PKIErrorResponseDTO;
import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.cogent.cogentappointment.esewa.pki.PKIData;
import com.cogent.cogentappointment.esewa.pki.utils.JacksonUtil;
import com.cogent.cogentappointment.esewa.pki.wrapper.BufferedServletResponseWrapper;
import com.cogent.cogentappointment.esewa.pki.wrapper.DataWrapper;
import com.cogent.cogentappointment.esewa.pki.wrapper.RequestWrapper;
import com.cogent.cogentappointment.esewa.service.PKIAuthenticationInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.server.ExportException;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.PKIMessages.MISSING_PAYLOAD;
import static com.cogent.cogentappointment.esewa.pki.utils.SecurityUtil.encryptPayloadAndGenerateSignature;
import static com.cogent.cogentappointment.esewa.pki.utils.SecurityUtil.responseValidator;

/**
 * @author smriti on 06/07/20
 */
@Slf4j
@Component
public class PKIFilter extends OncePerRequestFilter implements javax.servlet.Filter {

    private final PKIAuthenticationInfoService pkiAuthenticationInfoService;

    private final DataWrapper dataWrapper;

    public PKIFilter(PKIAuthenticationInfoService pkiAuthenticationInfoService,
                     DataWrapper dataWrapper) {
        this.pkiAuthenticationInfoService = pkiAuthenticationInfoService;
        this.dataWrapper = dataWrapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        BufferedServletResponseWrapper customResponse = new BufferedServletResponseWrapper(response);

        String serverPrivateKey = pkiAuthenticationInfoService.findServerPrivateKeyByClientId("eSewa");

        try (BufferedReader reader = request.getReader()) {
            String encryptedPayloadData = this.getPayloadData(reader);

            RequestWrapper requestWrapper = JacksonUtil.get(encryptedPayloadData, RequestWrapper.class);

            String clientPublicKey = pkiAuthenticationInfoService.findClientPublicKeyByClientId(
                    requestWrapper.getClient_id());

            String decryptedData = responseValidator(requestWrapper, clientPublicKey, serverPrivateKey);

            dataWrapper.setData(decryptedData);

            filterChain.doFilter(request, response);

            if (!ObjectUtils.isEmpty(customResponse.getResponseData())) {
//                uri.startsWith(WebRoutes.CLIENT_REQUEST + "/")) {
                encryptResponse(response, customResponse, requestWrapper);
            }
        } catch (Exception e) {
            log.error("Error occurred while validating encrypted request :: {}", e.getMessage());
            handleFilterException(response, e);
        }
    }

    private void handleFilterException(HttpServletResponse httpServletResponse,
                                       Exception exception) throws IOException {

        PKIErrorResponseDTO errorResponseDTO = PKIErrorResponseDTO.builder()
                .status(400)
                .errorMessage(exception.getMessage())
                .build();

        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(errorResponseDTO);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        assert json != null;
        httpServletResponse.getWriter().write(json);
        httpServletResponse.setStatus(errorResponseDTO.getStatus());
        httpServletResponse.flushBuffer();
    }

    private void encryptResponse(HttpServletResponse httpServletResponse,
                                 BufferedServletResponseWrapper customResponse,
                                 RequestWrapper requestWrapper) throws IOException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String clientPublicKey = pkiAuthenticationInfoService.findClientPublicKeyByClientId(
                    requestWrapper.getClient_id());

            PKIData pkiData = encryptData(customResponse.getResponseData(), clientPublicKey);

            OutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(objectMapper.writeValueAsString(pkiData).getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (ExportException e) {
            log.error("throw new error occurred while preparing encrypted response payload {}", e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    private PKIData encryptData(String payload, String clientPublicKey) {
        log.info("Encrypting data with payload:{}", payload);

        String serverPrivateKey = pkiAuthenticationInfoService.findServerPrivateKeyByClientId("eSewa");

        return encryptPayloadAndGenerateSignature(payload, clientPublicKey, serverPrivateKey);
    }

    private String getPayloadData(BufferedReader reader) throws IOException {
        final StringBuilder builder = new StringBuilder();
        if (reader == null) {
            log.error("Request body is null");
            throw new BadRequestException(MISSING_PAYLOAD);
        }
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    @Override
    public void destroy() {
    }

}

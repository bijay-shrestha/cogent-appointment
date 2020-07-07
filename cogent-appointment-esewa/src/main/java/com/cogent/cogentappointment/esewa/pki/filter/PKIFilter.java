package com.cogent.cogentappointment.esewa.pki.filter;

import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.cogent.cogentappointment.esewa.pki.wrapper.BufferedServletResponseWrapper;
import com.cogent.cogentappointment.esewa.pki.PKIData;
import com.cogent.cogentappointment.esewa.pki.utils.JacksonUtil;
import com.cogent.cogentappointment.esewa.pki.utils.SecurityUtil;
import com.cogent.cogentappointment.esewa.pki.wrapper.DataWrapper;
import com.cogent.cogentappointment.esewa.pki.wrapper.RequestWrapper;
import com.cogent.cogentappointment.esewa.service.PKIAuthenticationInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author smriti on 06/07/20
 */
@Slf4j
@Component
public class PKIFilter extends SecurityUtil implements javax.servlet.Filter {

    private final PKIAuthenticationInfoService pkiAuthenticationInfoService;

    private final DataWrapper dataWrapper;

    public PKIFilter(PKIAuthenticationInfoService pkiAuthenticationInfoService,
                     DataWrapper dataWrapper) {
        this.pkiAuthenticationInfoService = pkiAuthenticationInfoService;
        this.dataWrapper = dataWrapper;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        BufferedServletResponseWrapper customResponse = new BufferedServletResponseWrapper(httpServletResponse);

        RequestWrapper requestWrapper = new RequestWrapper();
//        String uri = httpServletRequest.getRequestURI();

//        if (uri.startsWith(WebRoutes.CLIENT_REQUEST + "/")) {
        String serverPrivateKey = pkiAuthenticationInfoService.findServerPrivateKeyByClientId("eSewa");

        try (BufferedReader reader = request.getReader()) {
            String encryptedPayloadData = this.getPayloadData(reader);

            requestWrapper = JacksonUtil.get(encryptedPayloadData, RequestWrapper.class);

            String clientPublicKey = pkiAuthenticationInfoService.findClientPublicKeyByClientId(
                    requestWrapper.getClient_id());

            String decryptedData = responseValidator(encryptedPayloadData, clientPublicKey, serverPrivateKey);

            dataWrapper.setData(decryptedData);
        } catch (Exception e) {
            log.error("Error occurred while validating encrypted request :: {}", e.getMessage());
            handleFilterException(httpServletResponse);
        }

//        if (uri.startsWith(WebRoutes.CLIENT_REQUEST + "/")) {
//            chain.doFilter(request, customResponse);
//        } else {
//            chain.doFilter(request, response);
//        }

        chain.doFilter(request, response);

        if (!ObjectUtils.isEmpty(customResponse.getResponseData())) {
//                uri.startsWith(WebRoutes.CLIENT_REQUEST + "/")) {
            encryptResponse(httpServletResponse, customResponse, requestWrapper);
        }
    }

    private void handleFilterException(HttpServletResponse httpServletResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        httpServletResponse.setContentType("application/json");
//        PrintWriter out = httpServletResponse.getWriter();


        //todo: exception
//        out.print(mapper.writeValueAsString(ClientPaymentExceptionResource.builder()
//                .code(ClientResponse.INVALID_PAYLOAD_SIGNATURE.getCode())
//                .message(ClientResponse.INVALID_PAYLOAD_SIGNATURE.getValue())
//                .build()));
//        out.flush();
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
        } catch (BadRequestException e) {
            log.error("throw new error occurred while preparing encrypted response payload {}", e.getMessage());
//         CoreClientException(ClientResponse.SYSTEM_ERROR.getCode(), ClientResponse.SYSTEM_ERROR.getValue());
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

            //todo: exception

            return null;
//            throw new CoreClientException(ClientResponse.MISSING_PAYLOAD.getCode(), ClientResponse.MISSING_PAYLOAD.getValue());
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

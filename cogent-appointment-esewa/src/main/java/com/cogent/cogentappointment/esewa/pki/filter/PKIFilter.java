package com.cogent.cogentappointment.esewa.pki.filter;

import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.cogent.cogentappointment.esewa.pki.BufferedServletResponseWrapper;
import com.cogent.cogentappointment.esewa.pki.PKIData;
import com.cogent.cogentappointment.esewa.pki.utils.JacksonUtil;
import com.cogent.cogentappointment.esewa.pki.utils.SecurityUtil;
import com.cogent.cogentappointment.esewa.pki.wrapper.DataWrapper;
import com.cogent.cogentappointment.esewa.pki.wrapper.RequestWrapper;
import com.cogent.cogentappointment.esewa.repository.PKIAuthenticationInfoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author smriti on 06/07/20
 */
@Slf4j
@Component
public class PKIFilter extends SecurityUtil implements javax.servlet.Filter {

    private final PKIAuthenticationInfoRepository pkiAuthenticationInfoRepository;

    private final DataWrapper dataWrapper;

    @Autowired
    public PKIFilter(PKIAuthenticationInfoRepository pkiAuthenticationInfoRepository,
                     DataWrapper dataWrapper) {
        this.pkiAuthenticationInfoRepository = pkiAuthenticationInfoRepository;
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
//        String privateKey = pkiAuthenticationInfoRepository.findServerPrivateKeyByClientId("eSewa");

        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCXTAHogaDLOgn3GRyxLZTbAmoNMGX8XKgelU3EFt8Ly3AvMGhC+9IdR94ceky/VctyGizB6BcGywi8VUW1sa0QJjltzxGwUeTB3V0U1hrB4a+NVCyUkwV6yviYfjK0G8kGY1UXYez4bwESzN4tBNF04pvDsUTZy6ja8i6FUKZAurSiweRm2FpgLTn34ipnpVxM092UZyKnFW8X9mNm2Nfz1xN0GUV8GPNWYhssNKNu/FBjpaTUXlrB5F2fMamg66h+WQ6hKE621BlrR98cRX7zq89mel7xxYPF3+KgOy5E1OpdbGX4SVO7gxrZBkux7gsu57e2P7XFbFFyx6bxHiPRAgMBAAECggEAcNJ+LcrUhBfwrHHugnUyJqtDODiaJLlXqQ6/YfWIOHxpWNcpOKIeijU4fVX5+0hYIOtB6wtOeINZLVANXrNzEbLfanJah3haNPME4W/TnjbUuXhGkjicgnfvL5AT8Vky6++Q2ZHtq0jjrQhWuY15QEdnzmNXq24CqdqlNEby4xrtjYnwruj3taClJMVpibjQ26syCWgpKGMqdWKEfry2Q+Xy6GdY2DRruh6YQthC1iIQOS23FqJeBrbo4+5gqDzjO5/yTBsGx3E9VS6eQM8sBYXcJFt9TEpreYMuuncpGeyi3veeoLNmaxZK3LG/IIDm6a0miS8I7CQ8xv6Jep90AQKBgQDpx8FrcrTpJgXBqYpUoSN56iE8E24EuXA43C5FfsTr12umqykamKo463tZPw7vvSTNl5ydlg7Jr7tAq9i3L+IPEV7/r3lZIEAOk0nBcTY5FIGs6refDTwx8l/0LTKqbKrdCD5jAvjGiXLF1yWVKN9Yny5Y5ePOK6C5zDE55FAdEQKBgQClrUzt43owsZRyxbBtsKlw1oltmDLYVGMlwEIIfxH2kus8GTaltR1AmTogxLMkdQP0s8A8Wj0qmBKEaE5uRBsdGQBAHDzOrQDWXqHmH92oJhIRCfCo4HpsVmPz2LwssdZPCzZv1DlrLkyKhuPncaMMdS/H1EY1CY7zAVAe23yawQKBgQDWcy1UuVaHASAKtNF2LJL0hPeTumcT4l+1aRTxHwbZKTVRIHWGNkEAEdOG3LeA58rY5Zj/XeWW0aM+AeW8tSnzlIXGmlsAjPr149qmnomU9uC1lGJ4fpWXY7TtsoBloWVjeOpxvQokZXVpUqDhISswimTjm47LU24OwebuKifrYQKBgBI+mFcmEsGj/JX7ASfDKZWcenvQI+FAwb5ZgqwO2jqOCUuP9z2eST9g4E7VemjMXggnd0buJQg4wOlF10U7SMUWiLmGooeb85inySpfXfhzYM/xiUf/mFuv08f5mRdO6ivAL1l3RG9yJMmoexZ0pCDuErntvWF/0PcfsOQFBZ7BAoGATSOgJJ3fN1wRMGLgMluR/wQHBbtVzBj5RG9tcAxrdPbKW7AA1wmUsmsxBPiXD1CZ+K5n2I2U62lg1bl5WdUaYPj35+m7mbnAC8j2lJ3qOjBfvEScvzus3WcXF5LgTITFkTrS4bguQf5+NQ8Vx0AuPqyiZchnBcVP7yJjCiHjmnI=";


        try (BufferedReader reader = request.getReader()) {
            String encryptedPayloadData = this.getPayloadData(reader);

            requestWrapper = JacksonUtil.get(encryptedPayloadData, RequestWrapper.class);

            System.out.println("test-------------1");
            String publicKey =
                    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCF+ropsUEGSM9tP6ozpKb8hjHsaMeyOwRfqMjEn9Pi1+MkrjYMEuPMCi8cdbqRuJGXMU9RphK6tL87+xpNMsE2ralmpXKsy/+4xdrQCUzFRMiiaC9M4a/qYJEie3WdZ48T+gBxxuDlOJe09vOxsGjAO/HpHAq/PbKgQjnkbdlJ5wIDAQAB";
//
//                    pkiAuthenticationInfoRepository.findClientPublicKeyByClientId(
//                    requestWrapper.getClient_id());

            String decryptedData = responseValidator(encryptedPayloadData, publicKey, privateKey);

            dataWrapper.setData(decryptedData);
        } catch (Exception e) {
            log.error("Error occurred while validating encrypted request :: {}", e.getMessage());
            handleFilterException(httpServletResponse);
        }
//        }

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
        PrintWriter out = httpServletResponse.getWriter();


        //todo: exception
//        out.print(mapper.writeValueAsString(ClientPaymentExceptionResource.builder()
//                .code(ClientResponse.INVALID_PAYLOAD_SIGNATURE.getCode())
//                .message(ClientResponse.INVALID_PAYLOAD_SIGNATURE.getValue())
//                .build()));
        out.flush();
    }

    private void encryptResponse(HttpServletResponse httpServletResponse, BufferedServletResponseWrapper customResponse,
                                 RequestWrapper requestWrapper) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String publicKey = pkiAuthenticationInfoRepository.findClientPublicKeyByClientId(requestWrapper.getClient_id());

            PKIData pkiData = encryptData(customResponse.getResponseData(), publicKey);
            OutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(objectMapper.writeValueAsString(pkiData).getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (BadRequestException e) {

            log.error("throw new rror occurred while preparing encrypted response payload {}", e.getMessage());
//         CoreClientException(ClientResponse.SYSTEM_ERROR.getCode(), ClientResponse.SYSTEM_ERROR.getValue());
        }
    }

    private PKIData encryptData(String payload, String clientPublicKey) {
        log.info("Encrypting data with payload:{}", payload);

        String privateKey = pkiAuthenticationInfoRepository.findServerPrivateKeyByClientId("eSewa");

        return encryptPayloadAndGenerateSignature(payload, clientPublicKey, privateKey);
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

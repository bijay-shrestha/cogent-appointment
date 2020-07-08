package com.cogent.cogentappointment.esewa.security.filter;

import com.cogent.cogentappointment.esewa.dto.request.DataWrapperRequest;
import com.cogent.cogentappointment.esewa.dto.request.EsewaRequestDTO;
import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.cogent.cogentappointment.esewa.resource.v1.AppointmentResource;
import com.cogent.cogentappointment.esewa.utils.commons.ObjectMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import static com.cogent.cogentappointment.esewa.utils.JWTDecryptUtils.toDecrypt;

/**
 * @author Sauravi Thapa ON 7/7/20
 */
@Slf4j
@Component
public class JwtRequestFilter implements Filter {

    public static final String DATA = "data";

    private final DataWrapperRequest dataWrapperRequest;

    public JwtRequestFilter(DataWrapperRequest dataWrapperRequest) {
        this.dataWrapperRequest = dataWrapperRequest;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException,
            ServletException {


        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String uri = httpServletRequest.getRequestURI();

        EsewaRequestDTO esewaRequestDTO = null;


//        String SKIP_URL = "/test";


        List<String> WHITE_LIST = Arrays.asList("/esewa/api/v2/test", "/esewa/api/v1");

        boolean found = WHITE_LIST.stream().anyMatch(test -> test.contains(uri));

        if (!found) {
            if (uri.contains("/esewa/api/v2")) {
                try (BufferedReader reader = request.getReader()) {

                    String encryptedPayloadData = this.getPayloadData(reader);

                    System.out.println(encryptedPayloadData);

                    esewaRequestDTO = ObjectMapperUtils.map(encryptedPayloadData, EsewaRequestDTO.class);

                    Map<String, String> map = new HashMap<>();
                    map.put("data", esewaRequestDTO.getData().toString());

                    Object decryptedData = toDecrypt(map);
                    System.out.println(decryptedData);

                    dataWrapperRequest.setData(decryptedData);

                } catch (Exception e) {
                    log.error("Error occurred while validating encrypted request :: {}", e.getMessage());
                }

            }

        }

        chain.doFilter(request, response);


    }

    @Override
    public void destroy() {

    }

    private String getPayloadData(BufferedReader reader) throws IOException {
        final StringBuilder builder = new StringBuilder();
        if (reader == null) {
            log.error("Request body is null");
            throw new BadRequestException("Request body is null");
        }
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

}

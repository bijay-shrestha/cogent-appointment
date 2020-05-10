package com.cogent.cogentappointment.client.loghandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.LOGIN;
import static com.cogent.cogentappointment.client.loghandler.RequestCheckpoint.URL_TO_LOG;
import static java.util.Arrays.asList;

@ControllerAdvice
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {

        String uri = httpServletRequest.getServletPath();
        if (asList(URL_TO_LOG).contains(uri)) {

            if (httpServletRequest.getServletPath().contains(LOGIN) && httpServletRequest.getMethod().equalsIgnoreCase("POST")) {

                String jsonString = "";
                ObjectMapper mapper = new ObjectMapper();
                //Converting the Object to JSONString
                try {
                    jsonString = mapper.writeValueAsString(body);
                    System.out.println(jsonString);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                httpServletRequest.setAttribute("loginRequest", jsonString);

            }

        }


        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
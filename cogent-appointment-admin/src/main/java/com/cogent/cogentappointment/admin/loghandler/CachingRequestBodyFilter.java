package com.cogent.cogentappointment.admin.loghandler;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CachingRequestBodyFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        chain.doFilter(requestWrapper, responseWrapper);
        responseWrapper.copyBodyToResponse();

//        try {
            chain.doFilter(requestWrapper, responseWrapper);
            responseWrapper.copyBodyToResponse();

//        } finally {
//            String requestBody = new String(requestWrapper.getContentAsByteArray());
//            System.out.println(requestBody);
//
//            String responseBody = new String(responseWrapper.getContentAsByteArray());
//            System.out.println(responseBody);

            // Do not forget this line after reading response content or actual response will be empty!
//        }

//        }

    }
}
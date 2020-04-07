//package com.cogent.cogentappointment.admin.configuration;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @author Rupak
// */
//@Component
//public class MyLogInterceptor implements HandlerInterceptor {
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        System.out.println();
//
//
//    }
//    @Override
//    public boolean postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//
//    }
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
//                                Exception exception) throws Exception {
//        long renderingStartTime = (Long)request.getAttribute("rendering-start-time");
//        long renderingEndTime = System.currentTimeMillis();
//        long renderingDuration = renderingEndTime - renderingStartTime;
//        ThreadContext.put("rendering-duration", renderingDuration);
//        logger.info("My interceptor handler message");
//        ThreadContext.clearMap();
//    }
//}
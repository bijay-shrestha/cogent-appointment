package com.cogent.cogentappointment.client.loghandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rupak
 */
public class RequestHeader {

    public static String getUserLogs(HttpServletRequest request) { return request.getHeader("log-header"); }

    public static String getUserAgent(HttpServletRequest request) { return request.getHeader("User-Agent"); }

    public static String getXForwardedFor(HttpServletRequest request) { return request.getHeader("X-Forwarded-For"); }

}

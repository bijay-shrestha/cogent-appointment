package com.cogent.cogentappointment.client.loghandler;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.cogent.cogentappointment.client.loghandler.RequestHeader.getUserAgent;
import static com.cogent.cogentappointment.client.loghandler.RequestHeader.getXForwardedFor;

public class RequestData {

    public static String getClientIpAddr(HttpServletRequest request) throws UnknownHostException {
        String ip = getXForwardedFor(request);

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();

            if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
                InetAddress inetAddress = InetAddress.getLocalHost();
                String ipAddress = inetAddress.getHostAddress();
                ip = ipAddress;
            }
        }


        return ip;
    }

    public static String getClientOS(HttpServletRequest request) {
        String browserDetails = getUserAgent(request);

        final String lowerCaseBrowser = browserDetails.toLowerCase();
        if (lowerCaseBrowser.contains("windows")) {
            return "Windows";
        } else if (lowerCaseBrowser.contains("mac")) {
            return "Mac";
        } else if (lowerCaseBrowser.contains("x11")) {
            return "Unix";
        } else if (lowerCaseBrowser.contains("android")) {
            return "Android";
        } else if (lowerCaseBrowser.contains("iphone")) {
            return "IPhone";
        } else {
            return "UnKnown, More-Info: " + browserDetails;
        }
    }

    public static String getClientBrowser(HttpServletRequest request) {

        String browserDetails = getUserAgent(request);
        String user = browserDetails.toLowerCase();

        String browser = "";
        if (user.contains("msie")) {
            String substring = browserDetails.substring(browserDetails.indexOf("MSIE")).split(";")[0];
            browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];

        } else if (user.contains("safari") && user.contains("version")) {
            browser = (browserDetails.substring(browserDetails.indexOf("Safari")).split(" ")[0]).split(
                    "/")[0] + "-" + (browserDetails.substring(
                    browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];

        } else if (user.contains("opr") || user.contains("opera")) {
            if (user.contains("opera"))
                browser = (browserDetails.substring(browserDetails.indexOf("Opera")).split(" ")[0]).split(
                        "/")[0] + "-" + (browserDetails.substring(
                        browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if (user.contains("opr"))
                browser = ((browserDetails.substring(browserDetails.indexOf("OPR")).split(" ")[0]).replace("/",
                        "-")).replace(
                        "OPR", "Opera");

        } else if (user.contains("chromium")) {
            browser = (browserDetails.substring(browserDetails.indexOf("Chromium")).split(" ")[0]).replace("/", "-");

        } else if (user.contains("edg") || user.contains("edge")) {

            if (user.contains("edge"))
                browser = (browserDetails.substring(browserDetails.indexOf("Edge")).split(" ")[0]).split(
                        "/")[0] + "-" + (browserDetails.substring(
                        browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if (user.contains("edg"))
                browser = ((browserDetails.substring(browserDetails.indexOf("Edg")).split(" ")[0]).replace("/",
                        "-")).replace(
                        "Edg", "Edge");

        } else if (user.contains("UCBrowser")) {
            browser = (browserDetails.substring(browserDetails.indexOf("UCBrowser")).split(" ")[0]).replace("/", "-");

        } else if (user.contains("chrome")) {
            browser = (browserDetails.substring(browserDetails.indexOf("Chrome")).split(" ")[0]).replace("/", "-");

        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1) || (user.indexOf(
                "mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf(
                "mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
            browser = "Netscape-?";

        } else if (user.contains("firefox")) {
            browser = (browserDetails.substring(browserDetails.indexOf("Firefox")).split(" ")[0]).replace("/", "-");

        } else if (user.contains("rv")) {
            browser = "IE";

        } else {
            browser = "N/A";
        }

        return browser;
    }


}

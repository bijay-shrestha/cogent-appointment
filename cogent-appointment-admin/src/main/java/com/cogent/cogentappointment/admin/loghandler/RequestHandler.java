package com.cogent.cogentappointment.admin.loghandler;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.utils.commons.ObjectMapperUtils;
import com.cogent.cogentappointment.admin.utils.commons.SecurityContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.splitByCharacterTypeCamelCase;

/**
 * @author Rupak
 */
public class RequestHandler {

    public static String getRemoteAddr(HttpServletRequest request) throws UnknownHostException {

        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            System.out.println("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }

        String remoteAddr = request.getRemoteAddr();

        if (remoteAddr.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            remoteAddr = ipAddress;
        }
        return remoteAddr;
    }

    public static AdminLogRequestDTO convertToAdminLogRequestDTO(String userLog) throws IOException {

        AdminLogRequestDTO adminLogRequestDTO = ObjectMapperUtils.map(userLog, AdminLogRequestDTO.class);
        adminLogRequestDTO.setAdminId(SecurityContextUtils.getLoggedInCompanyId());
        adminLogRequestDTO.setFeature(convertToNormalCase(splitByCharacterTypeCamelCase(adminLogRequestDTO.getFeature())));

        return adminLogRequestDTO;
    }
}

package com.cogent.cogentappointment.client.loghandler;
import com.cogent.cogentappointment.admin.utils.commons.SecurityContextUtils;
import com.cogent.cogentappointment.client.dto.commons.ClientLogRequestDTO;
import com.cogent.cogentappointment.client.utils.commons.ObjectMapperUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.cogent.cogentappointment.client.utils.commons.StringUtil.convertToNormalCase;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.splitByCharacterTypeCamelCase;

/**
 * @author Rupak
 */
public class RequestHandler {

    public static String getRemoteAddr(HttpServletRequest request) {

        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            System.out.println("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }

    public static ClientLogRequestDTO convertToClientLogRequestDTO(String userLog) throws IOException {

        ClientLogRequestDTO clientLogRequestDTO = ObjectMapperUtils.map(userLog, ClientLogRequestDTO.class);
        clientLogRequestDTO.setAdminId(SecurityContextUtils.getLoggedInCompanyId());
        clientLogRequestDTO.setFeature(convertToNormalCase(splitByCharacterTypeCamelCase(clientLogRequestDTO.getFeature())));

        return clientLogRequestDTO;
    }
}

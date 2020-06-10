package com.cogent.cogentappointment.admin.loghandler;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.utils.commons.FileResourceUtils;
import com.cogent.cogentappointment.admin.utils.commons.ObjectMapperUtils;
import com.cogent.cogentappointment.admin.utils.commons.SecurityContextUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import static com.cogent.cogentappointment.admin.utils.commons.SecurityContextUtils.getLoggedInAdminEmail;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.splitByCharacterTypeCamelCase;

/**
 * @author Rupak
 */
public class RequestHandler {

    public static AdminLogRequestDTO convertToAdminLogRequestDTO(String userLog, HttpServletRequest request) throws IOException, GeoIp2Exception {

        AdminLogRequestDTO adminLogRequestDTO = ObjectMapperUtils.map(userLog, AdminLogRequestDTO.class);

        adminLogRequestDTO.setAdminEmail(getLoggedInAdminEmail());
        adminLogRequestDTO.setFeature(convertToNormalCase(splitByCharacterTypeCamelCase(adminLogRequestDTO.getFeature())));

        getUserDetails(adminLogRequestDTO, request);

        return adminLogRequestDTO;
    }

    public static AdminLogRequestDTO getUserDetails(
            AdminLogRequestDTO adminLogRequestDTO,
            HttpServletRequest request

    ) throws IOException, GeoIp2Exception {

        String clientBrowser = RequestData.getClientBrowser(request);
        String clientOS = RequestData.getClientOS(request);
        String clientIpAddr = RequestData.getClientIpAddr(request);
        String location = location(clientIpAddr);

        adminLogRequestDTO.setLocation(location);
        adminLogRequestDTO.setBrowser(clientBrowser);
        adminLogRequestDTO.setOperatingSystem(clientOS);
        adminLogRequestDTO.setIpAddress(clientIpAddr);

        return adminLogRequestDTO;
    }

    public static AdminLogRequestDTO forgotPasswordLogging(HttpServletRequest request) throws IOException, GeoIp2Exception {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        String requestedEmail = new String(requestWrapper.getParameter("email"));

        AdminLogRequestDTO adminLogRequestDTO = AdminLogRequestDTO.
                builder()
                .feature("Forgot Password")
                .actionType("Forgot Password")
                .parentId(8001l)
                .roleId(3002l)
                .adminEmail(requestedEmail)
                .build();

        getUserDetails(adminLogRequestDTO, request);

        return adminLogRequestDTO;
    }

    public static AdminLogRequestDTO userLoginLogging(HttpServletRequest request, String email) throws IOException, GeoIp2Exception {

        AdminLogRequestDTO adminLogRequestDTO = AdminLogRequestDTO.
                builder()
                .feature("Login")
                .actionType("Login")
                .parentId(8002l)
                .roleId(3001l)
                .adminEmail(email)
                .build();

        getUserDetails(adminLogRequestDTO, request);

        return adminLogRequestDTO;
    }

    public static AdminLogRequestDTO userLogoutLogging(HttpServletRequest request) throws IOException, GeoIp2Exception {

        AdminLogRequestDTO adminLogRequestDTO = AdminLogRequestDTO.
                builder()
                .feature("Logout")
                .actionType("Logout")
                .parentId(8082l)
                .roleId(3003l)
                .adminEmail(getLoggedInAdminEmail())
                .build();

        getUserDetails(adminLogRequestDTO, request);

        return adminLogRequestDTO;
    }

    public static String location(String ip) throws IOException, GeoIp2Exception {

        String countryName = "";
        String cityName = "";
        String location = "";
        try {

            String name = "./location/GeoLite2-City.mmdb";
            File database = (new FileResourceUtils().convertResourcesFileIntoFile(name));
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);

            countryName = response.getCountry().getName();
            cityName = response.getCity().getName();

            if (cityName == null) {
                location = countryName;
            }

            if (cityName != null && countryName != null) {
                location = cityName + ", " + countryName;
            }

            return location;

        } catch (IOException | AddressNotFoundException e) {
            return "N/A";
        }


    }


}

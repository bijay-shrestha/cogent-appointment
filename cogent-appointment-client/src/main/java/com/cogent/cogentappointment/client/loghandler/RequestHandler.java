package com.cogent.cogentappointment.client.loghandler;

import com.cogent.cogentappointment.client.dto.commons.ClientLogRequestDTO;
import com.cogent.cogentappointment.client.utils.commons.FileResourceUtils;
import com.cogent.cogentappointment.client.utils.commons.ObjectMapperUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInAdminEmail;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.splitByCharacterTypeCamelCase;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;

/**
 * @author Rupak
 */
public class RequestHandler {

    public static ClientLogRequestDTO convertToClientLogRequestDTO(String userLog, HttpServletRequest request) throws IOException, GeoIp2Exception {

        ClientLogRequestDTO clientLogRequestDTO = ObjectMapperUtils.map(userLog, ClientLogRequestDTO.class);

        clientLogRequestDTO.setAdminEmail(getLoggedInAdminEmail());
        clientLogRequestDTO.setFeature(toNormalCase(splitByCharacterTypeCamelCase(clientLogRequestDTO.getFeature())));

        getUserDetails(clientLogRequestDTO, request);

        return clientLogRequestDTO;
    }

    public static ClientLogRequestDTO getUserDetails(ClientLogRequestDTO clientLogRequestDTO, HttpServletRequest request) throws IOException, GeoIp2Exception {

        String clientBrowser = RequestData.getClientBrowser(request);
        String clientOS = RequestData.getClientOS(request);
        String clientIpAddr = RequestData.getClientIpAddr(request);
        String location=location(clientIpAddr);

        clientLogRequestDTO.setLocation(location);
        clientLogRequestDTO.setBrowser(clientBrowser);
        clientLogRequestDTO.setOperatingSystem(clientOS);
        clientLogRequestDTO.setIpAddress(clientIpAddr);

        return clientLogRequestDTO;
    }

    public static ClientLogRequestDTO forgotPasswordLogging(HttpServletRequest request) throws IOException, GeoIp2Exception {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        String requestedEmail = new String(requestWrapper.getParameter("email"));

        ClientLogRequestDTO clientLogRequestDTO = ClientLogRequestDTO.
                builder()
                .feature("Forgot Password")
                .actionType("Forgot Password")
                .parentId(8001l)
                .roleId(3002l)
                .adminEmail(requestedEmail)
                .build();

        getUserDetails(clientLogRequestDTO, request);

        return clientLogRequestDTO;
    }

    public static String location(String ip) throws IOException, GeoIp2Exception {

        String countryName = "";
        String cityName = "";
        try {

            String name = "./location/GeoLite2-City.mmdb";
            File database = (new FileResourceUtils().convertResourcesFileIntoFile(name));
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);

            countryName = response.getCountry().getName();
            cityName = response.getCity().getName();

            return cityName + ", " + countryName;

        } catch (IOException | AddressNotFoundException e) {
            return "N/A";
        }

    }
}

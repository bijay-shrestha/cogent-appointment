package com.cogent.cogentappointment.admin.loghandler;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.utils.commons.FileResourceUtils;
import com.cogent.cogentappointment.admin.utils.commons.ObjectMapperUtils;
import com.cogent.cogentappointment.admin.utils.commons.SecurityContextUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import org.springframework.core.io.FileSystemResource;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.cogent.cogentappointment.admin.constants.EmailConstants.LOGO_LOCATION;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.splitByCharacterTypeCamelCase;

/**
 * @author Rupak
 */
public class RequestHandler {
    
    public static AdminLogRequestDTO convertToAdminLogRequestDTO(String userLog) throws IOException {

        AdminLogRequestDTO adminLogRequestDTO = ObjectMapperUtils.map(userLog, AdminLogRequestDTO.class);

        //to do: rupak
        adminLogRequestDTO.setAdminEmail(SecurityContextUtils.getLoggedInAdminEmail());
        adminLogRequestDTO.setFeature(convertToNormalCase(splitByCharacterTypeCamelCase(adminLogRequestDTO.getFeature())));

        return adminLogRequestDTO;
    }

    public static String location(String ip) throws IOException, GeoIp2Exception {

        String address="";
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

                 Location location=response.getLocation();
                 Double lat=location.getLatitude();
                 Double longitude=location.getLongitude();

                System.out.println(lat+","+longitude);

            }catch(Exception e){
                address= "N/A";
            }

            address= countryName + ", " + cityName;

        return address;

    }


}

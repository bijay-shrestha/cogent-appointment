package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.commons.configuration.MinIOProperties;
import org.springframework.stereotype.Component;

/**
 * @author rupak ON 2020/07/17-12:19 PM
 */
@Component
public class MinIOUtils {

    private static MinIOProperties minIOProperties;

    public MinIOUtils(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
    }

    public static String fileUrlCheckPoint(String url) {

        if (url.contains("public")) {
            url = minIOProperties.getCDN_URL() + url.split("public")[1];
        }

        return url;

    }

}

package com.cogent.cogentappointment.commons.utils;

import com.cogent.cogentappointment.commons.configuration.MinIOProperties;
import org.springframework.stereotype.Component;

/**
 * @author rupak ON 2020/07/17-12:19 PM
 */
@Component
public class MinIOUtils {

    private static MinIOProperties minIOProperties;

    private static String ACCESS_KEY="public";

    public MinIOUtils(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
    }

    public static String fileUrlCheckPoint(String url) {

        if (url.contains(ACCESS_KEY)) {
            url = minIOProperties.getCDN_URL() + url.split(ACCESS_KEY)[1];
        }

        return url;

    }

}

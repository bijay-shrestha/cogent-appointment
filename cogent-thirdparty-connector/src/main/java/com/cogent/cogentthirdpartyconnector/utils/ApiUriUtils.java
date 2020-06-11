package com.cogent.cogentthirdpartyconnector.utils;

/**
 * @author rupak ON 2020/06/11-4:35 PM
 */
public class ApiUriUtils {

    public static String checkApiUri(String uri,String key) {

        if (uri.contains("%s")) {

            return uri.replace("%s", "5VQ");
            //add key
        }

        return uri;

    }
}

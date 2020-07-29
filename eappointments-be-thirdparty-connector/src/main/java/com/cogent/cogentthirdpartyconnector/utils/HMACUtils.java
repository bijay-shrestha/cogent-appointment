package com.cogent.cogentthirdpartyconnector.utils;

import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

import static com.cogent.cogentthirdpartyconnector.constants.HMACConstant.COLON;
import static com.cogent.cogentthirdpartyconnector.security.HMACBuilderForEsewa.hmacShaGenerator;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Component
public class HMACUtils {

    public static BiFunction<String, String, String> getSigatureForEsewa = (esewaId, merchantCode) -> {
        String messgae = esewaId + COLON + merchantCode;

        final String signature = hmacShaGenerator(messgae);

        return signature;
    };

}

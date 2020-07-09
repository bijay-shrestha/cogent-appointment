package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.commons.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;

import java.util.Map;

import static com.cogent.cogentappointment.esewa.constants.StringConstant.DATA;

/**
 * @author rupak ON 2020/07/06-2:52 PM
 */
public class JWTDecryptUtils {

    public static Claims decrypt(Map<String, String> token) {

        System.out.println("ENETRING DECRYPT *******************************");

        Claims claims = JwtUtils.decodeToken(token);

        System.out.println("***************************");

        System.out.println("claims------>"+claims);

        System.out.println("EXITING DECRYPT *******************************");

        return claims;

    }

    public static  Object toDecrypt(Map<String, String> data){

        System.out.println("ENETRING TO DECRYPT *******************************");

        System.out.println("***************************");

        System.out.println("data in toDecrpty------>"+data);

        System.out.println("ENETRING TO DECRYPT *******************************");

        return decrypt(data).get(DATA);

    }
}

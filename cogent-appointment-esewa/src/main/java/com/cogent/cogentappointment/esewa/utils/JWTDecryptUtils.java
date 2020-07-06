package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.commons.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;

import java.util.Map;

/**
 * @author rupak ON 2020/07/06-2:52 PM
 */
public class JWTDecryptUtils {

    public static Claims decrypt(Map<String, String> token) {

        Claims claims = JwtUtils.decodeToken(token);

        System.out.println(claims.getSubject());

        return claims;

    }
}

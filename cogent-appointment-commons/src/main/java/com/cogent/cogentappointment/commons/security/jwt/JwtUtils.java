package com.cogent.cogentappointment.commons.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.Map;

;

/**
 * @author Sauravi Thapa ON 7/5/20
 */
@Component
public class JwtUtils implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private static String SECRET_KEY = "ZXNld2FfbWVyY2hhbnRfY2xpZW50";
    public static String generateToken(Object request) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

//We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

//Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setClaims(getClaims(request))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    private static Claims getClaims(Object request) {
        Claims claims = Jwts.claims();
        claims.put("data", request);
        return claims;
    }

    public static Claims decodeToken(Map<String, String> map) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(map.get("data")).getBody();
    }

}

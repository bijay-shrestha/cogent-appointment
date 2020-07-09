package com.cogent.cogentappointment.commons.security.jwt;

import com.cogent.cogentappointment.commons.configuration.ESewaHMAC;
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
import java.util.Objects;

;

/**
 * @author Sauravi Thapa ON 7/5/20
 */
@Component
public class JwtUtils implements Serializable {

    private static ESewaHMAC eSewaHMAC;

    private static String DECODE_CODE = "HmacSHA512 eSewa:057d470f-c6dc-4509-8a2c-670e9bbb1731:954145191157303:ky98M6rSqZ5KXaVZ5NEdcvh2CSwRVgCXcx18RmaVJ0huggvbVQ3+lJmKZKiZVvkEbElXUVGpOYX8nPnoH6ErQA==";

    public JwtUtils(ESewaHMAC eSewaHMAC) {
        this.eSewaHMAC = eSewaHMAC;
    }


    public static String generateToken(Object request) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.valueOf(eSewaHMAC.getHMAC_ALGORITHM());

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(eSewaHMAC.getHMAC_API_SECRET_ESEWA());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        JwtBuilder builder = Jwts.builder()
                .setClaims(getClaims(request))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(
                        eSewaHMAC.getHMAC_API_SECRET_ESEWA_TIME_VALIDITY()) * 1000))
                .signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    public static String generateTokenToTest(Object request) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;


        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(eSewaHMAC.getHMAC_DECODE_API_SECRET_ESEWA());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        JwtBuilder builder = Jwts.builder()
                .setClaims(getClaims(request))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(
                        eSewaHMAC.getHMAC_API_SECRET_ESEWA_TIME_VALIDITY()) * 1000))
                .signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    private static Claims getClaims(Object request) {
        Claims claims = Jwts.claims();
        claims.put("data", request);
        return claims;
    }

    public static Claims decodeToken(Map<String, String> map) {

        System.out.println("ENETRING DECODE TOEKN *******************************");

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(eSewaHMAC.getHMAC_DECODE_API_SECRET_ESEWA()))
                .parseClaimsJws(map.get("data")).getBody();

        System.out.println("*******************************");

        System.out.println("claims-------------->"+claims);

        System.out.println("EXITING DECODE TOEKN *******************************");

        return claims;
    }

}

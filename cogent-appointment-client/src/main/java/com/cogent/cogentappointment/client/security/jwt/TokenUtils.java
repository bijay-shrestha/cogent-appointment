package com.cogent.cogentappointment.client.security.jwt;//package com.cogent.cogentappointment.security.jwt;
//
//import com.cogent.cogentappointment.model.HMACConfig;
//import io.jsonwebtoken.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//import static com.cogent.cogentappointment.log.constants.JwtLog.*;
//
///**
// * @author Sauravi Thapa २०/१/१४
// */
//
//@Component
//@Slf4j
//public class TokenUtils {
//
//   private final HMACConfig HMACConfig;
//
//    public TokenUtils(HMACConfig HMACConfig) {
//        this.HMACConfig = HMACConfig;
//    }
//
//    public String generateJwtToken(Authentication authentication) {
//
//        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
//
//        return Jwts.builder()
//                .setSubject((userPrincipal.getUsername()))
//                .setIssuedAt(new Date())
//                .setAudience("esewa")
//                .setExpiration(new Date((new Date()).getTime() * HMACConfig.getExpiration()))
//                .signWith(SignatureAlgorithm.HS512, HMACConfig.getSecret())
//                .compact();
//    }
//
//    public String getUserNameFromJwtToken(String token) {
//        return Jwts.parser().setSigningKey(HMACConfig.getSecret()).parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public boolean validateJwtToken(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(HMACConfig.getSecret()).parseClaimsJws(authToken);
//            return true;
//        } catch (SignatureException e) {
//            log.error(INVALID_JWT_SIGNATURE, e.getMessage());
//        } catch (MalformedJwtException e) {
//            log.error(INVALID_JWT_TOKEN, e.getMessage());
//        } catch (ExpiredJwtException e) {
//            log.error(JWT_TOKEN_EXPIRED, e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            log.error(JWT_TOKEN_UNSUPPORTED, e.getMessage());
//        } catch (IllegalArgumentException e) {
//            log.error(JWT_CLAIMS_EMPTY, e.getMessage());
//        }
//
//        return false;
//    }
//
//}

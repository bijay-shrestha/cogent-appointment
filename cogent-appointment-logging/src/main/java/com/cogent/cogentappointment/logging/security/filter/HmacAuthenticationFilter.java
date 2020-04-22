package com.cogent.cogentappointment.logging.security.filter;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.logging.repository.HmacApiInfoRepository;
import com.cogent.cogentappointment.logging.security.hmac.AuthHeader;
import com.cogent.cogentappointment.logging.security.hmac.HMACBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cogent.cogentappointment.logging.constants.ErrorMessageConstants.HMAC_BAD_SIGNATURE;
import static com.cogent.cogentappointment.logging.constants.PatternConstants.AUTHORIZATION_HEADER_PATTERN;
import static com.cogent.cogentappointment.logging.constants.PatternConstants.AUTHORIZATION_HEADER_PATTERN_FOR_ESEWA;

/**
 * @author Sauravi Thapa २०/१/१९
 */
@Component
public class HmacAuthenticationFilter extends OncePerRequestFilter {

    private final HmacApiInfoRepository hmacApiInfoRepository;

    public HmacAuthenticationFilter(HmacApiInfoRepository hmacApiInfoRepository) {
        this.hmacApiInfoRepository = hmacApiInfoRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final AuthHeader authHeader = getAuthHeader(request);

        if (authHeader != null) {

            AdminMinDetails adminMinDetails = getAdminMinDetails(authHeader.getEmail(),
                    authHeader.getHospitalCode(),authHeader.getApiKey());

            HMACBuilder signatureBuilder = new HMACBuilder()
                    .algorithm(authHeader.getAlgorithm())
                    .nonce(authHeader.getNonce())
                    .username(authHeader.getEmail())
                    .hospitalId(Math.toIntExact(authHeader.getHospitalId()))
                    .hospitalCode(authHeader.getHospitalCode())
                    .apiKey(authHeader.getApiKey())
                    .apiSecret(adminMinDetails.getApiSecret());

            compareSignature(signatureBuilder, authHeader.getDigest());

            SecurityContextHolder.getContext().setAuthentication(getAuthenticationForHospital(adminMinDetails.getEmail(),
                    adminMinDetails.getHospitalId()));
        }
    }


    public AuthHeader getAuthHeader(HttpServletRequest request) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            return null;
        }
        final Matcher authHeaderMatcher = Pattern.compile(AUTHORIZATION_HEADER_PATTERN).matcher(authHeader);

        if (!authHeaderMatcher.matches()) {
            return null;
        }

        return new AuthHeader(authHeaderMatcher.group(1),
                Integer.parseInt(authHeaderMatcher.group(2)),
                authHeaderMatcher.group(3),
                Integer.parseInt(authHeaderMatcher.group(4)),
                authHeaderMatcher.group(5),
                authHeaderMatcher.group(6),
                authHeaderMatcher.group(7),
                DatatypeConverter.parseBase64Binary(authHeaderMatcher.group(8)));

    }

    private AdminMinDetails getAdminMinDetails(String email,String hospitalCode,String apiKey){

        AdminMinDetails admin = hmacApiInfoRepository.getAdminDetailForAuthentication(
                email,
                hospitalCode,
                apiKey);

        AdminMinDetails client = hmacApiInfoRepository.getAdminDetailForAuthenticationForClient(
                email,
                hospitalCode,
                apiKey);
        if (admin!=null){
            return admin;
        }else {
            return client;
        }


    }


    public void compareSignature(HMACBuilder signatureBuilder, byte[] digest) {
        if (!signatureBuilder.isHashEquals(digest))
            throw new BadCredentialsException(HMAC_BAD_SIGNATURE);
    }

    public PreAuthenticatedAuthenticationToken getAuthentication(String username, String hospitalCode) {
        return new PreAuthenticatedAuthenticationToken(
                username,
                hospitalCode,
                null);
    }

    public PreAuthenticatedAuthenticationToken getAuthenticationForHospital(String username, Long hospitalId) {
        return new PreAuthenticatedAuthenticationToken(
                username,
                hospitalId,
                null);
    }
}

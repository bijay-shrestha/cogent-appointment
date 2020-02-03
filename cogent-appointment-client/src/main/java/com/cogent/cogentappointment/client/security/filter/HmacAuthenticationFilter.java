package com.cogent.cogentappointment.client.security.filter;

import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.client.repository.HmacApiInfoRepository;
import com.cogent.cogentappointment.client.security.hmac.AuthHeader;
import com.cogent.cogentappointment.client.security.hmac.HMACBuilder;
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

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.HMAC_BAD_SIGNATURE;
import static com.cogent.cogentappointment.client.constants.PatternConstants.AUTHORIZATION_HEADER_PATTERN;
import static com.cogent.cogentappointment.client.constants.PatternConstants.AUTHORIZATION_HEADER_PATTERN_FOR_ESEWA;

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

        final AuthHeader eSewaAuthHeader = getAuthHeaderForeSewa(request);

        if (authHeader != null) {

            AdminMinDetails adminMinDetails = hmacApiInfoRepository.getAdminDetailForAuthentication(
                    authHeader.getUsername(),
                    authHeader.getHospitalCode(),
                    authHeader.getApiKey());

            final HMACBuilder signatureBuilder = new HMACBuilder()
                    .algorithm(authHeader.getAlgorithm())
                    .nonce(authHeader.getNonce())
                    .username(adminMinDetails.getUsername())
                    .hospitalCode(adminMinDetails.getHospitalCode())
                    .apiKey(adminMinDetails.getApiKey())
                    .apiSecret(adminMinDetails.getApiSecret());

            compareSignature(signatureBuilder, authHeader.getDigest());

            SecurityContextHolder.getContext().setAuthentication(getAuthentication(adminMinDetails.getUsername()));
        }

        if (authHeader == null && eSewaAuthHeader != null) {

            ThirdPartyDetail thirdPartyDetail = hmacApiInfoRepository.getDetailForAuthentication(
                    eSewaAuthHeader.getHospitalCode(),
                    eSewaAuthHeader.getApiKey());

            final HMACBuilder signatureBuilder = new HMACBuilder()
                    .algorithm(eSewaAuthHeader.getAlgorithm())
                    .nonce(eSewaAuthHeader.getNonce())
                    .hospitalCode(thirdPartyDetail.getHospitalCode())
                    .apiKey(thirdPartyDetail.getApiKey())
                    .apiSecret(thirdPartyDetail.getApiSecret());

            compareSignature(signatureBuilder, eSewaAuthHeader.getDigest());

            SecurityContextHolder.getContext().setAuthentication(getAuthentication(thirdPartyDetail.getHospitalCode()));
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    public AuthHeader getAuthHeader(HttpServletRequest request) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            return null;
        }

        final Matcher authHeaderMatcher = checkIfHeaderMatches(authHeader,
                AUTHORIZATION_HEADER_PATTERN);

        return new AuthHeader(authHeaderMatcher.group(1),
                authHeaderMatcher.group(2),
                authHeaderMatcher.group(3),
                authHeaderMatcher.group(4),
                authHeaderMatcher.group(5),
                DatatypeConverter.parseBase64Binary(authHeaderMatcher.group(6)));
    }

    public AuthHeader getAuthHeaderForeSewa(HttpServletRequest request) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            return null;
        }

        final Matcher authHeaderMatcher = checkIfHeaderMatches(authHeader,
                AUTHORIZATION_HEADER_PATTERN_FOR_ESEWA);

        return new AuthHeader(authHeaderMatcher.group(1),
                null,
                authHeaderMatcher.group(2),
                authHeaderMatcher.group(3),
                authHeaderMatcher.group(4),
                DatatypeConverter.parseBase64Binary(authHeaderMatcher.group(5)));
    }

    public void compareSignature(HMACBuilder signatureBuilder, byte[] digest) {
        if (!signatureBuilder.isHashEquals(digest))
            throw new BadCredentialsException(HMAC_BAD_SIGNATURE);
    }

    public Matcher checkIfHeaderMatches(String authHeader, String pattern) {
        final Matcher authHeaderMatcher = Pattern.compile(pattern).matcher(authHeader);
        if (!authHeaderMatcher.matches()) {
            return null;
        }
        return authHeaderMatcher;
    }

    public PreAuthenticatedAuthenticationToken getAuthentication(String principal) {
        return new PreAuthenticatedAuthenticationToken(
                principal,
                null,
                null);
    }
}

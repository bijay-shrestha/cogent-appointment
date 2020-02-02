package com.cogent.cogentappointment.client.security.filter;

import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.repository.AdminRepository;
import com.cogent.cogentappointment.client.security.hmac.AuthHeader;
import com.cogent.cogentappointment.client.security.hmac.HMACBuilder;
import com.cogent.cogentappointment.client.service.impl.UserDetailsServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import static com.cogent.cogentappointment.client.constants.StringConstant.SPACE;

/**
 * @author Sauravi Thapa २०/१/१९
 */
@Component
public class HmacAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;

    private final AdminRepository adminRepository;

    public HmacAuthenticationFilter(UserDetailsServiceImpl userDetailsService,
                                    AdminRepository adminRepository) {
        this.userDetailsService = userDetailsService;
        this.adminRepository = adminRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final AuthHeader authHeader = getAuthHeader(request);

        final AuthHeader eSewaAuthHeader = getAuthHeaderForeSewa(request);

        if (authHeader != null) {

            String apiKey = authHeader.getApiKey();

            AdminMinDetails adminMinDetails = adminRepository.getAdminInfoByUsernameAndHospitalCode(
                    authHeader.getUsername(),
                    authHeader.getHospitalCode());

            final HMACBuilder signatureBuilder = new HMACBuilder()
                    .algorithm(authHeader.getAlgorithm())
                    .nonce(authHeader.getNonce())
                    .username(adminMinDetails.getUsername())
                    .hospitalCode(adminMinDetails.getHospitalCode())
                    .apiKey(apiKey);

            compareSignature(signatureBuilder, authHeader.getDigest());

            final PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
                    adminMinDetails.getUsername(),
                    null,
                    null);
//            authentication.setDetails(adminMinDetails.getUsername() + adminMinDetails.getHospitalCode());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        if (authHeader == null && eSewaAuthHeader != null) {
            String apiKey = eSewaAuthHeader.getApiKey();
            final HMACBuilder signatureBuilder = new HMACBuilder()
                    .algorithm(eSewaAuthHeader.getAlgorithm())
                    .nonce(eSewaAuthHeader.getNonce())
                    .apiKey(apiKey);
            compareSignature(signatureBuilder, eSewaAuthHeader.getDigest());
            final PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
                    "eSewa",
                    null,
                    null);
//            authentication.setDetails(apiKey);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    public static AuthHeader getAuthHeader(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            return null;
        }
        final Matcher authHeaderMatcher = Pattern.compile(AUTHORIZATION_HEADER_PATTERN).matcher(authHeader);
        if (!authHeaderMatcher.matches()) {
            return null;
        }
        return new AuthHeader(authHeaderMatcher.group(1),
                authHeaderMatcher.group(2),
                authHeaderMatcher.group(3),
                authHeaderMatcher.group(4),
                authHeaderMatcher.group(5),
                DatatypeConverter.parseBase64Binary(authHeaderMatcher.group(6)));
    }

    public static AuthHeader getAuthHeaderForeSewa(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            return null;
        }
        final Matcher authHeaderMatcher = Pattern.compile(AUTHORIZATION_HEADER_PATTERN_FOR_ESEWA).matcher(authHeader);
        if (!authHeaderMatcher.matches()) {
            return null;
        }
        return new AuthHeader(authHeaderMatcher.group(1),
                null,
                null,
                authHeaderMatcher.group(2),
                authHeaderMatcher.group(3),
                DatatypeConverter.parseBase64Binary(authHeaderMatcher.group(4)));
    }

    public void compareSignature(HMACBuilder signatureBuilder, byte[] digest) {
        if (!signatureBuilder.isHashEquals(digest))
            throw new BadCredentialsException(HMAC_BAD_SIGNATURE);
    }
}

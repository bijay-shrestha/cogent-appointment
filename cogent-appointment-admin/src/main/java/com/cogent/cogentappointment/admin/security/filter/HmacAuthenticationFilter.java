package com.cogent.cogentappointment.admin.security.filter;

import com.cogent.cogentappointment.admin.security.hmac.AuthHeader;
import com.cogent.cogentappointment.admin.security.hmac.HMACBuilder;
import com.cogent.cogentappointment.admin.service.impl.UserDetailsServiceImpl;
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

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.HMAC_BAD_SIGNATURE;
import static com.cogent.cogentappointment.admin.constants.PatternConstants.AUTHORIZATION_HEADER_PATTERN;
import static com.cogent.cogentappointment.admin.constants.PatternConstants.AUTHORIZATION_HEADER_PATTERN_FOR_ESEWA;

/**
 * @author Sauravi Thapa २०/१/१९
 */
@Component
public class HmacAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;

    public HmacAuthenticationFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
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

            UserDetails userDetails = userDetailsService.loadUserByUsername(authHeader.getUsername());
            final HMACBuilder signatureBuilder = new HMACBuilder()
                    .algorithm(authHeader.getAlgorithm())
                    .host(request.getServerName() + ":" + request.getServerPort())
                    .nonce(authHeader.getNonce())
                    .username(userDetails.getUsername())
                    .apiKey(apiKey)
                    .scheme(request.getScheme());

            if (!signatureBuilder.isHashEquals(authHeader.getDigest())) {
                throw new BadCredentialsException(HMAC_BAD_SIGNATURE);
            }
            final PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
                    userDetails,
                    null,
                    null);
            authentication.setDetails(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        if (authHeader == null  && eSewaAuthHeader !=null) {
            String apiKey = eSewaAuthHeader.getApiKey();

            final HMACBuilder signatureBuilder = new HMACBuilder()
                    .algorithm(eSewaAuthHeader.getAlgorithm())
                    .host(request.getServerName() + ":" + request.getServerPort())
                    .nonce(eSewaAuthHeader.getNonce())
                    .apiKey(apiKey)
                    .scheme(request.getScheme());

            if (!signatureBuilder.isHashEquals(eSewaAuthHeader.getDigest())) {
                throw new BadCredentialsException(HMAC_BAD_SIGNATURE);
            }
            final PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
                    apiKey,
                    null,
                    null);
            authentication.setDetails(apiKey);
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
        final String algorithm = authHeaderMatcher.group(1);
        final String username = authHeaderMatcher.group(2);
        final String apiKey = authHeaderMatcher.group(3);
        final String nonce = authHeaderMatcher.group(4);
        final String receivedDigest = authHeaderMatcher.group(5);
        return new AuthHeader(algorithm, apiKey, username, nonce, DatatypeConverter.parseBase64Binary(receivedDigest));
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
        final String algorithm = authHeaderMatcher.group(1);
        final String apiKey = authHeaderMatcher.group(2);
        final String nonce = authHeaderMatcher.group(3);
        final String receivedDigest = authHeaderMatcher.group(4);
        return new AuthHeader(algorithm, apiKey, null, nonce, DatatypeConverter.parseBase64Binary(receivedDigest));
    }

}

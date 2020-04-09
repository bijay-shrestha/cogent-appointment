package com.cogent.cogentappointment.admin.security.filter;

import com.cogent.cogentappointment.admin.security.hmac.AuthHeader;
import com.cogent.cogentappointment.admin.security.hmac.HMACBuilder;
import com.cogent.cogentappointment.admin.service.impl.UserDetailsImpl;
import com.cogent.cogentappointment.admin.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
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

/**
 * @author Sauravi Thapa २०/१/१९
 */
@Component
@Slf4j
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

        if (authHeader != null) {

            final HMACBuilder signatureBuilder;

            UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(authHeader.getUsername());

            if (userDetails.getIsCompany().equals('Y')) {
                signatureBuilder = new HMACBuilder()
                        .algorithm(authHeader.getAlgorithm())
                        .id(authHeader.getUserId())
                        .nonce(authHeader.getNonce())
                        .username(userDetails.getUsername())
                        .companyId(Math.toIntExact(authHeader.getCompanyId()))
                        .companyCode(authHeader.getCompanyCode())
                        .apiKey(authHeader.getApiKey())
                        .apiSecret(userDetails.getApiSecret());

            } else {
                signatureBuilder = null;
            }

            compareSignature(signatureBuilder, authHeader.getDigest());

            SecurityContextHolder.getContext().setAuthentication(getAuthenticationForCompany(userDetails.getUsername(),
                    userDetails.getCompanyId()));
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

    public void compareSignature(HMACBuilder signatureBuilder, byte[] digest) {
        if (!signatureBuilder.isHashEquals(digest))
            throw new BadCredentialsException(HMAC_BAD_SIGNATURE);
    }

    public PreAuthenticatedAuthenticationToken getAuthenticationForCompany(String username, Long companyId) {
        return new PreAuthenticatedAuthenticationToken(
                username,
                companyId,
                null);
    }

}

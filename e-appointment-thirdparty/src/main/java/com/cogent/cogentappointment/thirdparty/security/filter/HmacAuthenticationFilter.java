//package com.cogent.cogentappointment.thirdparty.security.filter;
//
//import com.cogent.cogentappointment.thirdparty.dto.request.login.ThirdPartyDetail;
//import com.cogent.cogentappointment.thirdparty.repository.HmacApiInfoRepository;
//import com.cogent.cogentappointment.thirdparty.security.hmac.AuthHeader;
//import com.cogent.cogentappointment.thirdparty.security.hmac.HMACBuilder;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.xml.bind.DatatypeConverter;
//import java.io.IOException;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import static com.cogent.cogentappointment.thirdparty.constants.PatternConstants.AUTHORIZATION_HEADER_PATTERN_FOR_ESEWA;
//
///**
// * @author Sauravi Thapa २०/१/१९
// */
//@Component
//@Slf4j
//public class HmacAuthenticationFilter extends OncePerRequestFilter {
//
//    private final HmacApiInfoRepository hmacApiInfoRepository;
//
//    public HmacAuthenticationFilter(HmacApiInfoRepository hmacApiInfoRepository) {
//        this.hmacApiInfoRepository = hmacApiInfoRepository;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        final AuthHeader thirdPartyAuthHeader = getAuthHeaderForeSewa(request);
//
//
//        if (thirdPartyAuthHeader != null) {
//
//            ThirdPartyDetail thirdPartyDetail = hmacApiInfoRepository.getDetailForAuthentication(
//                    thirdPartyAuthHeader.getCompanyCode(),
//                    thirdPartyAuthHeader.getApiKey());
//
//            final HMACBuilder signatureBuilder = new HMACBuilder()
//                    .algorithm(thirdPartyAuthHeader.getAlgorithm())
//                    .nonce(thirdPartyAuthHeader.getNonce())
//                    .companyCode(thirdPartyAuthHeader.getCompanyCode())
//                    .apiKey(thirdPartyAuthHeader.getApiKey())
//                    .apiSecret(thirdPartyDetail.getApiSecret());
//
//            compareSignature(signatureBuilder, thirdPartyAuthHeader.getDigest());
//
//            SecurityContextHolder.getContext().setAuthentication(getAuthentication(thirdPartyDetail.getCompanyCode()));
//        }
//        try {
//            filterChain.doFilter(request, response);
//        } finally {
//            SecurityContextHolder.clearContext();
//        }
//
//    }
//
//
//    public AuthHeader getAuthHeaderForeSewa(HttpServletRequest request) {
//
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null) {
//            return null;
//        }
//        final Matcher authHeaderMatcher = Pattern.compile(AUTHORIZATION_HEADER_PATTERN_FOR_ESEWA).matcher(authHeader);
//        if (!authHeaderMatcher.matches()) {
//            return null;
//        }
//        return new AuthHeader(authHeaderMatcher.group(1),
//                authHeaderMatcher.group(2),
//                authHeaderMatcher.group(3),
//                authHeaderMatcher.group(4),
//                DatatypeConverter.parseBase64Binary(authHeaderMatcher.group(5)));
//    }
//
//    public void compareSignature(HMACBuilder signatureBuilder, byte[] digest) {
//        if (!signatureBuilder.isHashEquals(digest))
//            throw new BadCredentialsException("HmacAccessFilter.badSignature");
//    }
//
//    public PreAuthenticatedAuthenticationToken getAuthentication(String companyCode) {
//        return new PreAuthenticatedAuthenticationToken(
//                companyCode,
//                companyCode,
//                null);
//    }
//}

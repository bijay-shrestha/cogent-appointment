package com.cogent.cogentappointment.esewa.resource.v2;

import com.cogent.cogentappointment.esewa.dto.request.LoginRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.esewa.repository.HmacApiInfoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.esewa.security.hmac.HMACUtils.getAuthToken;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 4/3/20
 */
@RestController(API_V2+LOGIN)
@RequestMapping(API_V2+LOGIN)
public class LoginResource {

    private final HmacApiInfoRepository hmacApiInfoRepository;

    public LoginResource(HmacApiInfoRepository hmacApiInfoRepository) {
        this.hmacApiInfoRepository = hmacApiInfoRepository;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO requestDTO) {
        ThirdPartyDetail thirdPartyDetail = hmacApiInfoRepository.getDetailsByHospitalCode(requestDTO.getCompanyCode());

        String authToken = getAuthToken(thirdPartyDetail);

        return ok(authToken);
    }
}

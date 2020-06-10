package com.cogent.cogentappointment.thirdparty.repository.custom;

import com.cogent.cogentappointment.thirdparty.dto.request.login.ThirdPartyDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 7/21/19
 */
@Repository
@Qualifier("hmacApiKeyRepositoryCustom")
public interface HmacApiInfoRepositoryCustom {

    ThirdPartyDetail getDetailForAuthentication(String companyCode, String apiKey);

    ThirdPartyDetail getDetailsByHospitalCode(String hospitalCode);
}


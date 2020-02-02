package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.dto.request.login.ThirdPartyDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 7/21/19
 */
@Repository
@Qualifier("hmacApiKeyRepositoryCustom")
public interface HmacApiInfoRepositoryCustom {
    ThirdPartyDetail getDetailsForAuthentication(String hospitalCode, String apiKey);

    ThirdPartyDetail getDetailsByHositalCode(String hospitalCode);

    AdminMinDetails verifyLoggedInAdmin(String username, String hospitalCode);

    AdminMinDetails getAdminDetailsForAuthentication(String username,
                                                     String hospitalCode,
                                                     String apiKey);

}


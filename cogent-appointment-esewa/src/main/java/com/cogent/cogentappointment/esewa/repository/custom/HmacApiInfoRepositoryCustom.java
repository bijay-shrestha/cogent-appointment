package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.esewa.dto.request.login.ThirdPartyDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 7/21/19
 */
@Repository
@Qualifier("hmacApiKeyRepositoryCustom")
public interface HmacApiInfoRepositoryCustom {
    ThirdPartyDetail getDetailForAuthentication(String hospitalCode, String apiKey);

    ThirdPartyDetail getDetailsByHospitalCode(String hospitalCode);

    AdminMinDetails verifyLoggedInAdmin(String username, String hospitalCode);

    AdminMinDetails getAdminDetailForAuthentication(String username,
                                                    String hospitalCode,
                                                    String apiKey);

}


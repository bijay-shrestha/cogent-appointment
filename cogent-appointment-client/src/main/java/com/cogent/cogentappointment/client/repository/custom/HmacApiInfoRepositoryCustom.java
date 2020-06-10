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
    ThirdPartyDetail getDetailForAuthentication(String hospitalCode, String apiKey);

    ThirdPartyDetail getDetailsByHospitalCode(String companyCode);

    AdminMinDetails getAdminDetailForAuthentication(String email,
                                                    String hospitalCode,
                                                    String apiKey);

}


package com.cogent.cogentappointment.logging.repository.custom;
import com.cogent.cogentappointment.logging.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.logging.dto.request.login.ThirdPartyDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 7/21/19
 */
@Repository
@Qualifier("hmacApiKeyRepositoryCustom")
public interface HmacApiInfoRepositoryCustom {

    AdminMinDetails getAdminDetailForAuthentication(String email,
                                                    String hospitalCode,
                                                    String apiKey);

    AdminMinDetails getAdminDetailForAuthenticationForClient(String email,
                                                    String hospitalCode,
                                                    String apiKey);

}


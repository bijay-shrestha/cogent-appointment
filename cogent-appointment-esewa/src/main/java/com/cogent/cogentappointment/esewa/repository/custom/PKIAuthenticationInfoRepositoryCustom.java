package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.PKIResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 06/07/20
 */
@Repository
@Qualifier("PKIAuthenticationInfoRepositoryCustom")
public interface PKIAuthenticationInfoRepositoryCustom {

    PKIResponseDTO findServerPrivateKeyByClientId(String clientId);

    String findClientPublicKeyByClientId(String clientId);

}

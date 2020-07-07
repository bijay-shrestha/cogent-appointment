package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.PKIAuthenticationInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.PKIAuthenticationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 06/07/20
 */
@Repository
public interface PKIAuthenticationInfoRepository extends JpaRepository<PKIAuthenticationInfo, Long>,
        PKIAuthenticationInfoRepositoryCustom {

    @Query("SELECT p.serverPrivateKey FROM PKIAuthenticationInfo p WHERE p.clientId=:clientId AND p.status = 'Y'")
    Optional<String> fetchServerPrivateKeyByClientId(@Param("clientId") String clientId);

    @Query(" SELECT p.clientPublicKey FROM PKIAuthenticationInfo p WHERE p.clientId =:clientId AND p.status = 'Y'")
    Optional<String> fetchClientPublicKeyByClientId(@Param("clientId") String clientId);
}

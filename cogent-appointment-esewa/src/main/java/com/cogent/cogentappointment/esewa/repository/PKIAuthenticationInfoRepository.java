package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.PKIAuthenticationInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.PKIAuthenticationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 06/07/20
 */
@Repository
public interface PKIAuthenticationInfoRepository extends JpaRepository<PKIAuthenticationInfo, Long>,
        PKIAuthenticationInfoRepositoryCustom {
}

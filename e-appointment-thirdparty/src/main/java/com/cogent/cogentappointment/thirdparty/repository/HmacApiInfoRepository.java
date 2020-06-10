package com.cogent.cogentappointment.thirdparty.repository;

import com.cogent.cogentappointment.persistence.model.HmacApiInfo;
import com.cogent.cogentappointment.thirdparty.repository.custom.HmacApiInfoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sauravi Thapa २०/२/२
 */
@Repository
public interface HmacApiInfoRepository extends JpaRepository<HmacApiInfo, Long>, HmacApiInfoRepositoryCustom {

}



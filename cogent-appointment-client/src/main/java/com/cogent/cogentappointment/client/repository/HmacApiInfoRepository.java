package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.model.HmacApiInfo;
import com.cogent.cogentappointment.client.repository.custom.HmacApiInfoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sauravi Thapa २०/२/२
 */
@Repository
public interface HmacApiInfoRepository extends JpaRepository<HmacApiInfo, Long>, HmacApiInfoRepositoryCustom {

}



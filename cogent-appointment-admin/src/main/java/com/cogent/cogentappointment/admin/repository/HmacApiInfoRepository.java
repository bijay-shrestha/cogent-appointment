package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.HmacApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sauravi Thapa २०/२/२
 */
@Repository
public interface HmacApiInfoRepository extends JpaRepository<HmacApiInfo, Long> {

}



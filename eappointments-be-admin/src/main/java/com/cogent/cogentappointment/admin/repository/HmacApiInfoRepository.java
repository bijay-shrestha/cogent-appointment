package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.HmacApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Sauravi Thapa २०/२/२
 */
@Repository
public interface HmacApiInfoRepository extends JpaRepository<HmacApiInfo, Long> {

    @Query("FROM HmacApiInfo hai WHERE hai.hospital.id=:hospitalId AND hai.status!='D'")
    HmacApiInfo getHmacApiInfoByHospitalId(@Param("hospitalId") Long hospitalId);

    @Query("FROM HmacApiInfo hai WHERE hai.hospital.id=:companyId AND hai.status!='D'")
    HmacApiInfo getHmacApiInfoByCompanyId(@Param("companyId") Long companyId);
}



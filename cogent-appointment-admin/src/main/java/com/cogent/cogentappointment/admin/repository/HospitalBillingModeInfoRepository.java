package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.HospitalBillingModeInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalBillingModeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Sauravi Thapa ON 5/29/20
 */
@Repository
public interface HospitalBillingModeInfoRepository extends JpaRepository<HospitalBillingModeInfo,Long>,
        HospitalBillingModeInfoRepositoryCustom {

    @Query("SELECT h FROM HospitalBillingModeInfo h WHERE h.status='Y' AND h.billingMode.id = :billingModeId" +
            " AND h.hospital.id=:hospitalId")
    HospitalBillingModeInfo fetchHospitalBillingModeInfo(@Param("billingModeId") Long billingModeId,
                                                                   @Param("hospitalId") Long hospitalId);

    @Query("SELECT h FROM HospitalBillingModeInfo h WHERE h.status !='D'  AND h.hospital.id=:hospitalId")
    List<HospitalBillingModeInfo> fetchHospitalBillingModeInfoByHospitalId(@Param("hospitalId") Long hospitalId);
}

package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.HospitalBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 05/02/2020
 */
@Repository
public interface HospitalBannerRepository extends JpaRepository<HospitalBanner, Long> {

    @Query("SELECT h FROM HospitalBanner h WHERE h.hospital.id = :id")
    HospitalBanner findHospitalBannerByHospitalId(@Param("id") Long id);
}

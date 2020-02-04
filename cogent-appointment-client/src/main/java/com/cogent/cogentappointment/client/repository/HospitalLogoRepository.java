package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.HospitalLogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
public interface HospitalLogoRepository extends JpaRepository<HospitalLogo, Long> {

    @Query("SELECT h FROM HospitalLogo h WHERE h.hospital.id = :id")
    HospitalLogo findHospitalLogoByHospitalId(@Param("id") Long id);
}

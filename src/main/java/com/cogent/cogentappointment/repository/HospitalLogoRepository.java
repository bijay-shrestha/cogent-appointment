package com.cogent.cogentappointment.repository;

import com.cogent.cogentappointment.model.HospitalLogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
public interface HospitalLogoRepository extends JpaRepository<HospitalLogo, Long> {
}

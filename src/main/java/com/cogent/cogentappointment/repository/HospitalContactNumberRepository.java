package com.cogent.cogentappointment.repository;

import com.cogent.cogentappointment.model.HospitalContactNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
public interface HospitalContactNumberRepository extends JpaRepository<HospitalContactNumber, Long> {
}

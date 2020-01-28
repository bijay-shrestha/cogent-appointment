package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.model.DoctorQualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 13/11/2019
 */
@Repository
public interface DoctorQualificationRepository extends JpaRepository<DoctorQualification, Long> {
}

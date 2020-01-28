package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.model.DoctorQualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 13/11/2019
 */
@Repository
public interface DoctorQualificationRepository extends JpaRepository<DoctorQualification, Long> {
}

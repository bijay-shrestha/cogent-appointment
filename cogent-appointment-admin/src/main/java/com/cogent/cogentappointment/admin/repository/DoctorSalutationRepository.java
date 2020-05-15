package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.DoctorSalutation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorSalutationRepository extends JpaRepository<DoctorSalutation, Long> {


}

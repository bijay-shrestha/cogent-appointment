package com.cogent.cogentappointment.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorSalutationRepository extends JpaRepository<DoctorSalutation, Long> {


}

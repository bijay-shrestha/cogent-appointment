package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.HospitalAppointmentServiceTypeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalAppointmentServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 26/05/20
 */
@Repository
public interface HospitalAppointmentServiceTypeRepository extends JpaRepository<HospitalAppointmentServiceType, Long>,
        HospitalAppointmentServiceTypeRepositoryCustom {

    @Query("SELECT h FROM HospitalAppointmentServiceType h WHERE h.id =:id AND h.status = 'Y'")
    Optional<HospitalAppointmentServiceType> fetchActiveById(@Param("id") Long id);
}

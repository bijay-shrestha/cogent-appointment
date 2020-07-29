package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AppointmentServiceTypeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 26/05/20
 */
@Repository
public interface AppointmentServiceTypeRepository extends JpaRepository<AppointmentServiceType, Long>,
        AppointmentServiceTypeRepositoryCustom {

    @Query("SELECT a FROM AppointmentServiceType a WHERE a.id =:id AND a.status = 'Y'")
    Optional<AppointmentServiceType> fetchActiveById(@Param("id") Long id);

}

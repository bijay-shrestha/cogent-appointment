package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AppointmentModeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sauravi Thapa on 17/04/2020
 */
@Repository
public interface AppointmentModeRepository extends JpaRepository<AppointmentMode, Long>, AppointmentModeRepositoryCustom {

    @Query("SELECT am FROM AppointmentMode am WHERE am.status!='D' AND am.id = :id")
    Optional<AppointmentMode> fetchAppointmentModeById(@Param("id") Long id);

    @Query("SELECT am FROM AppointmentMode am WHERE am.status='Y' AND am.id = :id")
    Optional<AppointmentMode> fetchActiveAppointmentModeById(@Param("id") Long id);
}

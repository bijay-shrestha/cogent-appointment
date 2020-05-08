package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sauravi Thapa ON 4/19/20
 */
@Repository
public interface AppointmentModeRepository extends JpaRepository<AppointmentMode, Long> {

    @Query("SELECT am FROM AppointmentMode am WHERE am.status='Y' AND am.code = :code")
    Optional<AppointmentMode> fetchActiveAppointmentModeByCode(@Param("code") String code);
}

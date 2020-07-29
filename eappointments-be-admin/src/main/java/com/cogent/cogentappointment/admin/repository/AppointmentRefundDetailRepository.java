package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AppointmentRefundDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti ON 06/02/2020
 */
@Repository
public interface AppointmentRefundDetailRepository extends JpaRepository<AppointmentRefundDetail, Long>,
        AppointmentRefundDetailRepositoryCustom {

    @Query("SELECT a FROM AppointmentRefundDetail a WHERE a.status='PA' AND a.appointmentId.id = :appointmentId")
    Optional<AppointmentRefundDetail> findByAppointmentId(@Param("appointmentId") Long id);
}

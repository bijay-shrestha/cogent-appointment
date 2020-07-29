package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AppointmentRefundDetailRepositoryCustom;
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

    @Query("SELECT a FROM AppointmentRefundDetail a WHERE a.status='PA' AND a.appointmentId.id = :appointmentId" +
            " AND a.appointmentId.hospitalId.id=:hospitalId")
    Optional<AppointmentRefundDetail> findByAppointmentIdAndHospitalId(@Param("appointmentId") Long id,
                                                                       @Param("hospitalId") Long hospitalId);
}

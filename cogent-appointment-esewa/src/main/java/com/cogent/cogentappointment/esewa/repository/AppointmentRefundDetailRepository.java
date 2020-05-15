package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 06/02/2020
 */
@Repository
public interface AppointmentRefundDetailRepository extends JpaRepository<AppointmentRefundDetail, Long> {

}

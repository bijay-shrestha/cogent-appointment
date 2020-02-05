package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.model.AppointmentTransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 04/02/2020
 */
@Repository
public interface AppointmentTransactionInfoRepository extends JpaRepository<AppointmentTransactionDetail, Long> {
}

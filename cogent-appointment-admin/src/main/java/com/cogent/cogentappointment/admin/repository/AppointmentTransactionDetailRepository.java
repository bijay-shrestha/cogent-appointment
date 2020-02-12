package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AppointmentTransactionDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 04/02/2020
 */
@Repository
public interface AppointmentTransactionDetailRepository extends JpaRepository<AppointmentTransactionDetail, Long>,
        AppointmentTransactionDetailRepositoryCustom {
}

package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AppointmentTransferTransactionRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentTransferTransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public interface AppointmentTransferTransactionDetailRepository extends JpaRepository<AppointmentTransferTransactionDetail, Long>,
        AppointmentTransferTransactionRepositoryCustom {
}

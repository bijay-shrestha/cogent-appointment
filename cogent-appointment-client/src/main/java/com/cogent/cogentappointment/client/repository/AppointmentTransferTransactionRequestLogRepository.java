package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AppointmentTransferTransactionRepositoryCustom;
import com.cogent.cogentappointment.client.repository.custom.AppointmentTransferTransactionRequestLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public interface AppointmentTransferTransactionRequestLogRepository extends JpaRepository<AppointmentTransfer, Long>,
        AppointmentTransferTransactionRequestLogRepositoryCustom {
}

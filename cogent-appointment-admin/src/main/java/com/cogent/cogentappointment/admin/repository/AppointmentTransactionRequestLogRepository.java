package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AppointmentTransactionRequestLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author smriti on 31/03/20
 */
public interface AppointmentTransactionRequestLogRepository extends JpaRepository<AppointmentTransactionRequestLog, Long>,
        AppointmentTransactionRequestLogRepositoryCustom {

}

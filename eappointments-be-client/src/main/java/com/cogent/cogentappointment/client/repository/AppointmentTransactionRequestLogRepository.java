package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AppointmentTransactionRequestLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author smriti on 31/03/20
 */
public interface AppointmentTransactionRequestLogRepository extends JpaRepository<AppointmentTransactionRequestLog, Long>,
        AppointmentTransactionRequestLogRepositoryCustom {

    @Query("SELECT rl FROM AppointmentTransactionRequestLog rl WHERE rl.transactionNumber=:transactionNumber")
    Optional<AppointmentTransactionRequestLog> fetchByTransactionNumber(@Param("transactionNumber") String transactionNumber);
}

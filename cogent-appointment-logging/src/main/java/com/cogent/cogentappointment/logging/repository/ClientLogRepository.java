package com.cogent.cogentappointment.logging.repository;

import com.cogent.cogentappointment.logging.repository.custom.ClientLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ClientLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rupak
 */
@Repository
public interface ClientLogRepository extends JpaRepository<ClientLog, Long>, ClientLogRepositoryCustom {


}

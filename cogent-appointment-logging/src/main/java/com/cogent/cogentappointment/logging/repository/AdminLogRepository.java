package com.cogent.cogentappointment.logging.repository;

import com.cogent.cogentappointment.persistence.model.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rupak
 */
@Repository
public interface AdminLogRepository extends JpaRepository<AdminLog, Long> {
}

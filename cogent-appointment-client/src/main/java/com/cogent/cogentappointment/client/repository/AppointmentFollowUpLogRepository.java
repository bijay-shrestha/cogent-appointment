package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 13/02/20
 */
@Repository
public interface AppointmentFollowUpLogRepository extends JpaRepository<AppointmentFollowUpLog, Long> {

}

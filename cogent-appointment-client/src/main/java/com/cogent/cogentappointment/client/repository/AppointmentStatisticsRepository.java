package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sauravi Thapa ON 4/16/20
 */
@Repository
public interface AppointmentStatisticsRepository extends JpaRepository<AppointmentStatistics, Long> {
}

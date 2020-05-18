package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.SpecializationWeekDaysDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.SpecializationWeekDaysDutyRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sauravi Thapa ON 5/18/20
 */
@Repository
public interface SpecializationWeekDaysDutyRosterRepository extends JpaRepository<
        SpecializationWeekDaysDutyRoster, Long>, SpecializationWeekDaysDutyRosterRepositoryCustom {

}

package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.HospitalDeptWeekDaysDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 20/05/20
 */
@Repository
public interface HospitalDeptWeekDaysDutyRosterRepository extends JpaRepository<
        HospitalDepartmentWeekDaysDutyRoster, Long>, HospitalDeptWeekDaysDutyRosterRepositoryCustom {

    @Query("SELECT h FROM HospitalDepartmentWeekDaysDutyRoster h WHERE h.id = :id")
    Optional<HospitalDepartmentWeekDaysDutyRoster> fetchById(@Param("id") Long id);

}

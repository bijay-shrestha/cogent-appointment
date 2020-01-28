package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.model.DoctorWeekDaysDutyRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 27/11/2019
 */
@Repository
public interface DoctorWeekDaysDutyRosterRepository extends JpaRepository<DoctorWeekDaysDutyRoster, Long> {

    @Query("SELECT d FROM DoctorWeekDaysDutyRoster d WHERE d.doctorDutyRosterId.id =:doctorDutyRosterId")
    List<DoctorWeekDaysDutyRoster> fetchByDoctorDutyRosterId(@Param("doctorDutyRosterId") Long doctorDutyRosterId);
}

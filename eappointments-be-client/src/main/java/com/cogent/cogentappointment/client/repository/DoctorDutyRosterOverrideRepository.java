package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.DoctorDutyRosterOverrideRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorDutyRosterOverrideRepository extends JpaRepository<DoctorDutyRosterOverride, Long>,
        DoctorDutyRosterOverrideRepositoryCustom {

    @Query("SELECT d FROM DoctorDutyRosterOverride d WHERE d.status = 'Y' AND d.doctorDutyRosterId.id =:id")
    List<DoctorDutyRosterOverride> fetchByDoctorRosterId(@Param("id") Long id);

    @Query("SELECT d FROM DoctorDutyRosterOverride d WHERE d.status = 'Y' AND d.id =:id")
    DoctorDutyRosterOverride fetchById(@Param("id") Long id);
}

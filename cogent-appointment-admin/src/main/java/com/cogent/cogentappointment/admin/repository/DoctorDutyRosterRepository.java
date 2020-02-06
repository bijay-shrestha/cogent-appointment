package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DoctorDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**W
 * @author smriti on 26/11/2019
 */
@Repository
public interface DoctorDutyRosterRepository extends JpaRepository<DoctorDutyRoster, Long>,
        DoctorDutyRosterRepositoryCustom {

    @Query("SELECT d FROM DoctorDutyRoster d WHERE d.status!='D' AND d.id = :id")
    Optional<DoctorDutyRoster> findDoctorDutyRosterById(@Param("id") Long id);
}

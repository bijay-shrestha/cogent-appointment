package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.SpecializationDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.SpecializationDutyRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sauravi Thapa ON 5/18/20
 */
@Repository
public interface SpecializationDutyRosterRepository extends JpaRepository<SpecializationDutyRoster, Long>,
        SpecializationDutyRosterRepositoryCustom {

    @Query("SELECT s FROM SpecializationDutyRoster s WHERE s.status!='D' AND s.id = :id AND s.hospital.id=:hospitalId")
    Optional<SpecializationDutyRoster> findSpecializationDutyRosterByIdAndHospitalId(@Param("id") Long id,
                                                                     @Param("hospitalId") Long hospitalId);
}

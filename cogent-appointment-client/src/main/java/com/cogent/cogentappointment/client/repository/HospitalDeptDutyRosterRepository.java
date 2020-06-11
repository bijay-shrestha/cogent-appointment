package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.HospitalDeptDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 20/05/20
 */
@Repository
public interface HospitalDeptDutyRosterRepository extends JpaRepository<HospitalDepartmentDutyRoster, Long>,
        HospitalDeptDutyRosterRepositoryCustom {

    @Query("SELECT d FROM HospitalDepartmentDutyRoster d WHERE d.status!='D' AND d.id = :id" +
            " AND d.hospital.id =:hospitalId")
    Optional<HospitalDepartmentDutyRoster> fetchByIdAndHospitalId(@Param("id") Long id,
                                                                  @Param("hospitalId") Long hospitalId);
}

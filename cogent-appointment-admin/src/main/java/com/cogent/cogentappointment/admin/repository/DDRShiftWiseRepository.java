package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DDRShiftWiseRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DoctorDutyRosterShiftWise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 08/05/20
 */
@Repository
public interface DDRShiftWiseRepository extends JpaRepository<DoctorDutyRosterShiftWise, Long>,
        DDRShiftWiseRepositoryCustom {

    @Query("SELECT d FROM DoctorDutyRosterShiftWise d WHERE d.status!='D' AND d.id = :id")
    Optional<DoctorDutyRosterShiftWise> findDoctorDutyRosterById(@Param("id") Long id);

    @Query("SELECT d.hasOverride FROM DoctorDutyRosterShiftWise d WHERE d.status!='D' AND d.id = :id")
    Optional<Character> fetchOverrideStatus(@Param("id")Long id);
}

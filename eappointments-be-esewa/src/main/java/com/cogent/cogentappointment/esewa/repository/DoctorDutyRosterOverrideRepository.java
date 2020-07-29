package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.DoctorDutyRosterOverrideRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorDutyRosterOverrideRepository extends JpaRepository<DoctorDutyRosterOverride, Long>,
        DoctorDutyRosterOverrideRepositoryCustom {

}

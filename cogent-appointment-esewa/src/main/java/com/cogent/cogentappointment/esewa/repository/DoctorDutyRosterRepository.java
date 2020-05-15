package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.DoctorDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 26/11/2019
 */
@Repository
public interface DoctorDutyRosterRepository extends JpaRepository<DoctorDutyRoster, Long>,
        DoctorDutyRosterRepositoryCustom {

}

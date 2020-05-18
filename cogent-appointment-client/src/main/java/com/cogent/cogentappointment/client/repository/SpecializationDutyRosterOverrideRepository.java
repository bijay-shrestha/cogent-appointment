package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.SpecializationDutyRosterOverrideRepositoryCustom;
import com.cogent.cogentappointment.client.repository.custom.SpecializationDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.SpecializationDutyRoster;
import com.cogent.cogentappointment.persistence.model.SpecializationDutyRosterOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sauravi Thapa ON 5/18/20
 */
@Repository
public interface SpecializationDutyRosterOverrideRepository extends JpaRepository<SpecializationDutyRosterOverride, Long>,
        SpecializationDutyRosterOverrideRepositoryCustom {

}

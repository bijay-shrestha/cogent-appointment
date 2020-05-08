package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DDRShiftWiseRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterShiftWise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 08/05/20
 */
@Repository
public interface DDRShiftWiseRepository extends JpaRepository<DoctorDutyRosterShiftWise, Long>,
        DDRShiftWiseRepositoryCustom {

}

package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DDROverrideBreakDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDROverrideBreakDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 11/05/20
 */
@Repository
public interface DDROverrideBreakDetailRepository extends JpaRepository<DDROverrideBreakDetail, Long>,
        DDROverrideBreakDetailRepositoryCustom{
}

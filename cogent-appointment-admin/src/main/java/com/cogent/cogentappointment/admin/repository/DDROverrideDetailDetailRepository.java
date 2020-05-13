package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DDROverrideDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDROverrideDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 11/05/20
 */
@Repository
public interface DDROverrideDetailDetailRepository extends JpaRepository<DDROverrideDetail, Long>,
        DDROverrideDetailRepositoryCustom {
}

package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DDRShiftDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRShiftDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 08/05/20
 */
@Repository
public interface DDRShiftDetailRepository extends JpaRepository<DDRShiftDetail, Long>,
        DDRShiftDetailRepositoryCustom {

}

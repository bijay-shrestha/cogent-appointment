package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DDROverrideBreakDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDROverrideBreakDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 11/05/20
 */
@Repository
public interface DDROverrideBreakDetailRepository extends JpaRepository<DDROverrideBreakDetail, Long>,
        DDROverrideBreakDetailRepositoryCustom {

    @Query(" SELECT db FROM DDROverrideBreakDetail db WHERE db.status = 'Y' AND db.ddrOverrideDetail.id =:ddrOverrideId")
    List<DDROverrideBreakDetail> fetchByDDROverrideId(@Param("ddrOverrideId") Long ddrOverrideId);
}

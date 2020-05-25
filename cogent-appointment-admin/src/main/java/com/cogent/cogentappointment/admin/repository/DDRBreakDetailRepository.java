package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DDRBreakDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRBreakDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 08/05/20
 */
@Repository
public interface DDRBreakDetailRepository extends JpaRepository<DDRBreakDetail, Long> ,
        DDRBreakDetailRepositoryCustom{

    @Query("SELECT b FROM BreakType b WHERE b.status = 'Y' AND b.id =:id")
    Optional<DDRBreakDetail> fetchActiveDDRBreakDetail(@Param("id") Long id);
}

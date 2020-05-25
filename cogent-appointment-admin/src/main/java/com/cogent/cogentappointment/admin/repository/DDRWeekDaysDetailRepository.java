package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DDRWeekDaysDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRShiftDetail;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRWeekDaysDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 08/05/20
 */
@Repository
public interface DDRWeekDaysDetailRepository extends JpaRepository<DDRWeekDaysDetail, Long>,
        DDRWeekDaysDetailRepositoryCustom {

    @Query("SELECT d FROM DDRWeekDaysDetail d WHERE d.id = :id")
    Optional<DDRWeekDaysDetail> fetchById(@Param("id") Long id);
}

package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DDROverrideDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDROverrideDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 11/05/20
 */
@Repository
public interface DDROverrideDetailDetailRepository extends JpaRepository<DDROverrideDetail, Long>,
        DDROverrideDetailRepositoryCustom {

    @Query("SELECT d FROM DDROverrideDetail d WHERE d.status = 'Y' AND d.id =:id")
    Optional<DDROverrideDetail> fetchById(@Param("id") Long id);
}

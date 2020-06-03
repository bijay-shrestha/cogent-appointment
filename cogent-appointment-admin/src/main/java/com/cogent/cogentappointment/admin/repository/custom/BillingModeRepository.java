package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.persistence.model.BillingMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sauravi Thapa on 29/05/2020
 */
@Repository
public interface BillingModeRepository extends JpaRepository<BillingMode, Long>, BillingModeRepositoryCustom {

    @Query("SELECT bm FROM BillingMode bm WHERE bm.status!='D' AND bm.id = :id")
    Optional<BillingMode> fetchBillingModeById(@Param("id") Long id);

    @Query("SELECT bm FROM BillingMode bm WHERE bm.status='Y' AND bm.code = :code")
    Optional<BillingMode> fetchActiveBillingModeByCode(@Param("code") String code);
}

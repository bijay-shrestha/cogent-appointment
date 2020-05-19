package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DDRShiftDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRShiftDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 08/05/20
 */
@Repository
public interface DDRShiftDetailRepository extends JpaRepository<DDRShiftDetail, Long>,
        DDRShiftDetailRepositoryCustom {

    @Query("SELECT d FROM DDRShiftDetail d WHERE d.status!='D' AND d.ddrShiftWise.id = :ddrId")
    List<DDRShiftDetail> fetchByDDRId(@Param("ddrId") Long ddrId);
}

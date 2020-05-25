package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.BreakTypeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.BreakType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 06/05/20
 */
@Repository
public interface BreakTypeRepository extends JpaRepository<BreakType, Long>, BreakTypeRepositoryCustom {

    @Query("SELECT b FROM BreakType b WHERE b.status = 'Y' AND b.id =:breakTypeId")
    Optional<BreakType> fetchActiveBreakType(@Param("breakTypeId") Long breakTypeId);
}

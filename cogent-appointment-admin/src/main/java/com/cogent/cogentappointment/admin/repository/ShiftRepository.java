package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.ShiftRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 06/05/20
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long>, ShiftRepositoryCustom {

    @Query("SELECT s FROM Shift s WHERE s.status = 'Y' AND s.id =:shiftId")
    Optional<Shift> fetchShiftById(@Param("shiftId") Long shiftId);

}

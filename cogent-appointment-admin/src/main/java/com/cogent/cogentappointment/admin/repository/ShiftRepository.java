package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.ShiftRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 06/05/20
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long>, ShiftRepositoryCustom {
}

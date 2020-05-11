package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.DDRShiftDetail;
import com.cogent.cogentappointment.persistence.model.DDRWeekDaysDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 08/05/20
 */
@Repository
public interface DDRWeekDaysDetailRepository extends JpaRepository<DDRWeekDaysDetail, Long> {

}

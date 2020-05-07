package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.DoctorShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author smriti on 07/05/20
 */
public interface DoctorShiftRepository extends JpaRepository<DoctorShift, Long> {

    @Query("SELECT ds FROM DoctorShift ds WHERE ds.doctor.id =:doctorId AND ds.shift.id =:shiftId")
    DoctorShift fetchByDoctorIdAndShiftId(@Param("doctorId") Long doctorId,
                                          @Param("shiftId") Long shiftId);
}

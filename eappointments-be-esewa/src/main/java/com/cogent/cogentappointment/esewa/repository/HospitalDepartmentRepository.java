package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.HospitalDepartmentRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author smriti on 28/05/20
 */
public interface HospitalDepartmentRepository extends JpaRepository<HospitalDepartment, Long>,
        HospitalDepartmentRepositoryCustom {

    @Query("SELECT hd FROM HospitalDepartment hd WHERE hd.status = 'Y' AND hd.id =:id AND hd.hospital.id =:hospitalId")
    Optional<HospitalDepartment> fetchActiveByIdAndHospitalId(@Param("id") Long id,
                                                              @Param("hospitalId") Long hospitalId);
}

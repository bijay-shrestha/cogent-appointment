package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.SpecializationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 2019-09-25
 */
@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long>, SpecializationRepositoryCustom {

    @Query("SELECT s FROM Specialization s WHERE s.status!='D' AND s.hospital.id =:hospitalId AND s.id = :specializationId")
    Optional<Specialization> findSpecializationByIdAndHospitalId(@Param("specializationId") Long specializationId,
                                                                 @Param("hospitalId") Long hospitalId);

    @Query("SELECT s FROM Specialization s WHERE s.status='Y' AND s.hospital.id =:hospitalId AND s.id = :specializationId")
    Optional<Specialization> findActiveSpecializationByIdAndHospitalId(@Param("specializationId") Long specializationId,
                                                                       @Param("hospitalId") Long hospitalId);

}

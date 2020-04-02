package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.UniversityRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 08/11/2019
 */
@Repository
public interface UniversityRepository extends JpaRepository<University, Long>, UniversityRepositoryCustom {

    @Query("SELECT u FROM University u WHERE u.status!='D' AND u.id = :id AND u.hospital.id =:hospitalId")
    Optional<University> fetchUniversityByIdAndHospitalId(@Param("id") Long id,
                                                          @Param("hospitalId") Long hospitalId);

    @Query("SELECT u FROM University u WHERE u.status='Y' AND u.id = :id")
    Optional<University> fetchActiveUniversityById(@Param("id") Long id);
}

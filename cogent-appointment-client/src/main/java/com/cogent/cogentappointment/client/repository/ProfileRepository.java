package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.ProfileRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 7/2/19
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileRepositoryCustom {

    @Query("SELECT p FROM Profile p WHERE p.department.hospital.id=:hospitalId AND p.status!='D' AND p.id = :id")
    Optional<Profile> findProfileByIdAndHospitalId(@Param("id") Long id,
                                                   @Param("hospitalId") Long hospitalId);

    @Query("SELECT p FROM Profile p WHERE p.department.hospital.id=:hospitalId AND p.status='Y' AND p.id = :id")
    Optional<Profile> findActiveProfileByIdAndHospitalId(@Param("id") Long id,
                                                         @Param("hospitalId") Long hospitalId);
}

package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DoctorSalutationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorSalutation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author rupak on 2020-05-13
 */
@Repository
public interface DoctorSalutationRepository extends JpaRepository<DoctorSalutation, Long>, DoctorSalutationRepositoryCustom {

    @Query("SELECT ds FROM DoctorSalutation ds WHERE ds.status!='D' AND ds.salutationId = :id")
    Optional<List<DoctorSalutation>> findDoctorSalutationBySalutationId(@Param("id") Long id);

    @Query("SELECT ds FROM DoctorSalutation ds WHERE ds.status!='D' AND ds.id = :id")
    Optional<DoctorSalutation> findDoctorSalutationById(@Param("id") Long id);
}

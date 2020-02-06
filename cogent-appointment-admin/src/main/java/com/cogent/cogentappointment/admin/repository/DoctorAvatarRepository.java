package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.DoctorAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 2019-09-29
 */
@Repository
public interface DoctorAvatarRepository extends JpaRepository<DoctorAvatar, Long> {

    @Query("SELECT c FROM DoctorAvatar c WHERE c.doctorId.id = :id")
    DoctorAvatar findByDoctorId(@Param("id") Long id);
}

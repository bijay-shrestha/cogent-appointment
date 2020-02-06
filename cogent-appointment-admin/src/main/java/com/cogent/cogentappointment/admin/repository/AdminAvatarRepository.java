package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.AdminAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 2019-08-27
 */
@Repository
public interface AdminAvatarRepository extends JpaRepository<AdminAvatar, Long> {

    @Query("SELECT a FROM AdminAvatar a WHERE a.admin.id = :id")
    AdminAvatar findAdminAvatarByAdminId(@Param("id") Long id);
}

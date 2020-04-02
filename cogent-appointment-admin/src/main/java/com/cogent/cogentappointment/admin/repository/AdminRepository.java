package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AdminRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, AdminRepositoryCustom {

    @Query("SELECT a FROM Admin a WHERE a.status!='D' AND a.id = :id")
    Optional<Admin> findAdminById(@Param("id") Long id);

}

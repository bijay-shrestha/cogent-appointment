package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.model.Admin;
import com.cogent.cogentappointment.client.repository.custom.AdminRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, AdminRepositoryCustom {

    @Query("SELECT a FROM Admin a WHERE a.status!='D' AND a.id = :id")
    Optional<Admin> findAdminById(@Param("id") Long id);

    @Query("SELECT a FROM Admin a WHERE a.status!='D' AND a.username = :username")
    Optional<Admin> findAdminByUsername(@Param("username") String username);

    @Query("SELECT a FROM Admin a WHERE (a.username=:username OR a.email =:email) AND a.status != 'D'")
    Optional<Admin> fetchAdminByUsernameOrEmail(@Param("username") String username,
                                                @Param("email") String email);
}

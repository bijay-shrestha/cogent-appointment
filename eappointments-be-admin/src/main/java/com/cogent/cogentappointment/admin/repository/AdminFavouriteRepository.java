package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AdminFavouriteRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminFavouriteRepository extends JpaRepository<AdminFavourite, Long>,
        AdminFavouriteRepositoryCustom {

    @Query("SELECT af FROM AdminFavourite af WHERE af.adminId.id=:adminId AND af.userMenuId=:userMenuId AND af.status='Y'")
    Optional<AdminFavourite> findAdminFavourite(@Param("adminId") Long id, @Param("userMenuId") Long userMenuId);

    @Query("SELECT af.userMenuId FROM AdminFavourite af WHERE af.adminId.id=:adminId AND af.status='Y'")
    Optional<List<Long>> findUserMenuIdByAdmin(@Param("adminId") Long adminId);
}
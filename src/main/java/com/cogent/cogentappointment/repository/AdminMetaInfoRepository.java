package com.cogent.cogentappointment.repository;

import com.cogent.cogentappointment.model.AdminMetaInfo;
import com.cogent.cogentappointment.repository.custom.AdminMetaInfoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 2019-08-27
 */
@Repository
public interface AdminMetaInfoRepository extends JpaRepository<AdminMetaInfo, Long>,
        AdminMetaInfoRepositoryCustom {

    @Query("SELECT a FROM AdminMetaInfo a WHERE a.admin.id = :id")
    Optional<AdminMetaInfo> findAdminMetaInfoByAdminId(@Param("id") Long id);
}

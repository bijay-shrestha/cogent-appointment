package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AdminMetaInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminMetaInfo;
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

    @Query("SELECT a FROM AdminMetaInfo a WHERE a.admin.id = :id AND a.admin.status!='D'")
    Optional<AdminMetaInfo> findAdminMetaInfoByAdminId(@Param("id") Long id);
}

package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.CompanyProfileRepositoryCustom;
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
public interface CompanyProfileRepository extends JpaRepository<Profile, Long>, CompanyProfileRepositoryCustom {

    @Query("SELECT p FROM Profile p WHERE p.status!='D' AND p.id = :id AND p.isCompanyProfile = 'Y'")
    Optional<Profile> findCompanyProfileById(@Param("id") Long id);
}

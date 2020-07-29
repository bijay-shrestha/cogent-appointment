package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.HttpRequestMethodRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HttpRequestMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author rupak on 2020-05-19
 */
@Repository
public interface HttpRequestMethodRepository extends JpaRepository<HttpRequestMethod,Long>,
        HttpRequestMethodRepositoryCustom {

    @Query(" SELECT hrm FROM HttpRequestMethod hrm WHERE hrm.id=:id AND hrm.status = 'Y'")
    Optional<HttpRequestMethod> httpRequestMethodById(@Param("id") Long featureTypeId);
}

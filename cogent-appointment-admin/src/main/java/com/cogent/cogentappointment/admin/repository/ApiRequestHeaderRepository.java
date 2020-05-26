package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ApiRequestHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rupak on 2020-05-19
 */
@Repository
public interface ApiRequestHeaderRepository extends JpaRepository<ApiRequestHeader,Long> {
}

package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ApiQueryParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rupak on 2020-05-19
 */
@Repository
public interface ApiQueryParametersRepository extends JpaRepository<ApiQueryParameters,Long> {
}
